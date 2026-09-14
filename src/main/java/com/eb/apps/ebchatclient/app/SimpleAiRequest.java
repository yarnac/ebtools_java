package com.eb.apps.ebchatclient.app;

import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmRequestService;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import com.eb.base.gui.PitMessageBox;

import java.awt.*;

public class SimpleAiRequest {
    public static void main(String[] args) {
        LlmRequest request = LlmRequest.builder()
                .addSystemMsg("Du bist ein freundlicher AI Assistent.")
                .addUserMsg("Sag Hallo")
                .setModel("qwen3:8b")
                .build();

        EventQueue.invokeLater(new Runnable() {
            public void run() {

                LlmResponse response = LlmRequestService.sendRequest(request);
                PitMessageBox.show(response.getAnswer());
            }
        });
    }
}
