package com.eb.chatclient.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LlmResponseDeprecated {
    private String model;
    private String message;
    private String output;
    private String content;
    private String answer;
    private int inputTokens;
    private int outputTokens;
    private int totalTokens;

    public void calcTokens() {
    }
}
