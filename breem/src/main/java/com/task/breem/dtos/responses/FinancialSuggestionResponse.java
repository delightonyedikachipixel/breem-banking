package com.task.breem.dtos.responses;

import com.task.breem.data.models.Enums.SuggestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialSuggestionResponse {
    private UUID id;
    private UUID userId;
    private SuggestionType type;
    private String message;
    private boolean isRead;
    private LocalDateTime createdAt;
}
