package com.task.breem.dtos.requests;

import com.task.breem.data.models.Enums.BudgetPeriod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateBudgetRequest {
    @NotNull
    private UUID userId;

    @NotBlank
    private String category;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal limitAmount;

    @NotNull
    private BudgetPeriod period;

    private LocalDate startDate;
    private LocalDate endDate;
}
