package com.eb.apps.ebchat_utils.simple;

public class SimpleAiServiceFactory {
    public static SimpleAiService getSimpleAiService(String model, String uri){
        if (isOpenAiModel(model)) {
            return new SimpleOpenAiService(model, uri);
        }

        return null;

    }

    private static boolean isOpenAiModel(String model) {
        return false;
    }
}
