package com.task.breem.services;

import com.task.breem.dtos.requests.RoundUpSavingsRequest;
import com.task.breem.dtos.responses.RoundUpSavingsResponse;

public interface SavingsService {
    RoundUpSavingsResponse roundUpSave(RoundUpSavingsRequest request);
}
