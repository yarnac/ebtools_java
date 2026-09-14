package com.eb.base.ai_service.llm_client.api;

public interface LlmRequestBuilderSystemOrUserMsg {

    LlmRequestBuilderUserMsg addSystemMsg(String msgContent);
    LlmRequestBuilderUserMsg addUserMsg(String msgContent);
    LlmRequestBuilderUserMsg addRequestMsg(String msgContent);


}
