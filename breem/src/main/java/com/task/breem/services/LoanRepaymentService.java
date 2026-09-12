package com.task.breem.services;

import com.task.breem.config.RepaymentArgs;
import com.task.breem.dtos.responses.LoanRepaymentResponse;

import java.util.List;
import java.util.UUID;

public interface LoanRepaymentService {

    LoanRepaymentResponse repayLoan (RepaymentArgs repaymentArgs);
    List<LoanRepaymentResponse> getAllPayments (UUID loanId);
}
