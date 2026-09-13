package com.eb.chatclient.domain.chat;

import com.eb.ai_service.llm_client.infrastructure.LlmModel;
import com.eb.ai_service.llm_client.infrastructure.LlmModelProvider;

public class Kosten {
    private String model;
    private double inputPer1MTokens;
    private double outputPer1MTokens;
    private double tokenCount;

    public Kosten(String model, double inPrice, double outPrice, int count) {
        this.model = model;
        this.inputPer1MTokens = inPrice;
        this.outputPer1MTokens = outPrice;
        this.tokenCount = count;
    }

    public static double getPreis(String modelName, int input, int output) {
        LlmModel model = LlmModelProvider.getCurrent().getModel(modelName);
        if (model == null) return 0.0;
        return (input * model.getInputTokenPrice() + output * model.getOutputTokenPrice()) / 1000000.0;
    }

    public static double getPreisEuroMwst(String model, int input, int output) {
        double preis = getPreis(model, input, output);
        return preis * 1.19 * 0.88;
    }

    public static double getPreisEuroMwst(AiChatMessage message) {
        return getPreisEuroMwst(message.getModel(), message.getInputTokens(), message.getOutputTokens());
    }
}
