package com.task.breem.data.repository;

import com.task.breem.data.models.BudgetEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BudgetEntryRepository extends JpaRepository<BudgetEntry, UUID> {
    List<BudgetEntry> findByBudgetIdOrderByEntryDateDesc(UUID budgetId);

    @Query("select coalesce(sum(be.amount), 0) from BudgetEntry be " +
            "where be.budget.id = :budgetId and be.entryDate between :start and :end")
    BigDecimal sumAmountByBudgetIdBetween(@Param("budgetId") UUID budgetId,
                                           @Param("start") LocalDate start,
                                           @Param("end") LocalDate end);
}
