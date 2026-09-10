package com.task.breem.services.impl;

import com.task.breem.data.models.Account;
import com.task.breem.data.models.Enums.AccountType;
import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.data.models.Enums.TransactionStatus;
import com.task.breem.data.models.Enums.TransactionType;
import com.task.breem.data.models.PaystackTransaction;
import com.task.breem.data.models.Transaction;
import com.task.breem.data.models.User;
import com.task.breem.data.repository.AccountRepository;
import com.task.breem.data.repository.PaystackTransactionRepository;
import com.task.breem.data.repository.TransactionRepository;
import com.task.breem.data.repository.UserRepository;
import com.task.breem.dtos.requests.PaystackInitRequest;
import com.task.breem.dtos.requests.PaystackWebhookRequest;
import com.task.breem.dtos.responses.PaystackTransactionResponse;
import com.task.breem.exceptions.ResourceNotFoundException;
import com.task.breem.services.NotificationService;
import com.task.breem.services.PaystackService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaystackServiceImpl implements PaystackService {

    private final PaystackTransactionRepository paystackTransactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Override
    public PaystackTransactionResponse initialize(PaystackInitRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Account account = accountRepository.findById(request.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + request.getAccountId()));
        if (!account.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Account does not belong to this user");
        }

        PaystackTransaction paystackTransaction = PaystackTransaction.builder()
                .user(user)
                .amount(request.getAmount())
                .purpose(request.getPurpose())
                .status(TransactionStatus.PENDING)
                .paystackReference(generatePaystackReference())
                .build();
        return toResponse(paystackTransactionRepository.save(paystackTransaction));
    }

    @Override
    @Transactional
    public PaystackTransactionResponse handleWebhook(PaystackWebhookRequest request) {
        PaystackTransaction paystackTransaction = paystackTransactionRepository
                .findByPaystackReference(request.getPaystackReference())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paystack transaction not found for reference: " + request.getPaystackReference()));

        if (paystackTransaction.getStatus() == TransactionStatus.SUCCESSFUL) {
            return toResponse(paystackTransaction); // already processed - webhook can be retried by Paystack
        }

        boolean succeeded = "success".equalsIgnoreCase(request.getStatus());
        paystackTransaction.setStatus(succeeded ? TransactionStatus.SUCCESSFUL : TransactionStatus.FAILED);
        paystackTransaction.setAuthorizationCode(request.getAuthorizationCode());

        if (succeeded) {
            Account account = resolveDestinationAccount(paystackTransaction.getUser().getId());

            account.setBalance(account.getBalance().add(paystackTransaction.getAmount()));
            accountRepository.save(account);

            Transaction transaction = Transaction.builder()
                    .account(account)
                    .type(TransactionType.DEPOSIT)
                    .amount(paystackTransaction.getAmount())
                    .description("Paystack funding - " + paystackTransaction.getPurpose())
                    .reference(paystackTransaction.getPaystackReference())
                    .status(TransactionStatus.SUCCESSFUL)
                    .build();
            transaction = transactionRepository.save(transaction);
            paystackTransaction.setTransaction(transaction);

            notificationService.notify(paystackTransaction.getUser().getId(), NotificationType.TRANSACTION,
                    "Wallet funded",
                    "Your wallet was funded with NGN " + paystackTransaction.getAmount() + " via Paystack.");
        } else {
            notificationService.notify(paystackTransaction.getUser().getId(), NotificationType.TRANSACTION,
                    "Funding failed",
                    "Your Paystack funding of NGN " + paystackTransaction.getAmount() + " could not be completed.");
        }

        return toResponse(paystackTransactionRepository.save(paystackTransaction));
    }

    @Override
    public List<PaystackTransactionResponse> getForUser(UUID userId) {
        return paystackTransactionRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }


    private Account resolveDestinationAccount(UUID userId) {
        Optional<Account> current = accountRepository.findByUserIdAndAccountType(userId, AccountType.CURRENT);
        if (current.isPresent()) {
            return current.get();
        }
        return accountRepository.findByUserId(userId).stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("User has no account to fund: " + userId));
    }

    private String generatePaystackReference() {
        return "PSK-" + UUID.randomUUID().toString().replace("-", "").substring(0, 18).toUpperCase();
    }

    private PaystackTransactionResponse toResponse(PaystackTransaction p) {
        return PaystackTransactionResponse.builder()
                .id(p.getId())
                .userId(p.getUser().getId())
                .transactionId(p.getTransaction() != null ? p.getTransaction().getId() : null)
                .paystackReference(p.getPaystackReference())
                .amount(p.getAmount())
                .purpose(p.getPurpose())
                .status(p.getStatus())
                .authorizationCode(p.getAuthorizationCode())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
