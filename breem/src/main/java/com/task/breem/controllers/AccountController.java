package com.task.breem.controllers;

import com.task.breem.dtos.requests.CreateAccountRequest;
import com.task.breem.dtos.responses.AccountResponse;
import com.task.breem.dtos.responses.ApiResponse;
import com.task.breem.services.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Account created", response));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(@PathVariable UUID accountId) {
        return ResponseEntity.ok(ApiResponse.success("Account retrieved", accountService.getAccount(accountId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAccountsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Accounts retrieved", accountService.getAccountsForUser(userId)));
    }
}
