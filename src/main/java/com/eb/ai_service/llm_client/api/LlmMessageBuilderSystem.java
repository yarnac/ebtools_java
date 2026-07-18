package com.eb.ai_service.llm_client.api;

import java.util.List;

public interface LlmMessageBuilderSystem {
    public LlmMessageBuilderUser addSystemMsg(String msgContent);
    public LlmMessageBuilderUser addUserMsg(String msgContent);
}
