package com.eb.base.ai_service.llm_client.api;

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
    private LlmRequest request;
    private double secondsToRun;
    private double tokensPerSecond;

    public void calcTokens()
    {
        if (totalTokens == 0)
            totalTokens = inputTokens + outputTokens;
        if (secondsToRun > 0)
            tokensPerSecond = (double) totalTokens / (double) secondsToRun;
    }
}