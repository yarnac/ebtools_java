package com.eb.apps.ebchatclient.app;

import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmRequestService;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import com.eb.base.gui.PitMessageBox;

import java.awt.*;

public class SimpleAiRequest2 {
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Starte Request");

                LlmResponse response = LlmRequestService.sendSimpleRequest("""
                        <Du bist ein freundlicher AI Assistent>>
                        Schreibe eine Java Klasse, die Hello World in einem neuen Fenster anzeigt.
                        """);
                PitMessageBox.show("Llm Request Antwort", response.getAnswer());
            }
        });
    }
}
