package com.eb.ai_service.ai_chat;

import com.eb.ai_service.llm_client.api.*;

import java.util.List;

public class SimpleWoerterbuchService {

    public static void main(String[] args)
    {
        SimpleWoerterbuchService service = new SimpleWoerterbuchService();

        String result = service.getUebersetzungen("cikarmak", "Türkisch", "gpt-5.4-nano");
        System.out.println(result);

        String result2 = service.getUebersetzungen("volar", "Portugiesisch", "gpt-5.4-nano");
        System.out.println(result2);
    }


    public String getUebersetzungen(String word, String sprache, String model)
    {
        String systemMessage = "Du bist %s Deutsch Übersetzer".formatted(sprache);
        String userMessage = """
                Übersetze das Wort '%s' von '%s' nach Deutsch. 
                Liefere nur die deutschen Übersetzungen im Format Übersetzung1|Übersetzung2 usw.
                ##Beispiel fuer yapmak
                machen|tun|herstellen
                """.formatted(word, sprache);;

        LlmRequest request = LlmRequest.builder()
                .addSystemMsg(systemMessage)
                .addUserMsg(userMessage)
                .setModel(model)
                .build();

        LlmClient client = new LlmClient();
        LlmResponse response = client.sendRequest(request);
        return response.getAnswer();
    }






}
