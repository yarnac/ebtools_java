package com.eb.apps.ebchatclient.app;

import com.eb.base.ai_service.llm_client.api.LlmRequestService;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import com.eb.base.ai_service.llm_client.infrastructure.ollama.OllamaClient;
import com.eb.base.gui.PitMessageBox;
import com.eb.base.io.FileUtil;

import java.awt.*;

public class SimpleAiRequestMessreihe {
    public static void main(String[] args) {


        for(String host : OllamaClient.HOSTS) {
            for(String model : OllamaClient.MODELLE) {
                for (int i=0; i<3; i++)
                {
                    System.out.println("Lauf Nr. " + (i+1));
                    LlmResponse response = LlmRequestService.sendSimpleRequest("""                        
                            << Du bist Deutsch Englisch Übersetzer >>
                            Übersetze den folgenden Text ins Englische:
                                    Eine Woche später bekam ich ein Billet von diesem Polizeirat Stieber. Er fragte mich, ob es mir sehr unangenehm wäre, nach München zu fahren, um dort einen Mann seines Vertrauens zu treffen, einen gewissen Goedsche13, dem ich meinen Bericht übergeben könne. Sicher war mir das nicht gerade angenehm, aber mein Interesse an der zweiten Hälfte des Honorars war größer.
                            """,

                            model,
                            host);

                    FileUtil.appendLine("c:/Data/AiTest/Übersetzungen.txt",response.getAnswer()+"\n\n\n");

                    System.out.println(response.getDetails());
                }

            }
        }
    }
}
