package com.eb.ai_service.llm_client.api;

import java.util.List;

public interface LlmMessageBuilderUser {
    public LlmMessageListBuilder addUserMsg(String msgContent);
    public List<LlmMessage> build();
}

