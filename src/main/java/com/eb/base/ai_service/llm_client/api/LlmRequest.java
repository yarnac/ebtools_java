package com.eb.base.ai_service.llm_client.api;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class LlmRequest {
    private String model;
    List<LlmMessage> messages;
    List<Parameter> parameters;

    public LlmRequest(String model, List<LlmMessage> messages) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(messages);
        this.model = model;
        this.messages = messages;
        this.parameters = new ArrayList<>();
    }

    public LlmRequest(String model, List<LlmMessage> messages, List<Parameter> parameters) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(messages);
        Objects.requireNonNull(parameters);
        this.model = model;
        this.messages = messages;
        this.parameters = parameters;
    }

    public  static LlmRequestBuilderSystemOrUserMsg builder()
    {
        return LlmRequestBuilder.create();
    }


    public boolean hasParameter(String maxTokens) {
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals(maxTokens)) {
                return true;
            }
        }
        return false;
    }
}
