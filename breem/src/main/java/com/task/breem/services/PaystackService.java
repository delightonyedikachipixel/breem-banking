package com.task.breem.services;

import com.task.breem.dtos.requests.PaystackInitRequest;
import com.task.breem.dtos.requests.PaystackWebhookRequest;
import com.task.breem.dtos.responses.PaystackTransactionResponse;

import java.util.List;
import java.util.UUID;

public interface PaystackService {
    PaystackTransactionResponse initialize(PaystackInitRequest request);

    PaystackTransactionResponse handleWebhook(PaystackWebhookRequest request);

    List<PaystackTransactionResponse> getForUser(UUID userId);
}
