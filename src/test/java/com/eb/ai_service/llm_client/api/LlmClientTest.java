package com.eb.ai_service.llm_client.api;

import com.eb.ai_service.llm_client.infrastructure.AbstrLlmClientTest;
import com.eb.base.ai_service.llm_client.api.LlmClient;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LlmClientTest {
    LlmClient service = new LlmClient();

    @Test
    void testOllama() {
        LlmRequest request = AbstrLlmClientTest.createTestRequest("qwen3:8b");
        LlmResponse response = service.sendRequest(request);
        assertNotNull(response);
        assert response.getAnswer().contains("machen") || response.getAnswer().contains("tun");
    }

    @Test
    void testOpenAi() {
        LlmRequest request = AbstrLlmClientTest.createTestRequest("gpt-5.4-nano");
        LlmResponse response = service.sendRequest(request);
        assertNotNull(response);
        String answer = response.getAnswer();
        assert answer.contains("machen") || answer.contains("tun");
    }

}