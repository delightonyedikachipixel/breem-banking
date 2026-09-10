package com.task.breem.services;

import com.task.breem.dtos.requests.CreateBudgetRequest;
import com.task.breem.dtos.requests.UpdateBudgetRequest;
import com.task.breem.dtos.responses.BudgetEntryResponse;
import com.task.breem.dtos.responses.BudgetResponse;
import com.task.breem.data.models.Transaction;

import java.util.List;
import java.util.UUID;

public interface BudgetService {
    BudgetResponse createBudget(CreateBudgetRequest request);
    BudgetResponse getBudget(UUID budgetId);
    List<BudgetResponse> getBudgetsForUser(UUID userId);
    BudgetResponse updateBudget(UUID budgetId, UpdateBudgetRequest request);
    void deleteBudget(UUID budgetId);
    List<BudgetEntryResponse> getEntries(UUID budgetId);
    void recordAgainstBudgetIfMatched(Transaction transaction);
}
