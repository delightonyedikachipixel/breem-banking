package com.task.breem.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetEntryResponse {
    private UUID id;
    private UUID budgetId;
    private UUID transactionId;
    private BigDecimal amount;
    private LocalDate entryDate;
}
