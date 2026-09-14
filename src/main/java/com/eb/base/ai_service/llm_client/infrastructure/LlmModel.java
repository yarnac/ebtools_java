package com.eb.base.ai_service.llm_client.infrastructure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LlmModel {

    private String company;
    private String modelName;

    @Override
    public String toString() {return company + " " +  modelName ;}

    public boolean isAnthropic()
    {
        return company.equals("anthropic");
    }

    public boolean isOpenAi()
    {
        return company.equals("openai");
    }

    public boolean isOllama()
    {
        return company.equals("ollama");
    }

    public int getInputTokenPrice() {
        return 0;
    }

    public int getOutputTokenPrice() {
        return 0;
    }
}
