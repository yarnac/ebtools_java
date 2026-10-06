package com.eb.base.ai_service.llm_client.infrastructure.ollama;

import com.eb.base.ai_service.llm_client.api.*;
import com.eb.base.ai_service.llm_client.infrastructure.ILlmClient;
import com.eb.base.ai_service.llm_client.infrastructure.TokenLogger;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.LlmRequestJsonMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;


public class OllamaClient implements ILlmClient {

    public static final String HOST_MACSTUDIO = "macstudio";
    public static final String HOST_MACSTUDIO2 = "192.168.178.105";
    public static final String HOST_MACBOOK = "macbookeb";
    public static final String HOST_MACBOOKAIR = "macbook-air-von-ekkart";
    public static final String HOST_XT13 = "xt13";
    public static final String[] HOSTS = new String[]{
            HOST_MACSTUDIO,
            HOST_MACSTUDIO2,
            HOST_MACBOOK,
            // HOST_MACBOOKAIR,
            // HOST_XT13
    };


    public static final String QWEN3_8 = "qwen3:8b";
    public static final String QWEN3_14 = "qwen3:14b";
    public static final String QWEN35_4 = "qwen3.5:4b";
    public static final String QWEN35_9 = "qwen3.5:9b";
    public static final String QWEN25CODER_7 = "qwen2.5-coder:7b";
    public static final String QWEN25CODER_14 = "qwen2.5-coder:14b";

    public static final String[] MODELLE = new String[]{
            //QWEN25CODER_7,
            //QWEN25CODER_14,
            //QWEN3_8,
            //QWEN3_14,
            QWEN35_4,
            QWEN35_9,
    };



    public static String HOST;

    @Override
    public LlmResponse sendRequest(LlmRequest llmRequest) throws IOException, InterruptedException {

        if (HOST==null)
        {
            HOST = determineHost();
        }

        LlmRequestJsonMapper requestJsonMapper = new LlmRequestJsonMapper(new ContentPartMapperOllama());
        String json = requestJsonMapper.getJsonString(llmRequest);


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://%s:11434/v1/chat/completions".formatted(HOST)))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + "")
                .timeout(Duration.ofMinutes(30))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();


        long startTime = System.nanoTime();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1000000000.0;

        ObjectMapper jsonMapper = new ObjectMapper();
        String respondBody = response.body();
        OllamaResponse ollamaResponse =
                jsonMapper.readValue(respondBody, OllamaResponse.class);

        LlmResponse llmResponse = new LlmResponse();
        OllamaResponse.Message answerMessage = ollamaResponse.getChoices().get(0).getMessage();
        llmResponse.setAnswer(answerMessage.getContent());
        llmResponse.setModel(ollamaResponse.getModel());
        llmResponse.setInputTokens(0);
        llmResponse.setOutputTokens(0);
        llmResponse.setTotalTokens(ollamaResponse.getUsage().getTotalTokens());
        llmResponse.setRequest(llmRequest);
        llmResponse.setSecondsToRun(elapsedSeconds);
        llmResponse.calcTokens();

        TokenLogger.log(llmRequest, llmResponse);

        return llmResponse;
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

    public List<String> getAvailableModels() {
        try {
            if (HOST==null)
                HOST = determineHost();

            HttpClient client = HttpClient.newBuilder()
                    .connectTimeout(Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://%s:%d/api/tags".formatted(HOST, 11434)))
                    .timeout(Duration.ofSeconds(30))
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode rootNode = objectMapper.readTree(response.body());
                JsonNode modelsNode = rootNode.get("models");

                List<String> modelNames = new ArrayList<>();
                if (modelsNode != null && modelsNode.isArray()) {
                    for (JsonNode modelNode : modelsNode) {
                        JsonNode nameNode = modelNode.get("name");
                        if (nameNode != null && !nameNode.asText().isEmpty()) {
                            modelNames.add(nameNode.asText());
                        }
                    }
                }

                return modelNames;
            } else {
                System.err.println("Failed to fetch models. Status code: " + response.statusCode());
                return new ArrayList<>();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error while fetching available models: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
