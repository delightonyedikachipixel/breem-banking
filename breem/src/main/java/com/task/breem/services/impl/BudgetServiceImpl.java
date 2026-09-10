package com.task.breem.services.impl;

import com.task.breem.data.models.Budget;
import com.task.breem.data.models.BudgetEntry;
import com.task.breem.data.models.Enums.NotificationType;
import com.task.breem.data.models.Enums.SuggestionType;
import com.task.breem.data.models.Transaction;
import com.task.breem.data.models.User;
import com.task.breem.data.repository.BudgetEntryRepository;
import com.task.breem.data.repository.BudgetRepository;
import com.task.breem.data.repository.UserRepository;
import com.task.breem.dtos.requests.CreateBudgetRequest;
import com.task.breem.dtos.requests.UpdateBudgetRequest;
import com.task.breem.dtos.responses.BudgetEntryResponse;
import com.task.breem.dtos.responses.BudgetResponse;
import com.task.breem.exceptions.ResourceNotFoundException;
import com.task.breem.services.BudgetService;
import com.task.breem.services.FinancialSuggestionService;
import com.task.breem.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BudgetServiceImpl implements BudgetService {

    private static final BigDecimal WARNING_THRESHOLD = new BigDecimal("0.80");

    private final BudgetRepository budgetRepository;
    private final BudgetEntryRepository budgetEntryRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final FinancialSuggestionService financialSuggestionService;

    @Override
    public BudgetResponse createBudget(CreateBudgetRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUserId()));

        Budget budget = Budget.builder()
                .user(user)
                .category(request.getCategory())
                .limitAmount(request.getLimitAmount())
                .period(request.getPeriod())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        return toResponse(budgetRepository.save(budget));
    }

    @Override
    public BudgetResponse getBudget(UUID budgetId) {
        return toResponse(getBudgetEntity(budgetId));
    }

    @Override
    public List<BudgetResponse> getBudgetsForUser(UUID userId) {
        return budgetRepository.findByUserId(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public BudgetResponse updateBudget(UUID budgetId, UpdateBudgetRequest request) {
        Budget budget = getBudgetEntity(budgetId);
        if (request.getLimitAmount() != null) {
            budget.setLimitAmount(request.getLimitAmount());
        }
        if (request.getStartDate() != null) {
            budget.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            budget.setEndDate(request.getEndDate());
        }
        return toResponse(budgetRepository.save(budget));
    }

    @Override
    public void deleteBudget(UUID budgetId) {
        Budget budget = getBudgetEntity(budgetId);
        budgetRepository.delete(budget);
    }

    @Override
    public List<BudgetEntryResponse> getEntries(UUID budgetId) {
        return budgetEntryRepository.findByBudgetIdOrderByEntryDateDesc(budgetId)
                .stream().map(this::toEntryResponse).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void recordAgainstBudgetIfMatched(Transaction transaction) {
        if (transaction.getCategory() == null || transaction.getCategory().isBlank()) {
            return;
        }

        UUID userId = transaction.getAccount().getUser().getId();
        Optional<Budget> maybeBudget = budgetRepository
                .findByUserIdAndCategoryIgnoreCase(userId, transaction.getCategory());

        if (maybeBudget.isEmpty()) {
            return;
        }

        Budget budget = maybeBudget.get();

        BudgetEntry entry = BudgetEntry.builder()
                .budget(budget)
                .transaction(transaction)
                .amount(transaction.getAmount())
                .entryDate(LocalDate.now())
                .build();
        budgetEntryRepository.save(entry);

        LocalDate start = budget.getStartDate() != null ? budget.getStartDate() : LocalDate.MIN;
        LocalDate end = budget.getEndDate() != null ? budget.getEndDate() : LocalDate.MAX;
        BigDecimal spent = budgetEntryRepository.sumAmountByBudgetIdBetween(budget.getId(), start, end);

        if (spent.compareTo(budget.getLimitAmount()) >= 0) {
            String message = "You've exceeded your " + budget.getCategory() + " budget of NGN "
                    + budget.getLimitAmount() + " (spent NGN " + spent + ").";
            notificationService.notify(userId, NotificationType.BUDGET_ALERT, "Budget exceeded", message);
            financialSuggestionService.suggest(userId, SuggestionType.SPENDING_ALERT, message);
        } else if (spent.compareTo(budget.getLimitAmount().multiply(WARNING_THRESHOLD)) >= 0) {
            String message = "You've used over 80% of your " + budget.getCategory() + " budget (NGN "
                    + spent + " of NGN " + budget.getLimitAmount() + ").";
            notificationService.notify(userId, NotificationType.BUDGET_ALERT, "Approaching budget limit", message);
            financialSuggestionService.suggest(userId, SuggestionType.BUDGET_TIP, message);
        }
    }

    private Budget getBudgetEntity(UUID budgetId) {
        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> new ResourceNotFoundException("Budget not found: " + budgetId));
    }

    private BudgetResponse toResponse(Budget budget) {
        LocalDate start = budget.getStartDate() != null ? budget.getStartDate() : LocalDate.MIN;
        LocalDate end = budget.getEndDate() != null ? budget.getEndDate() : LocalDate.MAX;
        BigDecimal spent = budgetEntryRepository.sumAmountByBudgetIdBetween(budget.getId(), start, end);

        return BudgetResponse.builder()
                .id(budget.getId())
                .userId(budget.getUser().getId())
                .category(budget.getCategory())
                .limitAmount(budget.getLimitAmount())
                .spentAmount(spent)
                .remainingAmount(budget.getLimitAmount().subtract(spent))
                .period(budget.getPeriod())
                .startDate(budget.getStartDate())
                .endDate(budget.getEndDate())
                .createdAt(budget.getCreatedAt())
                .build();
    }

    private BudgetEntryResponse toEntryResponse(BudgetEntry entry) {
        return BudgetEntryResponse.builder()
                .id(entry.getId())
                .budgetId(entry.getBudget().getId())
                .transactionId(entry.getTransaction().getId())
                .amount(entry.getAmount())
                .entryDate(entry.getEntryDate())
                .build();
    }
}
