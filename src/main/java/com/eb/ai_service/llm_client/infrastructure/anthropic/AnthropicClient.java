package com.eb.ai_service.llm_client.infrastructure.anthropic;

import com.eb.ai_service.llm_client.api.LlmMessage;
import com.eb.ai_service.llm_client.infrastructure.ILlmClient;
import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmResponse;
import com.eb.ai_service.llm_client.infrastructure.TokenLogger;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class AnthropicClient implements ILlmClient {

    private final ObjectMapper mapper;
    private final HttpClient httpClient;
    private final String apiKey;

    public AnthropicClient(HttpClient newHttpClient, String newApiKey) {
        httpClient = newHttpClient;
        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        apiKey = newApiKey;
    }

    @Override
    public LlmResponse sendRequest(LlmRequest llmRequest) throws IOException, InterruptedException {

        List<LlmMessage> messages = llmRequest.getMessages();
        List<LlmMessage> dialogMessages = getDialogMessages(messages);

        Map<String, Object> body = new HashMap<>();
        body.put("model", llmRequest.getModel());
        body.put("max_tokens", 20000);
        body.put("messages", dialogMessages);
        body.put("system", getSystemText(messages));


        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.anthropic.com/v1/messages"))
                .header("Content-Type", "application/json")
                .header("x-api-key", apiKey)
                .header("anthropic-version", "2023-06-01")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String respBody = response.body();
        AnthropicResponse anthropicAiResponse =
                mapper.readValue(respBody, AnthropicResponse.class);

        String answer = anthropicAiResponse.getContent().stream()
                .filter(c -> "text".equals(c.getType()))
                .map(AnthropicResponse.Content::getText)
                .findFirst()
                .orElse("");

        LlmResponse llrResponse = new LlmResponse();
        llrResponse.setAnswer(answer);
        llrResponse.setModel(anthropicAiResponse.getModel());
        llrResponse.setInputTokens(anthropicAiResponse.getUsage().getInputTokens());
        llrResponse.setOutputTokens(anthropicAiResponse.getUsage().getOutputTokens());
        llrResponse.calcTokens();
        TokenLogger.log(llmRequest, llrResponse);
        return llrResponse;
    }

    private List<LlmMessage> getDialogMessages(List<LlmMessage> messages) {
        return messages.stream().filter(x->!isSystemMessage(x)).collect(Collectors.toUnmodifiableList());
    }

    private String getSystemText(List<LlmMessage> messages) {
        List<LlmMessage> systemMessages = messages.stream().filter(x->isSystemMessage(x)).collect(Collectors.toUnmodifiableList());
        if (systemMessages.isEmpty())
            return "";

        if (systemMessages.size()>1)
            throw(new IllegalArgumentException("Mehrere Systemnachrichten"));

        return systemMessages.get(0).getMessage();
    }

    private boolean isSystemMessage(LlmMessage x) {
        return x.getRole().contains("system");
    }
}
