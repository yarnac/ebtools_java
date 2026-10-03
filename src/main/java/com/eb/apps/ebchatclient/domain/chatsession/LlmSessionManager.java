package com.eb.apps.ebchatclient.domain.chatsession;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmResponse;

public class LlmSessionManager {
    private static LlmSessionManager llmSessionManager;

    public static LlmSessionManager getInstance() {
        if (llmSessionManager == null) {
            llmSessionManager = new LlmSessionManager();
        }
        return llmSessionManager;
    }

    public void addResponse(LlmRequest request, LlmResponse response){
        request.getMessages().add(new LlmMessage("assistant", response.getAnswer()));

    }

    public void addUserMessage(LlmRequest request, String message){
        request.getMessages().add(new LlmMessage("user", message));
    }



    private LlmSessionManager() {

    }

}
