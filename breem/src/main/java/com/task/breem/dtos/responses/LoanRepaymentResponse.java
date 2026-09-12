package com.task.breem.dtos.responses;

import com.task.breem.data.models.Enums.LoanRepaymentStatus;
import com.task.breem.data.models.Enums.RepaymentFrequency;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class LoanRepaymentResponse {

    private UUID id;
    private LoanResponseDto loan;
    private BigDecimal amount;
    private LocalDateTime dueDate;
    private LocalDateTime paidDate;
    private LoanRepaymentStatus status;
    private RepaymentFrequency repaymentFrequency;
}
