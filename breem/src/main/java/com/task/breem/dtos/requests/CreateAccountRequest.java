package com.task.breem.dtos.requests;

import com.task.breem.data.models.Enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccountRequest {
    @NotNull
    private UUID userId;

    @NotNull
    private AccountType accountType;

    private String currency;
}
