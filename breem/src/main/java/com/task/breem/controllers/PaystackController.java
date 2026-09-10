package com.task.breem.controllers;

import com.task.breem.dtos.requests.PaystackInitRequest;
import com.task.breem.dtos.requests.PaystackWebhookRequest;
import com.task.breem.dtos.responses.ApiResponse;
import com.task.breem.dtos.responses.PaystackTransactionResponse;
import com.task.breem.services.PaystackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/paystack")
@RequiredArgsConstructor
public class PaystackController {

    private final PaystackService paystackService;

    @PostMapping("/initialize")
    public ResponseEntity<ApiResponse<PaystackTransactionResponse>> initialize(@Valid @RequestBody PaystackInitRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Paystack transaction initialized", paystackService.initialize(request)));
    }

    @PostMapping("/webhook")
    public ResponseEntity<ApiResponse<PaystackTransactionResponse>> webhook(@Valid @RequestBody PaystackWebhookRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Webhook processed", paystackService.handleWebhook(request)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<PaystackTransactionResponse>>> getForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Paystack transactions retrieved", paystackService.getForUser(userId)));
    }
}
