package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.llm_client.api.LlmResponse;
import com.eb.ai_service.llm_client.infrastructure.ollama.OllamaClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.http.HttpClient;

class OllamaClientTest extends AbstrLlmClientTest {

    @Test
    void test() throws IOException, InterruptedException {

        OllamaClient client = new OllamaClient(HttpClient.newHttpClient()) ;

        LlmResponse response = client.sendRequest(createTestRequest("qwen3:8b"));

        String answer = response.getAnswer();
        assert(answer.contains("machen") || answer.contains("tun") );
    }


    private static String getAnswer(LlmResponse response) {
        return response.getAnswer();
    }

}