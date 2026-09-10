package com.task.breem.services.impl;

import com.task.breem.data.models.Enums.SuggestionType;
import com.task.breem.data.models.FinancialSuggestion;
import com.task.breem.data.models.User;
import com.task.breem.data.repository.FinancialSuggestionRepository;
import com.task.breem.data.repository.UserRepository;
import com.task.breem.dtos.responses.FinancialSuggestionResponse;
import com.task.breem.exceptions.ResourceNotFoundException;
import com.task.breem.services.FinancialSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FinancialSuggestionServiceImpl implements FinancialSuggestionService {

    private final FinancialSuggestionRepository suggestionRepository;
    private final UserRepository userRepository;

    @Override
    public FinancialSuggestionResponse suggest(UUID userId, SuggestionType type, String message) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        FinancialSuggestion suggestion = FinancialSuggestion.builder()
                .user(user)
                .type(type)
                .message(message)
                .isRead(false)
                .build();

        return toResponse(suggestionRepository.save(suggestion));
    }

    @Override
    public List<FinancialSuggestionResponse> getForUser(UUID userId) {
        return suggestionRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FinancialSuggestionResponse> getUnreadForUser(UUID userId) {
        return suggestionRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public FinancialSuggestionResponse markAsRead(UUID suggestionId) {
        FinancialSuggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new ResourceNotFoundException("Suggestion not found: " + suggestionId));
        suggestion.setRead(true);
        return toResponse(suggestionRepository.save(suggestion));
    }

    private FinancialSuggestionResponse toResponse(FinancialSuggestion s) {
        return FinancialSuggestionResponse.builder()
                .id(s.getId())
                .userId(s.getUser().getId())
                .type(s.getType())
                .message(s.getMessage())
                .isRead(s.isRead())
                .createdAt(s.getCreatedAt())
                .build();
    }
}
