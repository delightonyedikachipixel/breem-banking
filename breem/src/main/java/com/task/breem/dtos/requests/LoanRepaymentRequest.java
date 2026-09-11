package com.task.breem.dtos.requests;

import com.task.breem.data.models.Enums.LoanRepaymentStatus;
import com.task.breem.data.models.Loan;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class LoanRepaymentRequest {

    private Loan loan;

    private BigDecimal amount;

    private Instant dueDate;

    private Instant paidDate;

    private LoanRepaymentStatus status;
}
