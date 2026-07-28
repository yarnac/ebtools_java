package com.eb.ai_service.llm_client.api;

import com.eb.ai_service.llm_client.infrastructure.LlmModel;

public interface LlmRequestBuilderSystem {

    public LlmRequestBuilderUser addSystemMsg(String msgContent);
    public LlmRequestBuilderModel addUserMsg(String msgContent);
    public LlmRequestBuilderModel addRequestMsg(String msgContent);


}
