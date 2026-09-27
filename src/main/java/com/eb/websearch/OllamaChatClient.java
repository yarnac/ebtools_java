package com.eb.websearch;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Einfacher Ollama HTTP Client für /api/chat.
 * Wird vom WebsearchTool verwendet.
 */
public class OllamaChatClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public OllamaChatClient(String baseUrl) {
        this.httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2)
            .build();
        this.objectMapper = new ObjectMapper();
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }

    public String chat(String model, String userMessage) throws IOException, InterruptedException {
        Map<String, Object> requestMap = Map.of(
            "model", model,
            "messages", List.of(Map.of("role", "user", "content", userMessage)),
            "stream", false
        );

        String jsonBody = objectMapper.writeValueAsString(requestMap);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(baseUrl + "api/chat"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Ollama /api/chat failed: " + response.body());
        }

        return objectMapper.readTree(response.body())
            .path("message")
            .path("content")
            .asText();
    }
}