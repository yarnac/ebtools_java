package com.eb.base.ai_service.llmreqstore;

import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LlmRequestStoreItem {
    private long requestId;
    private LlmRequest llmRequest;
    private String title;
    private String description;

    public LlmRequestStoreItem() {

    };
}
