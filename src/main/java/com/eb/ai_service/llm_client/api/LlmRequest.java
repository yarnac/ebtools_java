package com.eb.ai_service.llm_client.api;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LlmRequest {
    private String model;
    List<LlmMessage> messages;

    public LlmRequest(String model, List<LlmMessage> messages) {
        this.model = model;
        this.messages = messages;
    }

    public  static LlmRequestBuilderSystem builder()
    {
        return LlmRequestBuilder.create();
    }


}
