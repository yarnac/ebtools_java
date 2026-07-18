package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.llm_client.api.LlmResponse;
import com.eb.ai_service.llm_client.infrastructure.openai.OpenAiClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

class OpenAiClientTest extends AbstrLlmClientTest {

    @Test
    void test() throws IOException, InterruptedException {
        OpenAiClient client = new OpenAiClient(HttpClient.newHttpClient(), LlmClientFactory.getOpenAiKey()) ;

        LlmResponse response = client.sendRequest(createTestRequest("gpt-5.4-nano"));

        String answer = response.getAnswer();
        assert(answer.contains("machen") || answer.contains("tun") );
    }

}