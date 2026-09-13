package com.eb.chatclient.domain.chat;

import java.util.ArrayList;
import java.util.List;

public class AiChatContext {
    private String name;
    private String kategorie;
    private String systemQuestion;
    private String userQuestion;
    private String requestMessage;
    private List<String> fileNames = new ArrayList<>();

    public AiChatContext() {
    }

    public AiChatContext(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getKategorie() { return kategorie; }
    public void setKategorie(String kategorie) { this.kategorie = kategorie; }

    public String getSystemQuestion() { return systemQuestion; }
    public void setSystemQuestion(String systemQuestion) { this.systemQuestion = systemQuestion; }

    public String getUserQuestion() { return userQuestion; }
    public void setUserQuestion(String userQuestion) { this.userQuestion = userQuestion; }

    public List<String> getFileNames() { return fileNames; }
    public void setFileNames(List<String> fileNames) { this.fileNames = fileNames; }

    public String getRequestMessage() {
        return "<<" + systemQuestion + ">>\n" + userQuestion;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
}
