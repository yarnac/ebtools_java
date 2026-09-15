package com.eb.apps.ebchatclient.app;

import com.eb.base.ai_service.llm_client.api.LlmRequestService;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import com.eb.base.ai_service.llm_client.infrastructure.ollama.OllamaClient;
import com.eb.base.gui.PitMessageBox;

import java.awt.*;

public class SimpleAiRequestMessreihe {
    public static void main(String[] args) {


        for(String host : OllamaClient.HOSTS) {
            for(String model : OllamaClient.MODELLE) {
                for (int i=0; i<5; i++)
                {
                    System.out.println("Lauf Nr. " + (i+1));
                    LlmResponse response = LlmRequestService.sendSimpleRequest("""                        
                            << Du bist Java 21 Programmierer >>
                            Schreibe einen http Client für die Ollama API.
                            """,

                            model,
                            host);

                    System.out.println(response.getDetails());
                }

            }
        }
    }
}
