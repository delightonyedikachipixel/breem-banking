package com.task.breem.dtos.responses;

import com.task.breem.data.models.Enums.BudgetPeriod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponse {
    private UUID id;
    private UUID userId;
    private String category;
    private BigDecimal limitAmount;
    private BigDecimal spentAmount;
    private BigDecimal remainingAmount;
    private BudgetPeriod period;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createdAt;
}
