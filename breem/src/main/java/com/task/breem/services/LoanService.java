package com.task.breem.services;

import com.task.breem.dtos.requests.LoanRequestDto;
import com.task.breem.dtos.responses.LoanResponseDto;

import java.util.UUID;

public interface LoanService {

    LoanResponseDto applyForLoan (LoanRequestDto requestDto);
    LoanResponseDto viewLoan (UUID loanId);
    LoanResponseDto approveLoan (UUID loanId);
    LoanResponseDto rejectLoan (UUID loanId);
    LoanResponseDto disburseLoan (UUID loanId);
    LoanResponseDto cancelLoan (UUID loanId);
    LoanResponseDto markCompleted (UUID loanId);
    LoanResponseDto markDefaulted (UUID loanId);
}
