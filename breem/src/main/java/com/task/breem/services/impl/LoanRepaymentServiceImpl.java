package com.task.breem.services.impl;

import com.task.breem.data.models.Account;
import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.data.models.Enums.RepaymentFrequency;
import com.task.breem.data.models.Loan;
import com.task.breem.data.models.LoanRepayment;
import com.task.breem.data.repository.AccountRepository;
import com.task.breem.data.repository.LoanRepaymentRepository;
import com.task.breem.data.repository.LoanRepository;
import com.task.breem.dtos.requests.TransferRequest;
import com.task.breem.dtos.responses.LoanRepaymentResponse;
import com.task.breem.exceptions.InsufficientBalanceException;
import com.task.breem.exceptions.LoanNotFoundException;
import com.task.breem.exceptions.UserNotFoundException;
import com.task.breem.services.LoanRepaymentService;
import com.task.breem.services.LoanService;
import com.task.breem.services.NotificationService;
import com.task.breem.services.TransactionService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoanRepaymentServiceImpl implements LoanRepaymentService {

    private final LoanRepaymentRepository loanRepaymentRepository;
    private final NotificationService notificationService;
    private final LoanService loanService;
    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final LoanRepository loanRepository;
    private final ModelMapper modelMapper;

    @Override
    public LoanRepaymentResponse repayLoan(UUID loanId, UUID accountId, UUID transactionId, TransferRequest transferRequest, NotificationType notificationType) {

        Account account = accountRepository.findById(accountId).orElseThrow(
                ()-> new UserNotFoundException("Account not found"));

        Loan loan  = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));

        LocalDateTime whenNextDue = getNextDueDate(loan.getFrequency(), loan.getDisbursedAt());

        BigDecimal repaymentAmount = loanService.calculateInstallment(loanId);

        if (account.getBalance().compareTo(repaymentAmount) >= 0) {
            LoanRepayment loanRepayment = LoanRepayment.builder()
                    .loan(loan)
                    .amount(repaymentAmount)
                    .dueDate(whenNextDue)
                    .paidDate(LocalDateTime.now())
                    .build();

            loanRepaymentRepository.save(loanRepayment);

            transactionService.transfer(transferRequest);

            notificationService.notify(loan.getUser().getId(), notificationType, "Loan", "Loan repayment ");

            return  modelMapper.map(loanRepayment, LoanRepaymentResponse.class);
        }

        throw new InsufficientBalanceException("Insufficient balance");
    }

    private LocalDateTime getNextDueDate(RepaymentFrequency frequency, LocalDateTime date) {
        return switch(frequency){
            case WEEKLY -> date.plusWeeks(1);
            case MONTHLY -> date.plusMonths(1);
            case YEARLY -> date.plusYears(1);
        };

    }

}
