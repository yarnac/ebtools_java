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

    public static String[] HOSTS = new String[]{"macbookeb", "macbook-air-von-ekkart3", "xt13", "127.0.0.1"};
    public static String HOST;

    @Override
    public LlmResponse sendRequest(LlmRequest llmRequest) throws IOException, InterruptedException {

        if (HOST==null)
        {
            HOST = determineHost();
        }

        /*
        Schreibe eine Java21 Klasse, die die Dauer eines Aufrufes eines Runnables ermittelt:‚
        double determineSecondsToRun(Runnable runnable)
         */

        List<LlmMessage> messages = llmRequest.getMessages();

        Map<String, Object> body = new HashMap<>();
        body.put("model", llmRequest.getModel());
        body.put("messages", messages);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                // .uri(URI.create("http://%s:11434/v1/chat/completions".formatted("macbook-air-von-ekkart")))
                // .uri(URI.create("http://%s:11434/v1/chat/completions".formatted("xt13")))
                // .uri(URI.create("http://%s:11434/v1/chat/completions".formatted("macbookeb")))
                .uri(URI.create("http://%s:11434/v1/chat/completions".formatted(HOST)))

                //.uri(URI.create("http://127.0.0.1:11434/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + "")
                .timeout(Duration.ofMinutes(30))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();


        long startTime = System.nanoTime();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1000000000.0;




        OllamaResponse ollamaResponse =
                mapper.readValue(response.body(), OllamaResponse.class);

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
}
