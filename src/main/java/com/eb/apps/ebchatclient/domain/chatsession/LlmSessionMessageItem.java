package com.eb.apps.ebchatclient.domain.chatsession;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmResponse;

public class LlmSessionMessageItem {
    private LlmMessage llmMessage;
    private LlmResponse llmResponse;

    public LlmSessionMessageItem(LlmMessage llmMessage){
        this.llmMessage = llmMessage;
    }



    public LlmSessionMessageItem(LlmResponse llmResponse) {
        this.llmResponse = llmResponse;

    }

}
