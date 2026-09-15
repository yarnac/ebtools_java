package com.eb.base.ai_service.llm_client.infrastructure.openai;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.infrastructure.ILlmClient;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import com.eb.base.ai_service.llm_client.infrastructure.TokenLogger;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.*;

public class OpenAiClient implements ILlmClient {

    private final HttpClient httpClient;
    private final ObjectMapper mapper;
    private final String apiKey;

    public OpenAiClient(HttpClient newHttpClient, String newApiKey) {
        httpClient = newHttpClient;
        apiKey = newApiKey;

        mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public LlmResponse sendRequest(LlmRequest llmRequest) throws IOException, InterruptedException {

        List<LlmMessage> messages = llmRequest.getMessages();

        Map<String, Object> body = new HashMap<>();
        body.put("model", llmRequest.getModel());
        // body.put("input", MessageGenerator.createMessages(messages));
        body.put("input", messages);
        body.put("store", false);

        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/responses"))
                .timeout(Duration.ofMinutes(30))
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " +  apiKey )
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        long startTime = System.nanoTime();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        long endTime = System.nanoTime();
        double elapsedSeconds = (endTime - startTime) / 1000000000.0;

        String responseBody = response.body();
        OpenAiResponse openAiResponse = mapper.readValue(responseBody , OpenAiResponse.class);

        LlmResponse llmResponse = new LlmResponse();
        List<OpenAiResponse.Output> output = openAiResponse.getOutput();

        for(OpenAiResponse.Output currentOutput: output)
        {
            if (currentOutput.getType().equals("message"))
            {
                llmResponse.setAnswer(currentOutput.getContent().get(0).getText());
                break;
            }
        }
        llmResponse.setModel(openAiResponse.getModel());
        llmResponse.setInputTokens(openAiResponse.getUsage().getInput_tokens());
        llmResponse.setOutputTokens(openAiResponse.getUsage().getOutput_tokens());
        llmResponse.setTotalTokens(openAiResponse.getUsage().getTotal_tokens());
        llmResponse.setRequest(llmRequest);
        llmResponse.setSecondsToRun(elapsedSeconds);

        TokenLogger.log(llmRequest, llmResponse);
        return llmResponse;
    }


}
