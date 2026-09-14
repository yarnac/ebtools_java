package com.eb.base.ai_service.llm_client.api;

import com.eb.base.extensions.StringExtensions;

import java.util.ArrayList;
import java.util.List;

public class LlmRequestBuilder implements LlmRequestBuilderSystemOrUserMsg,
        LlmRequestBuilderUserMsg, LlmRequestBuilderModel, LlmRequestBuilderFinish{

    List<LlmMessage> messages = new ArrayList<LlmMessage>();
    String model;

    static LlmRequestBuilderSystemOrUserMsg create()
    {
        return new LlmRequestBuilder();
    }


    LlmRequestBuilderSystemOrUserMsg builder;

    @Override
    public LlmRequestBuilderUserMsg addSystemMsg(String msgContent) {
         addMsg("system", msgContent);
         return this;
    }

    @Override
    public LlmRequestBuilderUserMsg addUserMsg(String msgContent) {
        addMsg("user", msgContent);
        return this;
    }

    public void addMsg(String role, String msgContent) {
        if (msgContent != null || msgContent.length() > 0)
            messages.add(new LlmMessage(role, msgContent));
    }


    @Override
    public LlmRequestBuilderUserMsg addRequestMsg(String msg) {

        String userMsg = StringExtensions.ebTrimAll(msg);
        String systemMsg = "";

        if (userMsg.startsWith("<<"))
        {
            int index = userMsg.indexOf(">>");
            systemMsg = StringExtensions.ebTrimAll(userMsg.substring(index + 2));
            userMsg = StringExtensions.ebTrimAll(userMsg.substring(2, index));
        }

        addSystemMsg(systemMsg);
        addUserMsg(userMsg);

        return this;
    }    @Override

    public LlmRequestBuilderFinish setModel(String newModel) {
        model = newModel;
        return this;
    }

    @Override
    public LlmRequest build() {
        return new LlmRequest(model, messages);
    }
}
