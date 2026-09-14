package com.eb.base.ai_service.llm_client.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LlmMessage {
    @JsonProperty("role")
    String role;
    @JsonProperty("content")
    String message;

    public LlmMessage(String user, String s) {
        role = user;
        message = s;
    }

    @Override
    public String toString() {return role + ": " + message;}
}
