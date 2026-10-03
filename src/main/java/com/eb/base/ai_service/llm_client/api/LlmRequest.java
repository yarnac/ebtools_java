package com.eb.base.ai_service.llm_client.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class LlmRequest {
    @JsonProperty("model")
    private String model;
    List<LlmMessage> messages;
    List<Parameter> parameters;
    private LlmResponse lastResponse;

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

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    @JsonProperty
    public Parameter getParameter(String name) {
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals(name)) {
                return parameter;
            }
        }
        return null;
    }

    public boolean hasParameter(String maxTokens) {
        return getParameter(maxTokens) != null;
    }

    public void addUserMsg(String inputStringWithFiles) {
        LlmMessage msg = new LlmMessage("user", inputStringWithFiles);
        getMessages().add(msg);
    }

    public void addResponse(LlmResponse response) {
        lastResponse = response;
        getMessages().add(new LlmMessage("assistant", response.getAnswer()));
    }
}
