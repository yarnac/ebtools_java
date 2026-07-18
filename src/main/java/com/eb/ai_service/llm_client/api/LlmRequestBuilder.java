package com.eb.ai_service.llm_client.api;

import java.util.ArrayList;
import java.util.List;

public class LlmRequestBuilder implements LlmRequestBuilderSystem, LlmRequestBuilderUser, LlmRequestBuilderModel, LlmRequestBuilderFinish{

    List<LlmMessage> messages = new ArrayList<LlmMessage>();
    String model;

    static LlmRequestBuilderSystem create()
    {
        return new LlmRequestBuilder();
    }


    LlmMessageBuilderSystem builder;

    @Override
    public LlmRequestBuilderUser addSystemMsg(String msgContent) {
        messages.add(new LlmMessage("system", msgContent));
        return this;
    }

    @Override
    public LlmRequestBuilderModel addUserMsg(String msgContent) {
        messages.add(new LlmMessage("system", msgContent));
        return this;
    }

    @Override
    public LlmRequestBuilderFinish setModel(String newModel) {
        model = newModel;
        return this;
    }

    @Override
    public LlmRequest build() {
        return new LlmRequest(model, messages);
    }
}
