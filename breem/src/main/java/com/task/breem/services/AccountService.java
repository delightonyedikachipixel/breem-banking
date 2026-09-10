package com.task.breem.services;

import com.task.breem.data.models.Account;
import com.task.breem.dtos.requests.CreateAccountRequest;
import com.task.breem.dtos.responses.AccountResponse;

import java.util.List;
import java.util.UUID;

public interface AccountService {
    AccountResponse createAccount(CreateAccountRequest request);
    AccountResponse getAccount(UUID accountId);
    Account getAccountEntity(UUID accountId);
    List<AccountResponse> getAccountsForUser(UUID userId);
    AccountResponse getResponse(Account account);
    Account getOrCreateSavingsAccount(UUID userId);
}
