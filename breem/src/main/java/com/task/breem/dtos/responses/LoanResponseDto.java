package com.task.breem.dtos.responses;

import com.task.breem.data.models.Enums.LoanStatus;
import com.task.breem.data.models.User;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
public class LoanResponseDto {

    private UUID id;

    //TODO: Change User to UserResponseDto

    private User user;

    private BigDecimal principalAmount;

    private double interestRate;

    private int tenureMonths;

    private LoanStatus status;

    private LocalDateTime disbursedAt;
}
