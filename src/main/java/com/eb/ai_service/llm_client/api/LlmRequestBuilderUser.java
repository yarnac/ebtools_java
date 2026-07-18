package com.eb.ai_service.llm_client.api;

import java.util.List;

public interface LlmRequestBuilderUser {

    public LlmRequestBuilderModel addUserMsg(String msgContent);
}
