package com.eb.ai_service.llm_client.api;

import com.eb.ai_service.llm_client.infrastructure.AbstrLlmClientTest;
import com.eb.ai_service.llm_client.infrastructure.LlmClientFactory;
import com.eb.ai_service.llm_client.infrastructure.ILlmClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class LlmClientFactoryTest {

    LlmClientFactory llmCientFactory = new LlmClientFactory();

    @Test
    void testOllama() throws IOException, InterruptedException {

        String model = "qwen3:8b";

        LlmRequest request = AbstrLlmClientTest.createTestRequest(model);
        ILlmClient client = llmCientFactory.getLlmClient(request);

        LlmResponse response = client.sendRequest(request);
        String answer = response.getAnswer();
        assert(answer.contains("machen") || answer.contains("tun") );
    }

    @Test
    void testOpenai() throws IOException, InterruptedException {

        String model = "gpt-5.4-nano";

        LlmRequest request = AbstrLlmClientTest.createTestRequest(model);
        ILlmClient client = llmCientFactory.getLlmClient(request);

        LlmResponse response = client.sendRequest(request);
        String answer = response.getAnswer();
        assert(answer.contains("machen") || answer.contains("tun") );
    }

    @Test
    void testAnthropic() throws IOException, InterruptedException {

        String model = "claude-haiku-4-5-20251001";

        LlmRequest request = AbstrLlmClientTest.createTestRequest(model);
        ILlmClient client = llmCientFactory.getLlmClient(request);

        LlmResponse response = client.sendRequest(request);
        String answer = response.getAnswer();
        assert(answer.contains("machen") || answer.contains("tun") );
    }


}