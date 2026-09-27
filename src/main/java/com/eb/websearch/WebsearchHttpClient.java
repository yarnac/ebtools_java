package com.eb.websearch;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * HTTP Client für Websearch-APIs (z. B. Tavily).
 * Ersetze die Implementierung durch andere APIs (Bing, Google Custom Search, SerpAPI etc.).
 */
public class WebsearchHttpClient {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String apiUrl;

    public WebsearchHttpClient(String apiKey) {
        this(apiKey, "https://api.tavily.com/search");
    }

    public WebsearchHttpClient(String apiKey, String apiUrl) {
        this.httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2)
            .build();
        this.objectMapper = new ObjectMapper();
        this.apiKey = apiKey;
        this.apiUrl = apiUrl;
    }

    /**
     * Führt eine Suche über die externe API durch.
     *
     * @param query Suchbegriff
     * @return Liste mit Ergebnissen
     * @throws IOException bei HTTP- oder JSON-Fehlern
     * @throws InterruptedException bei Unterbrechung
     */
    public List<WebsearchTool.SearchResult> search(String query) throws IOException, InterruptedException {
        // Beispiel für Tavily: JSON Payload
        Map<String, Object> payload = new java.util.HashMap<>();
        payload.put("api_key", apiKey);
        payload.put("query", query);
        payload.put("search_depth", "basic");
        payload.put("include_answers", true);
        payload.put("include_images", false);

        String jsonBody = objectMapper.writeValueAsString(payload);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Websearch API failed: " + response.statusCode() + ", body: " + response.body());
        }

        JsonNode json = objectMapper.readTree(response.body());
        List<WebsearchTool.SearchResult> results = new ArrayList<>();

        JsonNode resultsNode = json.path("results");
        if (resultsNode.isArray()) {
            for (JsonNode node : resultsNode) {
                String title = node.path("title").asText();
                String url = node.path("url").asText();
                String snippet = node.path("content").asText();
                results.add(new WebsearchTool.SearchResult(title, url, snippet));
            }
        }

        return results;
    }
}