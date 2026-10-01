package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Setter
@Getter
public class LlmMessageRaw {
    private String role;
    private List<Map<String, Object>> content;

    public LlmMessageRaw(String role) {
        this.role = role;
        content = new ArrayList<>();
    }

    public void addContentPartMap(Map<String, Object> contentPart) {
        content.add(contentPart);
    }

    @JsonIgnore
    public String getJsonString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }




}
