package com.eb.base.ai_service.llm_client.api;

import com.eb.base.ai_service.llm_client.infrastructure.ollama.OllamaClient;
import lombok.Getter;
import lombok.Setter;

@Setter

@Getter
public class LlmResponse {
    private int status;
    private String answer;
    private String model;
    private String reasoning;
    private String error;
    private int inputTokens;
    private int outputTokens;
    private int totalTokens;
    private LlmRequest request;
    private double secondsToRun;
    private double tokensPerSecond;

    public void calcTokens()
    {
        if (totalTokens == 0)
            totalTokens = inputTokens + outputTokens;
        if (secondsToRun > 0)
            tokensPerSecond = (double) totalTokens / (double) secondsToRun;
    }

    public String getAnswerWithDetails() {
        StringBuilder strb = new StringBuilder();
        strb.append("Modell               %s\n".formatted(model));
        if (model.startsWith("q"))
            strb.append("Host                 %s\n".formatted(OllamaClient.HOST));
        strb.append("Tokens               %d\n".formatted(getTotalTokens()));
        strb.append("Dauer in Sekunden    %.1f\n".formatted(getSecondsToRun()));
        strb.append("Tokens je Sekunde    %.1f\n\n".formatted(getTokensPerSecond()));
        strb.append(getAnswer());
        String res = strb.toString();
        return res;
    }

    public String getDetails() {

        StringBuilder strb = new StringBuilder();
        strb.append("Modell               %s\n".formatted(model));
        if (model.startsWith("q"))
            strb.append("Host                 %s\n".formatted(OllamaClient.HOST));
        strb.append("Tokens               %d\n".formatted(getTotalTokens()));
        strb.append("Dauer in Sekunden    %.1f\n".formatted(getSecondsToRun()));
        strb.append("Tokens je Sekunde    %.1f\n\n".formatted(getTokensPerSecond()));

        return strb.toString()
                .replace("macbook-air-von-ekkart","Mac Book Air M4 16GB")
                .replace("macbookeb","Mac Book Pro M1 Pro 16GB")
                ;

    }
}