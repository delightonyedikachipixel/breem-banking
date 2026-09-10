package com.task.breem.dtos.requests;

import com.task.breem.data.models.Enums.PaystackPurpose;
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
public class PaystackInitRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private UUID accountId;

    @NotNull
    @DecimalMin(value = "0.01")
    private BigDecimal amount;

    @NotNull
    private PaystackPurpose purpose;
}
