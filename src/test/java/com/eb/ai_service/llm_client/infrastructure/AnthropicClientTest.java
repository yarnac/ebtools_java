package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.llm_client.api.LlmResponse;
import com.eb.ai_service.llm_client.infrastructure.anthropic.AnthropicClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

class AnthropicClientTest extends AbstrLlmClientTest {
    @Test
    void test() throws IOException, InterruptedException {

        AnthropicClient client = new AnthropicClient(HttpClient.newHttpClient(), LlmClientFactory.getAnthropicApiKey()) ;

        LlmResponse response = client.sendRequest(createTestRequest("claude-haiku-4-5-20251001"));

        String answer = response.getAnswer();
        assert(answer.contains("machen") || answer.contains("tun") );
    }
}