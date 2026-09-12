package com.task.breem.services.impl;

import com.task.breem.config.RepaymentArgs;
import com.task.breem.data.models.Account;
import com.task.breem.data.models.Enums.LoanRepaymentStatus;
import com.task.breem.data.models.Enums.RepaymentFrequency;
import com.task.breem.data.models.Loan;
import com.task.breem.data.models.LoanRepayment;
import com.task.breem.data.repository.AccountRepository;
import com.task.breem.data.repository.LoanRepaymentRepository;
import com.task.breem.data.repository.LoanRepository;
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
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
    public LoanRepaymentResponse repayLoan(RepaymentArgs repaymentArgs) {

        Account account = accountRepository.findById(repaymentArgs.getAccountId()).orElseThrow(
                ()-> new UserNotFoundException("Account not found"));

        Loan loan  = loanRepository.findById(repaymentArgs.getLoanId()).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));

        LocalDateTime whenNextDue = getNextDueDate(loan.getFrequency(), loan.getDisbursedAt());

        BigDecimal repaymentAmount = loanService.calculateInstallment(repaymentArgs.getLoanId());

        if (account.getBalance().compareTo(repaymentAmount) >= 0) {
            LoanRepayment loanRepayment = LoanRepayment.builder()
                    .loan(loan)
                    .amount(repaymentAmount)
                    .dueDate(whenNextDue)
                    .paidDate(LocalDateTime.now())
                    .status(LoanRepaymentStatus.PAID)
                    .build();

            loanRepaymentRepository.save(loanRepayment);

            transactionService.transfer(repaymentArgs.getTransferRequest());

            notificationService.notify(loan.getUser().getId(), repaymentArgs.getNotificationType(), "Loan", "Loan repayment ");

            return  modelMapper.map(loanRepayment, LoanRepaymentResponse.class);
        }

        throw new InsufficientBalanceException("Insufficient balance");
    }

    @Override
    public List<LoanRepaymentResponse> getAllPayments(UUID loanId) {
        List <LoanRepayment> repayments = loanRepaymentRepository.findAllByLoan_id(loanId);
        Type type = new TypeToken<List<LoanRepaymentResponse>>(){}.getType();
        return modelMapper.map(repayments, type);
    }

    private LocalDateTime getNextDueDate(RepaymentFrequency frequency, LocalDateTime date) {
        return switch(frequency){
            case WEEKLY -> date.plusWeeks(1);
            case MONTHLY -> date.plusMonths(1);
            case YEARLY -> date.plusYears(1);
        };

    }

}
