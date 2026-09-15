package com.eb.base.ai_service.llm_client.infrastructure.ollama;

import com.eb.base.ai_service.llm_client.api.CheckNetworkService;
import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.infrastructure.ILlmClient;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
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
import java.util.concurrent.CompletableFuture;


public class OllamaClient implements ILlmClient {

    public static String[] HOSTS = new String[]{"macbook-air-von-ekkart2", "xt13", "127.0.0.1", "conroy"};
    public static String HOST;

    @Override
    public LlmResponse sendRequest(LlmRequest llmRequest) throws IOException, InterruptedException {

        if (HOST==null)
        {
            HOST = determineHost();
        }
        List<LlmMessage> messages = llmRequest.getMessages();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "qwen3:8B");
        body.put("messages", messages);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://%s:11434/v1/chat/completions".formatted(HOST)))
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

    private String determineHost() {
        if (HOST != null)
            return HOST;
        for (String host : OllamaClient.HOSTS)
        {
            if (HOST != null)
                return HOST;


            CompletableFuture<Boolean> isAvailable2 = CheckNetworkService.isOllamaAvailableAsync(host,11434);
            isAvailable2.thenAccept(isAvailable3 -> {
                if (isAvailable3) {
                    HOST = host;
                }
                System.out.println("%s isAvailable: ".formatted(host) + isAvailable3);
            });

        }
        return HOST;
    }

    private final HttpClient httpClient;
    public OllamaClient(HttpClient newHttpClient) {
        httpClient = newHttpClient;
    }
}
