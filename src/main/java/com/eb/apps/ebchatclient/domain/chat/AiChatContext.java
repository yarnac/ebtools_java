package com.eb.apps.ebchatclient.domain.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class AiChatContext {
    private String name;
    private String knoten;
    private String systemPrompt;
    private String userPrompt;
    private List<String> fileNames = new ArrayList<>();

    public AiChatContext() {
    }

    public AiChatContext(String name) {
        this.name = name;
    }

    public AiChatContext(String name, String knoten, String systemPrompt, String userPrompt) {
        this.name = name;
        this.knoten = knoten;
        this.systemPrompt = systemPrompt;
        this.userPrompt = userPrompt;
    }


    public String getRequestMessage() {
        return "<<" + systemPrompt + ">>\n" + userPrompt;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
