package com.task.breem.services.impl;

import com.task.breem.data.models.Account;
import com.task.breem.data.models.Enums.AccountStatus;
import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.data.models.Enums.TransactionStatus;
import com.task.breem.data.models.Enums.TransactionType;
import com.task.breem.data.models.Transaction;
import com.task.breem.data.repository.AccountRepository;
import com.task.breem.data.repository.TransactionRepository;
import com.task.breem.dtos.requests.FundAccountRequest;
import com.task.breem.dtos.requests.TransferRequest;
import com.task.breem.dtos.responses.TransactionResponse;
import com.task.breem.exceptions.InsufficientBalanceException;
import com.task.breem.exceptions.ResourceNotFoundException;
import com.task.breem.services.BudgetService;
import com.task.breem.services.NotificationService;
import com.task.breem.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final BudgetService budgetService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public TransactionResponse fundAccount(FundAccountRequest request) {
        Account account = getActiveAccount(request.getAccountId());

        account.setBalance(account.getBalance().add(request.getAmount()));
        accountRepository.save(account);

        Transaction transaction = Transaction.builder()
                .account(account)
                .type(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .description(request.getDescription())
                .reference(generateReference("DEP"))
                .status(TransactionStatus.SUCCESSFUL)
                .build();
        transaction = transactionRepository.save(transaction);

        notificationService.notify(account.getUser().getId(), NotificationType.TRANSACTION,
                "Account credited",
                "Your account was credited with " + formatAmount(request.getAmount())
                        + ". New balance: " + formatAmount(account.getBalance()));

        return toResponse(transaction);
    }

    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts must be different");
        }

        Account source = getActiveAccount(request.getSourceAccountId());
        Account destination = getActiveAccount(request.getDestinationAccountId());

        if (source.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in source account");
        }

        source.setBalance(source.getBalance().subtract(request.getAmount()));
        destination.setBalance(destination.getBalance().add(request.getAmount()));
        accountRepository.save(source);
        accountRepository.save(destination);

        String sharedReference = generateReference("TRF");

        Transaction debit = Transaction.builder()
                .account(source)
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .category(request.getCategory())
                .description(request.getDescription())
                .reference(sharedReference + "-DR")
                .status(TransactionStatus.SUCCESSFUL)
                .build();
        debit = transactionRepository.save(debit);

        Transaction credit = Transaction.builder()
                .account(destination)
                .type(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .description(request.getDescription())
                .reference(sharedReference + "-CR")
                .status(TransactionStatus.SUCCESSFUL)
                .build();
        transactionRepository.save(credit);

        // Only the debit side (money leaving the account) is tracked against a spending budget.
        budgetService.recordAgainstBudgetIfMatched(debit);

        notificationService.notify(source.getUser().getId(), NotificationType.TRANSACTION,
                "Transfer sent",
                formatAmount(request.getAmount()) + " was sent from account " + source.getAccountNumber());
        notificationService.notify(destination.getUser().getId(), NotificationType.TRANSACTION,
                "Transfer received",
                formatAmount(request.getAmount()) + " was received into account " + destination.getAccountNumber());

        return toResponse(debit);
    }

    @Override
    public List<TransactionResponse> getForAccount(UUID accountId) {
        return transactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public TransactionResponse getByReference(String reference) {
        return toResponse(transactionRepository.findByReference(reference)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found: " + reference)));
    }

    @Override
    public TransactionResponse getResponse(Transaction transaction) {
        return toResponse(transaction);
    }

    private Account getActiveAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new IllegalArgumentException("Account " + account.getAccountNumber() + " is not active");
        }
        return account;
    }

    private String generateReference(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }

    private String formatAmount(BigDecimal amount) {
        return "NGN " + amount.setScale(2, java.math.RoundingMode.HALF_UP);
    }

    private TransactionResponse toResponse(Transaction t) {
        return TransactionResponse.builder()
                .id(t.getId())
                .accountId(t.getAccount().getId())
                .type(t.getType())
                .amount(t.getAmount())
                .category(t.getCategory())
                .description(t.getDescription())
                .reference(t.getReference())
                .status(t.getStatus())
                .createdAt(t.getCreatedAt())
                .build();
    }
}
