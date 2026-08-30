package com.eb.ai_service.llm_client.infrastructure.openai;

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

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        OpenAiResponse openAiResponse = mapper.readValue(responseBody , OpenAiResponse.class);

        LlmResponse llrResponse = new LlmResponse();
        List<OpenAiResponse.Output> output = openAiResponse.getOutput();

        for(OpenAiResponse.Output currentOutput: output)
        {
            if (currentOutput.getType().equals("message"))
            {
                llrResponse.setAnswer(currentOutput.getContent().get(0).getText());
                break;
            }
        }
        llrResponse.setModel(openAiResponse.getModel());
        llrResponse.setInputTokens(openAiResponse.getUsage().getInput_tokens());
        llrResponse.setOutputTokens(openAiResponse.getUsage().getOutput_tokens());
        llrResponse.setTotalTokens(openAiResponse.getUsage().getTotal_tokens());
        TokenLogger.log(llmRequest, llrResponse);
        return llrResponse;
    }


}
