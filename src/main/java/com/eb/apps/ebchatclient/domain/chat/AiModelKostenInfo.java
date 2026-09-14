package com.eb.apps.ebchatclient.domain.chat;

import java.util.ArrayList;
import java.util.List;

public class AiModelKostenInfo {
    private String model;
    private double inputPer1MTokens;
    private double outputPer1MTokens;
    private double tokenCount;

    public AiModelKostenInfo() {
        this.model = "xxx";
    }

    public AiModelKostenInfo(String model, double inPrice, double outPrice, int count) {
        this.model = model;
        this.inputPer1MTokens = inPrice;
        this.outputPer1MTokens = outPrice;
        this.tokenCount = count;
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public double getInputPer1MTokens() { return inputPer1MTokens; }
    public void setInputPer1MTokens(double inputPer1MTokens) { this.inputPer1MTokens = inputPer1MTokens; }

    public double getOutputPer1MTokens() { return outputPer1MTokens; }
    public void setOutputPer1MTokens(double outputPer1MTokens) { this.outputPer1MTokens = outputPer1MTokens; }

    public double getTokenCount() { return tokenCount; }
    public void setTokenCount(double tokenCount) { this.tokenCount = tokenCount; }

    public static double getPreis(String model, int input, int output) {
        return Kosten.getPreisEuroMwst(model, input, output);
    }

    public static double GetPreisEuroMwst(String model, int input, int output) {
        double preis = getPreis(model, input, output);
        return preis * 1.19 * 0.88;
    }

    public static List<AiModelKostenInfo> GetKostenListe() {
        List<AiModelKostenInfo> res = new ArrayList<>();
        res.add(new AiModelKostenInfo("gpt-5.4-mini", 0.75, 4.5, 1000000));
        res.add(new AiModelKostenInfo("gpt-5.4-nano", 0.25, 1.25, 1000000));
        res.add(new AiModelKostenInfo("gpt-5.4", 2.5, 15, 1000000));
        res.add(new AiModelKostenInfo("gpt-5.5", 5, 30, 1000000));
        return res;
    }

    public static AiModelKostenInfo GetKosten(String model) {
        for (AiModelKostenInfo kosten : GetKostenListe()) {
            if (model != null && model.equals(kosten.model)) return kosten;
        }
        return new AiModelKostenInfo();
    }

    public static double GetPreisEuroMwst(AiChatMessage message) {
        return GetPreisEuroMwst(message.getModel(), message.getInputTokens(), message.getOutputTokens());
    }
}
