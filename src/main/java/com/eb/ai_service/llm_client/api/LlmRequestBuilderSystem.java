package com.eb.ai_service.llm_client.api;

public interface LlmRequestBuilderSystem {

    public LlmRequestBuilderUser addSystemMsg(String msgContent);
    public LlmRequestBuilderModel addUserMsg(String msgContent);
}
