package com.eb.chatclient;

public class SimpleAiServiceFactory {
    public static SimpleAiService getSimpleAiService(String model, String uri){
        if (isOpenAiModel(model)) {
            return new SimpleOpenAiService(model, uri);
        }

    }

    private static boolean isOpenAiModel(String model) {
        return false;
    }
}
