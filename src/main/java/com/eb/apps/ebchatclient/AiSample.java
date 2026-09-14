package com.eb.apps.ebchatclient;

import com.eb.apps.ebchatclient.clients.OpenAiClientDeprecated;
import com.eb.apps.ebchatclient.domain.LlmRequestDeprecated;
import com.eb.apps.ebchatclient.domain.LlmResponseDeprecated;

import java.awt.*;
import java.io.IOException;

public class AiSample {
    public static void main(String[] args) {
        String API_KEY = System.getenv("API_KEY_OPENAI");
        OpenAiClientDeprecated client = new OpenAiClientDeprecated(API_KEY);
        LlmRequestDeprecated request = new LlmRequestDeprecated();
        request.setModel("gpt-5.6-luna");
        request.addSystemMessage("Du bist ein freundlicher AI Agent");
        request.addUserMessage("Sag Hallo");

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LlmResponseDeprecated response = client.sendRequest(request);
                    PitMessageBox.show(response.getAnswer());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
