package com.task.breem.dtos.responses;

import com.task.breem.data.models.Enums.PaystackPurpose;
import com.task.breem.data.models.Enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaystackTransactionResponse {
    private UUID id;
    private UUID userId;
    private UUID transactionId;
    private String paystackReference;
    private BigDecimal amount;
    private PaystackPurpose purpose;
    private TransactionStatus status;
    private String authorizationCode;
    private LocalDateTime createdAt;
}
