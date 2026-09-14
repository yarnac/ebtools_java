package com.eb.apps.ebchatclient.domain.chat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class AiChatSerializer
{
    public static String serializeAiChatMessages(List<AiChatMessage> aiChatMessages)
    {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String jsonString;
            jsonString = mapper.writeValueAsString(aiChatMessages);
            return jsonString;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<AiChatMessage> deserializeAiChatMessages(String json)
    {
        ObjectMapper mapper = new ObjectMapper();

        try
        {
            List<AiChatMessage> aiChatMessages = mapper.readValue(json, new TypeReference<List<AiChatMessage>>() {});
            return aiChatMessages;
        }
        catch (Exception e)
        {
            return new ArrayList<>();
        }
    }
}
