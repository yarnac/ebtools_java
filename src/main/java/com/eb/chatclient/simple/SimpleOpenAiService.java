package com.eb.chatclient.simple;

public class SimpleOpenAiService implements SimpleAiService {
    public SimpleOpenAiService(String model, String uri) {
    }

    @Override
    public void SendAndReceiveAiRequest(SimpleAiRequest simpleAiRequest) {
        OpenAiRequest request = BuidOpenAiRequest(simpleAiRequest);
    }

    private OpenAiRequest BuidOpenAiRequest(SimpleAiRequest simpleAiRequest) {
        return null;
    }
}
