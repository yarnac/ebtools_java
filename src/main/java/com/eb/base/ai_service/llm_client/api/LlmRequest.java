package com.eb.base.ai_service.llm_client.api;

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

    public  static LlmRequestBuilderSystemOrUserMsg builder()
    {
        return LlmRequestBuilder.create();
    }


}
