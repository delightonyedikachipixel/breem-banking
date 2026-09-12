package com.task.breem.services.impl;

import com.task.breem.data.models.Enums.LoanStatus;
import com.task.breem.data.models.Enums.RepaymentFrequency;
import com.task.breem.data.models.Loan;
import com.task.breem.data.repository.LoanRepository;
import com.task.breem.data.repository.UserRepository;
import com.task.breem.dtos.requests.LoanRequestDto;
import com.task.breem.dtos.responses.LoanResponseDto;
import com.task.breem.exceptions.LoanNotFoundException;
import com.task.breem.exceptions.UserNotFoundException;
import com.task.breem.services.LoanService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public LoanResponseDto applyForLoan(LoanRequestDto requestDto) {
        Loan loan = Loan.builder()
                .user(userRepository.findById(requestDto.getUserId()).orElseThrow(
                        ()-> new UserNotFoundException("User not found")))
                .principalAmount(requestDto.getPrincipalAmount())
                .interestRate(requestDto.getInterestRate())
                .tenureCounts(requestDto.getTenureCounts())
                .frequency(requestDto.getFrequency())
                .build();
        loanRepository.save(loan);
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public LoanResponseDto viewLoan(UUID loanId) {
        Loan loan  = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public LoanResponseDto approveLoan(UUID loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        loan.setStatus(LoanStatus.APPROVED);
        loanRepository.save(loan);
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public LoanResponseDto rejectLoan(UUID loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        loan.setStatus(LoanStatus.REJECTED);
        loanRepository.save(loan);
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public LoanResponseDto disburseLoan(UUID loanId) {
        //TODO: Link to account balance
        Loan loan = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setDisbursedAt(LocalDateTime.now());
        loanRepository.save(loan);
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public LoanResponseDto cancelLoan(UUID loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        if (loan.getStatus() == LoanStatus.APPROVED && loan.getDisbursedAt() == null)
            loan.setStatus(LoanStatus.CANCELLED);
        loanRepository.save(loan);
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public LoanResponseDto markCompleted(UUID loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        loan.setStatus(LoanStatus.COMPLETED);
        loanRepository.save(loan);
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public LoanResponseDto markDefaulted(UUID loanId) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        loan.setStatus(LoanStatus.DEFAULTED);
        loanRepository.save(loan);
        return modelMapper.map(loan, LoanResponseDto.class);
    }

    @Override
    public BigDecimal calculateInstallment(UUID loanId) {
        Loan loan =  loanRepository.findById(loanId).orElseThrow(
                ()-> new LoanNotFoundException("Loan not found"));
        double principal =  loan.getPrincipalAmount().doubleValue();
        double rate;
        if (loan.getFrequency() == RepaymentFrequency.MONTHLY){
            rate = (loan.getInterestRate()/100)/12;}
        else if (loan.getFrequency() == RepaymentFrequency.YEARLY){
            rate = (loan.getInterestRate()/100);
        }
        else {
            rate = (loan.getInterestRate()/100/52);
        }

        int tenureMonths = loan.getTenureCounts();

        double payment = principal * (rate * Math.pow((1 + rate), tenureMonths)) /
                ((Math.pow(1 + rate,  tenureMonths)) -1);
        BigDecimal installment = BigDecimal.valueOf(payment);
        return installment.setScale(2, BigDecimal.ROUND_HALF_UP);

    }


}
