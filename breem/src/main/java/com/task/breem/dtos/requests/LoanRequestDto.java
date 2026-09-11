package com.task.breem.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter
@Getter
public class LoanRequestDto {

    @NotNull
    private UUID userId;

    @NotNull
    private BigDecimal principalAmount;

    @NotNull
    private double interestRate;

    @NotNull
    private int tenureMonths;

}
