package com.eb.base.ai_service.llm_client.api;

import com.eb.base.extensions.StringExtensions;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LlmRequestBuilder implements LlmRequestBuilderSystemOrUserMsg,
        LlmRequestBuilderUserMsg, LlmRequestBuilderModel, LlmRequestBuilderFinish{

    String model;
    List<LlmMessage> messages = new ArrayList<>();
    List<Parameter> parameters = new ArrayList<>();

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

    @Override
    public LlmRequestBuilderUserMsg addUserMsgWithImage(String msgContent, String imagePath) {

        if (msgContent != null && msgContent.length() > 0)
        {
            LlmMessage message = new LlmMessage("user", msgContent);
            try {
                message.addImageFromPath(imagePath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            messages.add(message);
        }
        return this;
    }

    public void addMsg(String role, String msgContent) {
        if (msgContent != null && msgContent.length() > 0)
            messages.add(new LlmMessage(role, msgContent));
    }



    @Override
    public LlmRequestBuilderUserMsg addRequestMsg(String msg, List<String> imageFileNames) {

        String userMsg = StringExtensions.ebTrimAll(msg);
        String systemMsg = "";

        if (userMsg.startsWith("<<"))
        {
            int index = userMsg.indexOf(">>");
            systemMsg = StringExtensions.ebTrimAll(userMsg.substring(index + 2));
            userMsg = StringExtensions.ebTrimAll(userMsg.substring(2, index));
        }

        addSystemMsg(systemMsg);
        addMsg("user", userMsg, imageFileNames);

        return this;
    }

    private void addMsg(String role, String msgContent, List<String> imageFileNames) {
        if (msgContent != null && msgContent.length() > 0) {
            messages.add(new LlmMessage(role, msgContent, imageFileNames));

        }
    }

    @Override

    public LlmRequestBuilderFinish setModel(String newModel) {
        model = newModel;
        return this;
    }

    @Override
    public LlmRequest build() {
        return new LlmRequest(model, messages, parameters);
    }

    @Override
    public LlmRequestBuilderFinish addParameter(String parameterName, Object value) {
        parameters.add(new Parameter(parameterName, value));
        return this;
    }

    @Override
    public LlmRequestBuilderUserMsg addRequestMsg(String inputString) {
        addRequestMsg(inputString, new ArrayList<>());
        return this;
    }
}
