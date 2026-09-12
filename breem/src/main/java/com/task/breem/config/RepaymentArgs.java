package com.task.breem.config;

import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.dtos.requests.TransferRequest;
import lombok.Data;

import java.util.UUID;

@Data
public class RepaymentArgs {
    private UUID loanId;
    private UUID accountId;
    private UUID transactionId;
    private TransferRequest transferRequest;
    private NotificationType notificationType;
}
