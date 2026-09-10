package com.task.breem.services;

import com.task.breem.data.models.Enums.SuggestionType;
import com.task.breem.dtos.responses.FinancialSuggestionResponse;

import java.util.List;
import java.util.UUID;

public interface FinancialSuggestionService {
    FinancialSuggestionResponse suggest(UUID userId, SuggestionType type, String message);
    List<FinancialSuggestionResponse> getForUser(UUID userId);
    List<FinancialSuggestionResponse> getUnreadForUser(UUID userId);
    FinancialSuggestionResponse markAsRead(UUID suggestionId);
}
