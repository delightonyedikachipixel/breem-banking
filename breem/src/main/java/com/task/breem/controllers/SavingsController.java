package com.task.breem.controllers;

import com.task.breem.dtos.requests.RoundUpSavingsRequest;
import com.task.breem.dtos.responses.ApiResponse;
import com.task.breem.dtos.responses.RoundUpSavingsResponse;
import com.task.breem.services.SavingsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/savings")
@RequiredArgsConstructor
public class SavingsController {

    private final SavingsService savingsService;


    @PostMapping("/round-up")
    public ResponseEntity<ApiResponse<RoundUpSavingsResponse>> roundUpSave(@Valid @RequestBody RoundUpSavingsRequest request) {
        RoundUpSavingsResponse response = savingsService.roundUpSave(request);
        String message = response.isSaved()
                ? "Saved NGN " + response.getAmountSaved()
                : "Balance is already a round figure - nothing to save";
        return ResponseEntity.ok(ApiResponse.success(message, response));
    }
}
