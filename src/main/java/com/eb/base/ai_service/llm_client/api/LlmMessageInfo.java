package com.eb.base.ai_service.llm_client.api;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LlmMessageInfo {
    private int InputTokens;
    private int OutputTokens;
    private int TotalTokens;
    private String model;
    private double secondsToRun;
    private double tokensPerSecond;

    public LlmMessageInfo() {}
}


