package com.eb.base.ai_service.llm_client.api;

import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class LlmMessage {

    public LlmMessage()
    {

    }

    @JsonProperty("role")
    private String role; // z.B. "user", "system", "assistant"

    @JsonProperty("content")
    private List<ContentPart> contentParts = new ArrayList<>();

    @JsonProperty("info")
    private LlmMessageInfo messageInfo;

    @JsonIgnore
    List<String> imageFileNames = new ArrayList<>();

    @JsonIgnore
    List<String> textFileNames = new ArrayList<>();

    // Konstruktor für reine Text-Nachrichten (Backward Compatibility)
    public LlmMessage(String role, String text) {
        this.role = role;
        addText(text);
    }

    public LlmMessage(String role, String text, List<String> imageFileNames) {
        this.role = role;
        addText(text);
        for (String imageFileName : imageFileNames) {
            try {
                addImageFromPath(imageFileName);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Fügt einen reinen Textblock hinzu.
     * WICHTIG: Viele APIs (OpenAI) erwarten auch bei reinem Text eine Liste von Objekten.
     */

    public void addText(String text) {
        ContentPart textPart = new ContentPartText(text);
        this.contentParts.add(textPart);
    }

    /**
     * Fügt ein Bild aus einer lokalen Datei hinzu (konvertiert zu Base64).
     * Form: data:image/jpeg;base64,...
     */

    public void addImageFromPath(String filePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of(filePath));
        String base64 = Base64.getEncoder().encodeToString(imageBytes);

        String lowerCase = filePath.toLowerCase();
        if (lowerCase.endsWith(".png")) {
            addImage("image/png", base64);
        }
        else if (lowerCase.endsWith(".jpg") || lowerCase.endsWith(".jpeg")) {
            addImage("image/jpeg", base64);
        }
    }

    /**
     * Fügt ein Bild hinzu (Bereits als Base64 String oder URL)
     */
    public void addImage(String mimeType, String imageData) {
        ContentPart imagePart = new ContentPartImage(mimeType, imageData);
        this.contentParts.add(imagePart);
    }

    @JsonIgnore
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<%s>\n".formatted(role));
        for (ContentPart contentPart : contentParts) {
            stringBuilder.append(contentPart);
            stringBuilder.append("\n\n");
        }
        return stringBuilder.toString();
    }
}
