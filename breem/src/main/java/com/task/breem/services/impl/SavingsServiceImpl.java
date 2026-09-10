package com.task.breem.services.impl;

import com.task.breem.data.models.Account;
import com.task.breem.data.models.Enums.AccountStatus;
import com.task.breem.data.models.Enums.AccountType;
import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.data.models.Enums.SuggestionType;
import com.task.breem.data.models.Enums.TransactionStatus;
import com.task.breem.data.models.Enums.TransactionType;
import com.task.breem.data.models.Transaction;
import com.task.breem.data.repository.AccountRepository;
import com.task.breem.data.repository.TransactionRepository;
import com.task.breem.dtos.requests.RoundUpSavingsRequest;
import com.task.breem.dtos.responses.RoundUpSavingsResponse;
import com.task.breem.exceptions.ResourceNotFoundException;
import com.task.breem.services.AccountService;
import com.task.breem.services.FinancialSuggestionService;
import com.task.breem.services.NotificationService;
import com.task.breem.services.SavingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SavingsServiceImpl implements SavingsService {

    private static final BigDecimal DEFAULT_ROUND_UNIT = new BigDecimal("100");

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final NotificationService notificationService;
    private final FinancialSuggestionService financialSuggestionService;

    @Override
    @Transactional
    public RoundUpSavingsResponse roundUpSave(RoundUpSavingsRequest request) {
        BigDecimal roundUnit = (request.getRoundUnit() == null || request.getRoundUnit().signum() <= 0)
                ? DEFAULT_ROUND_UNIT : request.getRoundUnit();

        Account source = accountRepository.findByUserIdAndAccountType(request.getUserId(), AccountType.CURRENT)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User has no CURRENT account to round-up from: " + request.getUserId()));

        if (source.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account " + source.getAccountNumber() + " is not active");
        }

        BigDecimal balanceBefore = source.getBalance();
        BigDecimal remainder = balanceBefore.remainder(roundUnit);

        Account savings = accountService.getOrCreateSavingsAccount(request.getUserId());

        if (remainder.signum() == 0) {
            return RoundUpSavingsResponse.builder()
                    .sourceAccountId(source.getId())
                    .savingsAccountId(savings.getId())
                    .balanceBefore(balanceBefore)
                    .amountSaved(BigDecimal.ZERO)
                    .balanceAfter(balanceBefore)
                    .savingsBalanceAfter(savings.getBalance())
                    .saved(false)
                    .build();
        }

        BigDecimal balanceAfter = balanceBefore.subtract(remainder);
        source.setBalance(balanceAfter);
        savings.setBalance(savings.getBalance().add(remainder));
        accountRepository.save(source);
        accountRepository.save(savings);

        String sharedReference = "SAV-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();

        Transaction debit = Transaction.builder()
                .account(source)
                .type(TransactionType.ROUND_UP_SAVINGS)
                .amount(remainder)
                .category("Savings")
                .description("Round-up savings to reach a balance of " + balanceAfter)
                .reference(sharedReference + "-DR")
                .status(TransactionStatus.SUCCESSFUL)
                .build();
        transactionRepository.save(debit);

        Transaction credit = Transaction.builder()
                .account(savings)
                .type(TransactionType.ROUND_UP_SAVINGS)
                .amount(remainder)
                .description("Round-up savings from account " + source.getAccountNumber())
                .reference(sharedReference + "-CR")
                .status(TransactionStatus.SUCCESSFUL)
                .build();
        transactionRepository.save(credit);

        String message = "We saved NGN " + remainder + " from your balance to round it down to NGN "
                + balanceAfter + ". Your savings balance is now NGN " + savings.getBalance() + ".";
        notificationService.notify(request.getUserId(), NotificationType.SAVINGS, "Round-up savings", message);
        financialSuggestionService.suggest(request.getUserId(), SuggestionType.SAVINGS_TIP, message);

        return RoundUpSavingsResponse.builder()
                .sourceAccountId(source.getId())
                .savingsAccountId(savings.getId())
                .balanceBefore(balanceBefore)
                .amountSaved(remainder)
                .balanceAfter(balanceAfter)
                .savingsBalanceAfter(savings.getBalance())
                .saved(true)
                .build();
    }
}
