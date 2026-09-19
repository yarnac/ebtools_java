package com.eb.apps.ebchatclient.domain.chat;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class AiChatContext {
    private String name;
    private String kategorie;
    private String systemQuestion;
    private String userQuestion;
    private List<String> fileNames = new ArrayList<>();

    public AiChatContext() {
    }

    public AiChatContext(String name) {
        this.name = name;
    }


    public String getRequestMessage() {
        return "<<" + systemQuestion + ">>\n" + userQuestion;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
