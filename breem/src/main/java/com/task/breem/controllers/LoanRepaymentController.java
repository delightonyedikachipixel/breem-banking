package com.task.breem.controllers;

import com.task.breem.config.RepaymentArgs;
import com.task.breem.dtos.responses.LoanRepaymentResponse;
import com.task.breem.services.LoanRepaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/repayment")
public class LoanRepaymentController {

    private final LoanRepaymentService loanRepaymentService;

    @PostMapping
    @Operation(summary = "making a payment from the loan")
    public ResponseEntity<LoanRepaymentResponse> makePayments (@RequestBody RepaymentArgs repaymentArgs) {
        return ResponseEntity.ok(loanRepaymentService.repayLoan(repaymentArgs));
    }

    @GetMapping
    @Operation(summary = "get all payment records")
    public ResponseEntity<List<LoanRepaymentResponse>> getAllPaymentRecords (@RequestParam UUID loanId) {
        return ResponseEntity.ok(loanRepaymentService.getAllPayments(loanId));
    }
}
