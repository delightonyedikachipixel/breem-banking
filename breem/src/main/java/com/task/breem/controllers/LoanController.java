package com.task.breem.controllers;

import com.task.breem.dtos.requests.LoanRequestDto;
import com.task.breem.dtos.responses.LoanResponseDto;
import com.task.breem.services.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/loan")
public class LoanController {

    private final LoanService loanService;

    @PostMapping
    @Operation(summary = "requesting for a loan, the customer fills out the request form")
    public ResponseEntity<LoanResponseDto> requestLoan(@RequestBody LoanRequestDto loanRequestDto) {
        return ResponseEntity.ok(loanService.applyForLoan(loanRequestDto));
    }

    @GetMapping("/{loanId}")
    @Operation(summary = "view a particular loan")
    public ResponseEntity<LoanResponseDto> viewLoan(@PathVariable UUID loanId) {
        return ResponseEntity.ok(loanService.viewLoan(loanId));
    }

    @PatchMapping("/approval")
    @Operation(summary = "approve a loan filled out")
    public ResponseEntity<LoanResponseDto> approveLoan(@RequestParam UUID loanId) {
        return ResponseEntity.ok(loanService.approveLoan(loanId));
    }

    @PatchMapping("/decision")
    @Operation(summary = "reject a loan")
    public ResponseEntity<LoanResponseDto> rejectLoan(@RequestParam UUID loanId) {
        return ResponseEntity.ok(loanService.rejectLoan(loanId));
    }

    @PatchMapping("/disburse")
    @Operation(summary = "disburse loan")
    public ResponseEntity<LoanResponseDto> disburseLoan(@RequestParam UUID loanId) {
        return ResponseEntity.ok(loanService.disburseLoan(loanId));
    }

    @PatchMapping("/cancel")
    @Operation(summary = "cancel an already approved loan")
    public ResponseEntity<LoanResponseDto> cancelLoan(@RequestParam UUID loanId) {
        return ResponseEntity.ok(loanService.cancelLoan(loanId));
    }

    @PatchMapping("/mark")
    @Operation(summary = "mark a loan as completed")
    public ResponseEntity<LoanResponseDto> markLoanAsCompleted(@RequestParam UUID loanId) {
        return ResponseEntity.ok(loanService.markCompleted(loanId));
    }

    @PatchMapping("/defaulted")
    @Operation(summary = "mark as defaulted")
    public ResponseEntity<LoanResponseDto> markLoanAsDefaulted(@RequestParam UUID loanId) {
        return ResponseEntity.ok(loanService.markDefaulted(loanId));
    }
}
