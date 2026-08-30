package com.eb.ai_service.llm_client.infrastructure.ollama;

import com.eb.ai_service.llm_client.api.LlmMessage;
import com.eb.ai_service.llm_client.infrastructure.ILlmClient;
import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OllamaClient implements ILlmClient {

    @Override
    public LlmResponse sendRequest(LlmRequest llmRequest) throws IOException, InterruptedException {

        List<LlmMessage> messages = llmRequest.getMessages();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "qwen3:8B");
        body.put("messages", messages);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://macbook-air-von-ekkart:11434/v1/chat/completions"))
                //.uri(URI.create("http://127.0.0.1:11434/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + "")
                .timeout(Duration.ofMinutes(30))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        OllamaResponse ollamaResponse =
                mapper.readValue(response.body(), OllamaResponse.class);

        LlmResponse llrResponse = new LlmResponse();
        OllamaResponse.Message answerMessage = ollamaResponse.getChoices().get(0).getMessage();
        llrResponse.setAnswer(answerMessage.getContent());
        llrResponse.setModel(ollamaResponse.getModel());
        llrResponse.setInputTokens(0);
        llrResponse.setOutputTokens(0);
        llrResponse.setTotalTokens(ollamaResponse.getUsage().getTotalTokens());
        return llrResponse;
    }

    private final HttpClient httpClient;
    public OllamaClient(HttpClient newHttpClient) {
        httpClient = newHttpClient;
    }
}
