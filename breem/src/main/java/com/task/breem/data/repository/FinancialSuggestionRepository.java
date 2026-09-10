package com.task.breem.data.repository;

import com.task.breem.data.models.FinancialSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FinancialSuggestionRepository extends JpaRepository<FinancialSuggestion, UUID> {
    List<FinancialSuggestion> findByUserIdOrderByCreatedAtDesc(UUID userId);
    List<FinancialSuggestion> findByUserIdAndIsReadFalseOrderByCreatedAtDesc(UUID userId);
}
