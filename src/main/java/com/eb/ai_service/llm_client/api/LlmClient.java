package com.eb.ai_service.llm_client.api;

import com.eb.ai_service.llm_client.infrastructure.LlmClientFactory;
import com.eb.ai_service.llm_client.infrastructure.ILlmClient;

public class LlmClient implements ILlmClient {
    LlmClientFactory clientFactory;

    public LlmClient() {
        this.clientFactory = new LlmClientFactory();
    }

    @Override
    public LlmResponse sendRequest(LlmRequest llmRequest) {
        ILlmClient client = clientFactory.getLlmClient(llmRequest);
        try {
            return client.sendRequest(llmRequest);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
