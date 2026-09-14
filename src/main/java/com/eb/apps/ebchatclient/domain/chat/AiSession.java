package com.eb.apps.ebchatclient.domain.chat;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmResponse;

import java.util.ArrayList;
import java.util.List;

public class AiSession {
    private String name;
    private List<LlmMessage> messages = new ArrayList<>();

    public AiSession() {
    }

    public AiSession(AiSession session) {
        this.name = session.name;
        this.messages = new ArrayList<>(session.messages);
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<LlmMessage> getMessages() { return messages; }
    public void setMessages(List<LlmMessage> messages) { this.messages = messages; }

    public List<LlmMessage> getCombinedMessages() {
        List<LlmMessage> combined = new ArrayList<>();
        if (messages != null && messages.size() >= 2
                && "system".equals(messages.get(0).getRole())
                && "user".equals(messages.get(1).getRole())) {
            int n = 2;
            StringBuilder sb = new StringBuilder();
            sb.append("<<");
            sb.append(messages.get(0).getMessage());
            sb.append(">>\n");
            sb.append(messages.get(1).getMessage());

            combined.add(new LlmMessage("user", sb.toString()));
            combined.addAll(messages.subList(n, messages.size()));
        } else {
            combined.addAll(messages);
        }
        return combined;
    }

    public void addSystemMessage(String content) {
        messages.add(new LlmMessage("system", content));
    }

    public void addUserMessage(String content) {
        messages.add(new LlmMessage("user", content));
    }

    public void addAssistantMessage(String content) {
        messages.add(new LlmMessage("assistant", content));
    }

    public void addResponse(LlmResponse response) {
        addAssistantMessage(response.getAnswer());
    }

    public String getHistory() {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (LlmMessage message : messages) {
            sb.append("\n");
            if (i++ > 0) sb.append("### ------------------\n");
            sb.append("### " + message.getRole() + "\n");
            sb.append(message.getMessage()+"\n");
        }
        return sb.toString();
    }
}
