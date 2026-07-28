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
        messages.add(new LlmMessage("user", msgContent));
        return this;
    }

    @Override
    public LlmRequestBuilderModel addRequestMsg(String requestMsg) {

        if (requestMsg.startsWith("<<"))
        {
            int index = requestMsg.indexOf(">>");
            String textSystem = requestMsg.substring(2,index);
            String textUser = requestMsg.substring(index+2);
            int newIndex = index+2;
            while (newIndex < requestMsg.length())
            {
                char ch = requestMsg.charAt(newIndex);
                if (Character.isLetterOrDigit(ch))
                    break;
                newIndex++;
            }
            textUser = requestMsg.substring(newIndex);
            addSystemMsg(textSystem);
            addUserMsg(textUser);
        }
        else
            addSystemMsg(requestMsg);

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
