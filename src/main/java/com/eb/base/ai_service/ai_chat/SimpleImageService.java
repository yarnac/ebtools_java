package com.eb.base.ai_service.ai_chat;

import com.eb.base.ai_service.llm_client.api.LlmClient;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmResponse;

public class SimpleImageService {

    public static void main(String[] args)
    {
        SimpleImageService service = new SimpleImageService();

        //String text = "Analysiere das folgende Bild";
        String text = getVerschlagwortungString();

        String[] modelAuswahl = new String[]{"qwen3.5:4b", "qwen3.5:9b", "qwen3.5:35b", "qwen3-vl:30b", "gpt-5.6-luna","claude-haiku-4-5"};
        for (int i=0;i<modelAuswahl.length;i++)
        {
            String model = modelAuswahl[i];
            for (int j=0;j<5;j++)
            System.out.println(service.getImageDescription(text, "/Volumes/Macintosh HD/Users/ekkart/Pictures/Rotterdam/1/2794.JPG" , model));
        }

    }

    private static String getVerschlagwortungString() {
        String text =
                """
                                Du agierst als automatisiertes Daten-Tagging-Modell für ein Archivierungssystem. Deine Aufgabe ist es, das angehängte Bild analysierend in ein striktes JSON-Format zu taggen.
                        
                                Folgende Regeln gelten zwingend:
                                1. Gib NUR den validen JSON-Code aus. Kein markdown (also keine ```json Blöcke), keine Einleitung, keine Erklärungen.
                                2. Die Sprache der Tags (Inhalte) MUSS Deutsch sein.
                                3. Nutze für die Struktur exakt folgende JSON-Schema-Keys:
                                   - "architecture" (Liste mit Tags zu Gebäuden/Infrastruktur)
                                   - "landscape" (Liste mit Tags zu Natur/Umgebung)
                                   - "people" (Liste mit Tags zu Menschen/Personen)
                                   - "motiv" (Liste mit Tags zum Hauptmotiv des Bildes)
                                4. Wenn eine Kategorie nicht im Bild sichtbar ist, nutze ein leeres Array: []
                                5. Nutze keine Markdown-Formatierung für die Ausgabe, nur reiner JSON-Text.
                                6. Gebe je Kategorie immer 5 Schlagworte aus.
                        
                                Hier ist das Schema, das du ausgeben musst:
                                {
                                  "architecture": [ "Tag 1", "Tag 2" ],
                                  "landscape": [ "Tag 1", "Tag 2" ],
                                  "people": [ "Tag 1" ],
                                  "motiv": [ "Tag 1" ]
                                }
                        
                                Analysiere nun das Bild und generiere die Verschlagwörter nach diesem Schema.
                        """;
        return text;
    }

    public String getImageDescription(String userMessage, String image, String model)
    {
        LlmRequest request = LlmRequest.builder()
                .addUserMsgWithImage(userMessage, image)
                .setModel(model)
                .addParameter("temperature", 0.0)
                .build();

        LlmClient client = new LlmClient();
        LlmResponse response = client.sendRequest(request);
        return response.getAnswer() + "\n%s\t%d / %.2f \t %.2f T/s".formatted(model, response.getTotalTokens(), response.getSecondsToRun(), response.getTokensPerSecond());
    }
}
