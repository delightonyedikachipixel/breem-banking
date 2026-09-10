package com.task.breem.dtos.responses;

import com.task.breem.data.models.Enums.TransactionStatus;
import com.task.breem.data.models.Enums.TransactionType;
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
public class TransactionResponse {
    private UUID id;
    private UUID accountId;
    private TransactionType type;
    private BigDecimal amount;
    private String category;
    private String description;
    private String reference;
    private TransactionStatus status;
    private LocalDateTime createdAt;
}
