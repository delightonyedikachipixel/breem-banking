package com.task.breem.services;

import com.task.breem.data.models.Transaction;
import com.task.breem.dtos.requests.FundAccountRequest;
import com.task.breem.dtos.requests.TransferRequest;
import com.task.breem.dtos.responses.TransactionResponse;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    TransactionResponse fundAccount(FundAccountRequest request);
    TransactionResponse transfer(TransferRequest request);
    List<TransactionResponse> getForAccount(UUID accountId);
    TransactionResponse getByReference(String reference);
    TransactionResponse getResponse(Transaction transaction);
}
