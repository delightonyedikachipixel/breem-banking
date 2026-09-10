package com.task.breem.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoundUpSavingsResponse {
    private UUID sourceAccountId;
    private UUID savingsAccountId;
    private BigDecimal balanceBefore;
    private BigDecimal amountSaved;
    private BigDecimal balanceAfter;
    private BigDecimal savingsBalanceAfter;
    private boolean saved;
}
