package com.eb.base.ai_service.llm_client.api;

import java.awt.*;
import java.util.List;

public interface LlmRequestBuilderSystemOrUserMsg {

    LlmRequestBuilderUserMsg addSystemMsg(String msgContent);
    LlmRequestBuilderUserMsg addUserMsg(String msgContent);
    LlmRequestBuilderUserMsg addRequestMsg(String msgContent);
    LlmRequestBuilderUserMsg addRequestMsg(String msgContent, List<String> imageFileNames);


    LlmRequestBuilderUserMsg addUserMsgWithImage(String userMessage, String imagePath);
}
