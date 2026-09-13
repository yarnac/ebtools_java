package com.eb.chatclient.app;

import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmResponse;
import com.eb.ai_service.llm_client.infrastructure.ILlmClient;
import com.eb.ai_service.llm_client.infrastructure.LlmClientFactory;

import java.io.IOException;

public class LlmRequestService {

    public static LlmResponse sendRequest(LlmRequest llmRequest) {
        LlmClientFactory factory = new LlmClientFactory();
        ILlmClient client = factory.getLlmClient(llmRequest);
        try {
            LlmResponse response = client.sendRequest(llmRequest);
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
