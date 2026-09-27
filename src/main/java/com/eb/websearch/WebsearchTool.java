package com.eb.websearch;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient.Version;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * Tool zur Durchführung von Websearch-Anfragen via Ollama mit integrierter Internetrecherche-Fähigkeit.
 * Verwendet den Ollama API-Endpunkt /api/chat für qwen3.5:35b mit Function Calling (falls unterstützt).
 * Alternativ: direkte Integration eines externen Websearch-APIs über einen Adapter.
 */
public class WebsearchTool {

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String ollamaBaseUrl;

    public WebsearchTool(String ollamaBaseUrl) {
        this.httpClient = HttpClient.newBuilder()
            .version(Version.HTTP_2)
            .build();
        this.objectMapper = new ObjectMapper();
        this.ollamaBaseUrl = ollamaBaseUrl.endsWith("/") ? ollamaBaseUrl : ollamaBaseUrl + "/";
    }

    /**
     * Führt eine Webrecherche durch, indem eine Frage an Ollama gesendet wird,
     * das ggf. mit einem Tool (z. B. web_search) antwortet.
     *
     * Hinweis: qwen3.5:35b unterstützt aktuell *keine* native Tool-Call-Funktion.
     * Daher nutzen wir einen Workaround: Wir fragen Ollama, eine Recherche durchzuführen,
     * indem wir den Prompt explizit um den Hinweis "Suche online nach ..." erweitern.
     * Besser: Ein separater Websearch-Service wird vor/nach der Ollama-Anfrage aufgerufen.
     *
     * @param question Die zu suchende Frage
     * @return Die Antwort des Modells (ggf. inkl. Webergebniszitation)
     * @throws IOException wenn HTTP-Fehler auftreten
     * @throws InterruptedException wenn der Thread unterbrochen wird
     */
    public String searchWeb(String question) throws IOException, InterruptedException {
        // 1. Erstelle den Prompt mit explizitem Hinweis auf Online-Suche
        String prompt = String.format(
            "Du bist ein hilfreicher Assistent. Bitte führe eine aktuelle Webrecherche durch, um diese Frage zu beantworten:\n\"%s\"\n\n"
            + "Verwende dabei externe Quellen (z. B. News, Wissensdatenbanken, etc.), und gebe bei Bedarf URLs an.\n"
            + "Antworte klar und strukturiert.",
            question
        );

        // 2. Baue die Ollama /api/chat Anfrage
        Map<String, Object> requestMap = new HashMap<>();
        requestMap.put("model", "qwen3.5:35b");
        requestMap.put("messages", List.of(Map.of("role", "user", "content", prompt)));
        requestMap.put("stream", false);

        // 3. Serialisiere JSON
        String jsonBody = objectMapper.writeValueAsString(requestMap);

        // 4. Sende HTTP POST
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ollamaBaseUrl + "api/chat"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // 5. Verarbe Response
        if (response.statusCode() != 200) {
            throw new RuntimeException("Ollama API failed with status code: " + response.statusCode()
                + ", body: " + response.body());
        }

        JsonNode jsonNode = objectMapper.readTree(response.body());
        String messageContent = jsonNode.path("message").path("content").asText();

        return messageContent != null ? messageContent : "Keine Antwort erhalten.";
    }

    /**
     * Optionale Erweiterung: Verwende einen externen Websearch-Service (z. B. Bing, Google, Tavily)
     * und füge die Ergebnisse als Kontext hinzu.
     *
     * Beispiel: Tavily Search API
     * https://app.tavily.com/
     */
    public String searchWebWithExternalAPI(String question, String externalApiKey, String externalApiUrl)
            throws IOException, InterruptedException {
        // 1. Webrecherche mit externem API
        WebsearchHttpClient websearchClient = new WebsearchHttpClient(externalApiKey);
        List<SearchResult> results = websearchClient.search(question);

        // 2. Erstelle Prompt mit Kontext
        StringBuilder contextBuilder = new StringBuilder();
        for (int i = 0; i < Math.min(results.size(), 3); i++) {
            SearchResult r = results.get(i);
            contextBuilder.append(String.format("[Quelle %d]\nTitel: %s\nURL: %s\nInhalt: %s\n\n",
                    i + 1, r.title(), r.url(), r.snippet()));
        }

        String prompt = String.format(
            "Basierend auf folgenden Suchergebnissen beantworte die Frage:\n\n%s\n\nFrage: %s\n"
            + "Verwende nur Informationen aus den oben genannten Quellen und gebe die Quellennummern an.\n"
            + "Wenn keine Quelle relevant ist, sag das klar.",
            contextBuilder.toString(), question
        );

        // 3. Anfrage an Ollama senden
        Map<String, Object> requestMap = Map.of(
            "model", "qwen3.5:35b",
            "messages", List.of(Map.of("role", "user", "content", prompt)),
            "stream", false
        );

        String jsonBody = objectMapper.writeValueAsString(requestMap);

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(ollamaBaseUrl + "api/chat"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Ollama API failed: " + response.body());
        }

        JsonNode jsonNode = objectMapper.readTree(response.body());
        return jsonNode.path("message").path("content").asText();
    }

    // Simple data record für Suchergebnisse
    public record SearchResult(String title, String url, String snippet) {}
}