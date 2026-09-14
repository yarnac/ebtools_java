package com.eb.apps.ebchatclient.clients;

import com.eb.apps.ebchatclient.TokenLogger;
import com.eb.apps.ebchatclient.domain.ILlmClient;
import com.eb.apps.ebchatclient.domain.LlmRequestDeprecated;
import com.eb.apps.ebchatclient.domain.LlmResponseDeprecated;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OllamaClientDeprecated implements ILlmClient {

    public static final int TIMEOUT_MINUTES = 120;

    public static String serverURL;

    public static String[] serverURLsToCheck = {
            "macbook-air-von-ekkart",
            "192.168.178.75",
            "127.0.0.1"
    };

    public static String URL;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OllamaClientDeprecated() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private String determineOllamaUrl() throws IOException, InterruptedException {
        for (String url : serverURLsToCheck) {
            if (new CheckNetworkService().checkIsAvailable(url)) {
                return "http://" + url + ":11434/v1/chat/completions";
            }
        }

        return "http://macbook-air-von-ekkart:11434/v1/chat/completions";
    }

    @Override
    public LlmResponseDeprecated sendRequest(LlmRequestDeprecated request)
            throws IOException, InterruptedException {

        boolean hasSystemMessage = request.getMessages() != null
                && !request.getMessages().isEmpty()
                && "system".equalsIgnoreCase(
                        request.getMessages().get(0).getRole());

        String systemText = hasSystemMessage
                ? request.getMessages().get(0).getContent()
                : "";

        OpenAiCompatibleRequestBody body = new OpenAiCompatibleRequestBody(
                request.getModel(),
                request.getMessages(),
                "false",
                "none"
        );

        String jsonBody = objectMapper.writeValueAsString(body);

        if (URL == null) {
            URL = determineOllamaUrl();
        }

        if ("UNDEFINED".equals(URL)) {
            throw new IOException("No Server");
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .timeout(Duration.ofMinutes(TIMEOUT_MINUTES))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString()
        );

        String responseContent = response.body();

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IOException(
                    "Ollama API-Fehler "
                            + response.statusCode()
                            + ": "
                            + responseContent
            );
        }

        OllamaResponseFullDeprecated modelResponse = objectMapper.readValue(
                responseContent,
                OllamaResponseFullDeprecated.class
        );

        if (modelResponse.getChoices() == null
                || modelResponse.getChoices().isEmpty()) {
            throw new IOException("Ollama API lieferte keine Choices.");
        }

        OllamaResponseFullDeprecated.Message message =
                modelResponse.getChoices().get(0).getMessage();

        LlmResponseDeprecated llmResponseDeprecated = new LlmResponseDeprecated();
        llmResponseDeprecated.setModel(modelResponse.getModel());

        if (message != null) {
            llmResponseDeprecated.setAnswer(message.getContent());
        }

        if (modelResponse.getUsageInfo() != null) {
            llmResponseDeprecated.setTotalTokens(
                    modelResponse.getUsageInfo().getTotalTokens()
            );
        }

        TokenLogger.log(request, llmResponseDeprecated);

        return llmResponseDeprecated;
    }


    public boolean isLocal() {
        return true;
    }


    private record OpenAiCompatibleRequestBody(
            String model,
            Object messages,
            String think,
            String reasoning_effort
    ) {
    }

    private record AnthropicRequestBody(
            String model,
            int max_tokens,
            Object messages,
            String system
    ) {
    }
}
