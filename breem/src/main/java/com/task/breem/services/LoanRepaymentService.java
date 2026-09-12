package com.task.breem.services;

import com.task.breem.data.models.Enums.NotificationType;

import com.task.breem.dtos.requests.TransferRequest;
import com.task.breem.dtos.responses.LoanRepaymentResponse;

import java.util.UUID;

public interface LoanRepaymentService {

    LoanRepaymentResponse repayLoan (UUID loanId, UUID accountId, UUID transactionId, TransferRequest transferRequest,  NotificationType notificationType);

}
