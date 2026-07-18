package com.eb.ai_service.llm_client.api;

import lombok.Getter;
import lombok.Setter;

@Setter

@Getter
public class LlmResponse {
    private int status;
    private String answer;
    private String model;
    private String reasoning;
    private String error;
    private int inputTokens;
    private int outputTokens;
    private int totalTokens;

    public void calcTokens()
    {
        totalTokens = inputTokens + outputTokens;
    }
}