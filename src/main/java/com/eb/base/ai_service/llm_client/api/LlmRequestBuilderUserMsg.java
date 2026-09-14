package com.eb.base.ai_service.llm_client.api;

public interface LlmRequestBuilderUserMsg {
    LlmRequestBuilderUserMsg addUserMsg(String msgContent);
    LlmRequestBuilderFinish setModel(String newModel);
    LlmRequest build();
}

