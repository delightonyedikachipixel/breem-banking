package com.task.breem.controllers;

import com.task.breem.dtos.requests.CreateBudgetRequest;
import com.task.breem.dtos.requests.UpdateBudgetRequest;
import com.task.breem.dtos.responses.ApiResponse;
import com.task.breem.dtos.responses.BudgetEntryResponse;
import com.task.breem.dtos.responses.BudgetResponse;
import com.task.breem.services.BudgetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/budgets")
@RequiredArgsConstructor
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    public ResponseEntity<ApiResponse<BudgetResponse>> createBudget(@Valid @RequestBody CreateBudgetRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Budget created", budgetService.createBudget(request)));
    }

    @GetMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<BudgetResponse>> getBudget(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(ApiResponse.success("Budget retrieved", budgetService.getBudget(budgetId)));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getBudgetsForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Budgets retrieved", budgetService.getBudgetsForUser(userId)));
    }

    @PatchMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<BudgetResponse>> updateBudget(@PathVariable UUID budgetId,
                                                                      @RequestBody UpdateBudgetRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Budget updated", budgetService.updateBudget(budgetId, request)));
    }

    @DeleteMapping("/{budgetId}")
    public ResponseEntity<ApiResponse<Void>> deleteBudget(@PathVariable UUID budgetId) {
        budgetService.deleteBudget(budgetId);
        return ResponseEntity.ok(ApiResponse.success("Budget deleted", null));
    }

    @GetMapping("/{budgetId}/entries")
    public ResponseEntity<ApiResponse<List<BudgetEntryResponse>>> getEntries(@PathVariable UUID budgetId) {
        return ResponseEntity.ok(ApiResponse.success("Budget entries retrieved", budgetService.getEntries(budgetId)));
    }
}
