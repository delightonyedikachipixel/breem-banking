package com.task.breem.dtos.requests;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @NotNull
    private UUID sourceAccountId;

    @NotNull
    private UUID destinationAccountId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    private String category;
    private String description;
}
