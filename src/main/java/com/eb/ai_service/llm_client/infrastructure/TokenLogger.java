package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.AiServiceConfig;
import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class TokenLogger {
    public static void log(LlmRequest request, LlmResponse response) {
        String fileName = AiServiceConfig.current().getTokenFileName();
        StringBuilder sb = new StringBuilder();
        sb.append(java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        sb.append(" ");
        sb.append(request.getModel());
        sb.append(" IT:");
        sb.append(response.getInputTokens());
        sb.append(" OT:");
        sb.append(response.getOutputTokens());
        sb.append(" TT:");
        sb.append(response.getTotalTokens());

        appendLogMessage(fileName, sb.toString());
    }

    public static void appendLogMessage(String fileName, String line) {
        try {
            Files.writeString(
                    Path.of(fileName),
                    line + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Fehler beim Schreiben der Logdatei: " + fileName, e);
        }
    }

}
