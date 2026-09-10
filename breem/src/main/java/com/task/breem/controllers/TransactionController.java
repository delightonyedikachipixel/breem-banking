package com.task.breem.controllers;

import com.task.breem.dtos.requests.FundAccountRequest;
import com.task.breem.dtos.requests.TransferRequest;
import com.task.breem.dtos.responses.ApiResponse;
import com.task.breem.dtos.responses.TransactionResponse;
import com.task.breem.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/fund")
    public ResponseEntity<ApiResponse<TransactionResponse>> fundAccount(@Valid @RequestBody FundAccountRequest request) {
        TransactionResponse response = transactionService.fundAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Account funded", response));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransactionResponse>> transfer(@Valid @RequestBody TransferRequest request) {
        TransactionResponse response = transactionService.transfer(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Transfer successful", response));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<TransactionResponse>>> getForAccount(@PathVariable UUID accountId) {
        return ResponseEntity.ok(ApiResponse.success("Transactions retrieved", transactionService.getForAccount(accountId)));
    }

    @GetMapping("/reference/{reference}")
    public ResponseEntity<ApiResponse<TransactionResponse>> getByReference(@PathVariable String reference) {
        return ResponseEntity.ok(ApiResponse.success("Transaction retrieved", transactionService.getByReference(reference)));
    }
}
