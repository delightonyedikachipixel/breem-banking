package com.task.breem.controllers;

import com.task.breem.dtos.responses.ApiResponse;
import com.task.breem.dtos.responses.FinancialSuggestionResponse;
import com.task.breem.services.FinancialSuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/financial-suggestions")
@RequiredArgsConstructor
public class FinancialSuggestionController {

    private final FinancialSuggestionService financialSuggestionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<FinancialSuggestionResponse>>> getForUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Suggestions retrieved", financialSuggestionService.getForUser(userId)));
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<ApiResponse<List<FinancialSuggestionResponse>>> getUnread(@PathVariable UUID userId) {
        return ResponseEntity.ok(ApiResponse.success("Unread suggestions retrieved", financialSuggestionService.getUnreadForUser(userId)));
    }

    @PatchMapping("/{suggestionId}/read")
    public ResponseEntity<ApiResponse<FinancialSuggestionResponse>> markAsRead(@PathVariable UUID suggestionId) {
        return ResponseEntity.ok(ApiResponse.success("Suggestion marked as read", financialSuggestionService.markAsRead(suggestionId)));
    }
}
