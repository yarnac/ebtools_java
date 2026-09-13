package com.eb.chatclient.clients;

import com.eb.chatclient.PitMessageBox;
import com.eb.chatclient.TokenLogger;
import com.eb.chatclient.domain.ILlmClient;
import com.eb.chatclient.domain.LlmRequestDeprecated;
import com.eb.chatclient.domain.LlmResponseDeprecated;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class OpenAiClientDeprecated implements ILlmClient {

    private static final String API_URL = "https://api.openai.com/v1/responses";

    private final String apiKey;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenAiClientDeprecated(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30))
                .build();

        this.objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public LlmResponseDeprecated sendRequest(LlmRequestDeprecated request) throws IOException, InterruptedException {
        OpenAiRequestBody body = new OpenAiRequestBody(
                request.getModel(),
                request.getMessages(),
                false
        );

        String jsonBody;
        try {
            jsonBody = objectMapper.writeValueAsString(body);
        } catch (JsonProcessingException e) {
            throw new IOException("Fehler beim Serialisieren der OpenAI-Anfrage.", e);
        }

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .timeout(Duration.ofMinutes(30))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(
                httpRequest,
                HttpResponse.BodyHandlers.ofString()
        );

        String responseContent = response.body();

        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            String message = String.format(
                    "OpenAi API-Fehler %d: %s",
                    response.statusCode(),
                    responseContent
            );

            PitMessageBox.show(message);
            throw new IOException(message);
        }

        OpenAiResponseFullDeprecated modelResponse;
        try {
            modelResponse = objectMapper.readValue(
                    responseContent,
                    OpenAiResponseFullDeprecated.class
            );
        } catch (JsonProcessingException e) {
            String message = "OpenAi API-Antwort konnte nicht deserialisiert werden: "
                    + e.getMessage();
            PitMessageBox.show(message);
            throw new IOException(message, e);
        }

        LlmResponseDeprecated llmResponseDeprecated = new LlmResponseDeprecated();
        llmResponseDeprecated.setModel(modelResponse.getModel());

        if (modelResponse.getOutput() != null) {
            for (OpenAiResponseFullDeprecated.OutputMessage currentOutput : modelResponse.getOutput()) {
                if ("message".equalsIgnoreCase(currentOutput.getType())) {
                    if (currentOutput.getContent() != null
                            && !currentOutput.getContent().isEmpty()
                            && currentOutput.getContent().get(0) != null) {

                        llmResponseDeprecated.setAnswer(
                                currentOutput.getContent().get(0).getText()
                        );
                    }
                    break;
                }
            }
        }

        if (llmResponseDeprecated.getAnswer() == null) {
            PitMessageBox.show("Kein Content erhalten.");

            // Analog zur C#-Implementierung kann hier bei Bedarf ein Fehlerlog
            // geschrieben werden.
            System.err.println("OpenAi API-Fehler: Kein Content erhalten.");
            System.err.println(responseContent);
        }

        if (modelResponse.getUsageInfo() != null) {
            llmResponseDeprecated.setInputTokens(
                    modelResponse.getUsageInfo().getInputTokens()
            );
            llmResponseDeprecated.setOutputTokens(
                    modelResponse.getUsageInfo().getOutputTokens()
            );
            llmResponseDeprecated.setTotalTokens(
                    modelResponse.getUsageInfo().getTotalTokens()
            );
        }

        TokenLogger.log(request, llmResponseDeprecated);

        return llmResponseDeprecated;
    }

    /**
     * DTO für den Request an die OpenAI Responses API.
     */
    private record OpenAiRequestBody(
            String model,
            Object input,
            boolean store
    ) {
    }
}
