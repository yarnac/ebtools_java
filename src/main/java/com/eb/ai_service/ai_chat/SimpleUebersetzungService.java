package com.eb.ai_service.ai_chat;

import com.eb.ai_service.llm_client.api.LlmClient;
import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmResponse;

public class SimpleUebersetzungService {

    public static void main(String[] args)
    {
        SimpleUebersetzungService service = new SimpleUebersetzungService();

        String text = """
                On altı-on sekiz yaşlarımda, bir yandan radikal bir Batılılaşmacı gibi, şehrin ve kendimin bütünüyle Batılı olmasını istiyor, bir yandan da içgüdülerim, alışkanlıklarım ve anılarımla sevdiğim İstanbul’a ait olmak istiyordum. Çocukken bu iki talebi (bir çocuk ileride hem serseri, hem de büyük bir bilim adamı olacağını aynı anda sorunsuzca düşleyebilir) aklımın iki ayrı köşesinde koruyabilme yeteneğini yaşım ilerledikçe kaybetmem, beni yavaş yavaş, hüzünlü bir kişiye çeviriyordu.
                """;
        // String result2 = service.getUebersetzungen(text, "Türkisch", "gpt-5.6-luna");
        String result2 = service.getUebersetzungen(text, "Türkisch", "qwen3:14b");
        System.out.println(result2);
    }


    public String getUebersetzungen(String word, String sprache, String model)
    {
        String systemMessage = "Du bist %s Deutsch Übersetzer".formatted(sprache);
        String userMessage = """
                Übersetze den Text '%s' von '%s' nach Deutsch. 
                Liefere nur die deutsche Übersetzung.
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
