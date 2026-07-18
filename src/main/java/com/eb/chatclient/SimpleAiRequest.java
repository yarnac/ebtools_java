package com.eb.chatclient;

public class SimpleAiRequest {
    private String model;
    private String uri;
    private String systemText;
    private String userText;

    // output


    private String answer;
    private int inputTopens;
    private int outputTokens;
    private int totalTokens;

    public String getModel() {
        return model;
    }

    public SimpleAiRequest setModel(String model) {
        this.model = model;
        return this;
    }

    public String getUri() {
        return uri;
    }

    public SimpleAiRequest setUri(String uri) {
        this.uri = uri;
        return this;
    }

    public String getSystemText() {
        return systemText;
    }

    public SimpleAiRequest setSystemText(String systemText) {
        this.systemText = systemText;
        return this;
    }

    public String getUserText() {
        return userText;
    }

    public SimpleAiRequest setUserText(String userText) {
        this.userText = userText;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public SimpleAiRequest setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public int getInputTopens() {
        return inputTopens;
    }

    public SimpleAiRequest setInputTopens(int inputTopens) {
        this.inputTopens = inputTopens;
        return this;
    }

    public int getOutputTokens() {
        return outputTokens;
    }

    public SimpleAiRequest setOutputTokens(int outputTokens) {
        this.outputTokens = outputTokens;
        return this;
    }

    public int getTotalTokens() {
        return totalTokens;
    }

    public SimpleAiRequest setTotalTokens(int totalTokens) {
        this.totalTokens = totalTokens;
        return this;
    }

}
