package com.eb.ai_service.llm_client.api;

import java.util.ArrayList;
import java.util.List;

public class LlmMessageListBuilder implements LlmMessageBuilderSystem, LlmMessageBuilderUser{

    private List<LlmMessage> list;

    private LlmMessageListBuilder() {
        list = new ArrayList<LlmMessage>();
    }

    static LlmMessageListBuilder create() {
        return new LlmMessageListBuilder();
    }



    public LlmMessageBuilderUser addSystemMsg(String msgContent)
    {
        list.add(new LlmMessage("system", msgContent));
        return this;
    }

    @Override
    public LlmMessageListBuilder addUserMsg(String msgContent)
    {
        list.add(new LlmMessage("user", msgContent));
        return this;
    }

    public List<LlmMessage> build() {
        return list;
    }
}

