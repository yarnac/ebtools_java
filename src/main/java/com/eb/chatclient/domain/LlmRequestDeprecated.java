package com.eb.chatclient.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LlmRequestDeprecated {

    private String model;
    private List<LlmMessageDeprecated> messages;

    public LlmRequestDeprecated() {
        messages = new ArrayList<LlmMessageDeprecated>();
    }

    public void addMessages(final List<LlmMessageDeprecated> messages) {

        if (messages == null)
            return;

        for(LlmMessageDeprecated message : messages)
            addMessage(message);
    }

    public void addMessage(final LlmMessageDeprecated message) {

        messages.add(message);
    }

    public void addMessage(String role, String content)
    {
        LlmMessageDeprecated msg = new LlmMessageDeprecated();
        msg.setRole(role);
        msg.setContent(content);
        getMessages().add(msg);
    }

    public void addSystemMessage(final String message) {
        addMessage("system", message);
    }
    public void addUserMessage(final String message) {
        addMessage("user", message);
    }
    public void addAssistentMessage(final String message) {
        addMessage("assistent", message);
    }
}
