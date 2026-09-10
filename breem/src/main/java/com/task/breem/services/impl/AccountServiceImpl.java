package com.task.breem.services.impl;

import com.task.breem.data.models.Account;
import com.task.breem.data.models.Enums.AccountStatus;
import com.task.breem.data.models.User;
import com.task.breem.data.repository.AccountRepository;
import com.task.breem.data.repository.UserRepository;
import com.task.breem.dtos.requests.CreateAccountRequest;
import com.task.breem.dtos.responses.AccountResponse;
import com.task.breem.exceptions.ResourceNotFoundException;
import com.task.breem.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Account account = Account.builder()
                .user(user)
                .accountNumber(generateUniqueAccountNumber())
                .accountType(request.getAccountType())
                .balance(BigDecimal.ZERO)
                .currency(request.getCurrency() == null || request.getCurrency().isBlank()
                        ? "NGN" : request.getCurrency())
                .status(AccountStatus.ACTIVE)
                .build();

        return getResponse(accountRepository.save(account));
    }

    @Override
    public AccountResponse getAccount(UUID accountId) {
        return getResponse(getAccountEntity(accountId));
    }

    @Override
    public Account getAccountEntity(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found: " + accountId));
    }

    @Override
    public List<AccountResponse> getAccountsForUser(UUID userId) {
        return accountRepository.findByUserId(userId)
                .stream().map(this::getResponse).collect(Collectors.toList());
    }

    @Override
    public AccountResponse getResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUser().getId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .currency(account.getCurrency())
                .status(account.getStatus())
                .createdAt(account.getCreatedAt())
                .build();
    }

    @Override
    public Account getOrCreateSavingsAccount(UUID userId) {
        return accountRepository.findByUserIdAndAccountType(userId, com.task.breem.data.models.Enums.AccountType.SAVINGS)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));
                    Account savings = Account.builder()
                            .user(user)
                            .accountNumber(generateUniqueAccountNumber())
                            .accountType(com.task.breem.data.models.Enums.AccountType.SAVINGS)
                            .balance(BigDecimal.ZERO)
                            .currency("NGN")
                            .status(AccountStatus.ACTIVE)
                            .build();
                    return accountRepository.save(savings);
                });
    }

    private String generateUniqueAccountNumber() {
        String candidate;
        do {
            candidate = String.valueOf(1_000_000_000L + (long) (RANDOM.nextDouble() * 9_000_000_000L))
                    .substring(0, 10);
        } while (accountRepository.existsByAccountNumber(candidate));
        return candidate;
    }
}
