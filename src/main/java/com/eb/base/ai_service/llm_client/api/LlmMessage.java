package com.eb.base.ai_service.llm_client.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


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

    @JsonProperty("role")
    private String role; // z.B. "user", "system", "assistant"

    @JsonProperty("content")
    private List<ContentPart> contentParts = new ArrayList<>();

    @JsonIgnore
    List<String> imageFileNames = new ArrayList<>();

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
        ContentPart textPart = new ContentPart("text");
        textPart.setText(text);
        this.contentParts.add(textPart);
    }

    /**
     * Fügt ein Bild aus einer lokalen Datei hinzu (konvertiert zu Base64).
     * Form: data:image/jpeg;base64,...
     */
    public void addImageFromPath(String filePath) throws IOException {
        byte[] imageBytes = Files.readAllBytes(Path.of(filePath));
        String base64 = Base64.getEncoder().encodeToString(imageBytes);

        // Standard Prefix für OpenAI/Moderne APIs
        String dataUrl = "data:image/jpeg;base64," + base64;

        addImage(dataUrl);
    }

    /**
     * Fügt ein Bild hinzu (Bereits als Base64 String oder URL)
     */
    public void addImage(String imageData) {
        ContentPart imagePart = new ContentPart("image_url");
        // Für OpenAI wird das oft in einem inneren Object 'url' erwartet.
        // Da wir im JSON später serialisiert werden müssen, nutzen wir hier eine Logik.
        // Um die Kompatibilität zu wahren, speichern wir den URL-String direkt als Value oder bauen ein Objekt.

        // Hier bauen wir das OpenAI-kompatible Inner-Object (image_url)
        imagePart.setImageDetails(new ImageUrl(imageData));
        this.contentParts.add(imagePart);
    }

    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<%s>\n".formatted(role));
        for (ContentPart contentPart : contentParts) {
            stringBuilder.append(contentPart.getText());
            stringBuilder.append("\n\n");
        }
        return stringBuilder.toString();
    }
}
