package com.task.breem.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaystackWebhookRequest {
    @NotBlank
    private String paystackReference;

    @NotBlank
    private String status;

    private String authorizationCode;
}
