package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LlmRequestJsonMapper {
    private final ContentPartToMapMapper contentPartToMapMapper;
    public LlmRequestJsonMapper(ContentPartToMapMapper contentPartToMapMapper) {
        this.contentPartToMapMapper = contentPartToMapMapper;
    }

    public String getJsonString(LlmRequest llmRequest) {
        LlmMessageMapper messageMapper = new LlmMessageMapper(contentPartToMapMapper);
        List<LlmMessageRaw> messagesRaw = new ArrayList<>();

        for(LlmMessage llmMessage : llmRequest.getMessages())
            messagesRaw.add(messageMapper.getRawMessage(llmMessage));

        Map<String, Object> body = new HashMap<>();

        body.put("model", llmRequest.getModel());
        moveSystemContentsIfNeccesary(messagesRaw, body);
        body.put(contentPartToMapMapper.getContentIdentifier(), messagesRaw);
        if (contentPartToMapMapper.shouldSendMaxTokens())
            body.put("max_tokens", 20000);


        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            String json = jsonMapper.writeValueAsString(body);
            return json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private void moveSystemContentsIfNeccesary(List<LlmMessageRaw> messagesRaw, Map<String, Object> body) {
        if (contentPartToMapMapper.shouldSendSystemSeparately()) {
            LlmMessageRaw systemMessageRaw = messagesRaw.get(0);

            if (systemMessageRaw.getRole().equals("system")) {

                messagesRaw.remove(0);
                body.put("system", systemMessageRaw.getContent());
            }
        }
    }

}
