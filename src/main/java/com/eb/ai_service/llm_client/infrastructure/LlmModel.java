package com.eb.ai_service.llm_client.infrastructure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LlmModel {

    private String company;
    private String modelName;

    @Override
    public String toString() {return modelName + " " + company;}

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
}
