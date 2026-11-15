package com.eb.apps.ebchat_utils.codegen;

import com.eb.base.extensions.FileExtensions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.logging.Logger;

public class AiCodeGenerator {
    private String namespace;
    private String targetDirectory;

    private static final Logger LOGGER = Logger.getLogger(AiCodeGenerator.class.getName());
    // Thread-sicher und singleton-artig kacheln wir den ObjectMapper
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public AiCodeGenerator(String namespace, String targetDirectory) {
        this.namespace = namespace;
        this.targetDirectory = targetDirectory;
    }


    public static void extractAndSaveFiles(String jsonText) throws IOException {
        if (jsonText == null || jsonText.isBlank()) {
            LOGGER.warning("Leerer oder null-JSON-Eingang.");
            return;
        }

        JsonNode root = MAPPER.readTree(jsonText);
        if (!root.isArray()) {
            throw new IllegalArgumentException("Erwartet wird ein JSON-Array, kein Objekt oder String.");
        }

        for (JsonNode item : root) {
            String fileName = item.path("fileName").asText();
            String content  = item.path("content").asText();

            if (fileName == null || fileName.isEmpty()) {
                LOGGER.warning("Überspringe Eintrag ohne 'fileName'.");
                continue;
            }

            Path filePath = Paths.get(fileName);

            // Erstelle Ordnerstruktur automatisch, falls nicht vorhanden
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            // Java 21: Files.writeString ist der moderne Ersatz für FileWriter/OutputStreamWriter
            Files.writeString(filePath, content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            LOGGER.info("Datei erfolgreich geschrieben: " + filePath);
        }
    }

    public void generateCodeDateien(String jsonInput)
    {
        try {
            extractAndSaveFiles(jsonInput);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<AiCodeGeneratorCodeDatei> dateien = getDateien(jsonInput);
        for (AiCodeGeneratorCodeDatei datei : dateien) {
            String fileName = FileExtensions.ebFullFileNameInDirectory(datei.getName(), targetDirectory);
            FileExtensions.ebWriteTextToFileUtf8(datei.getContent(), fileName);
        }
    }


    private List<AiCodeGeneratorCodeDatei> getDateien(String jsonInput)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<AiCodeGeneratorCodeDatei> codeFiles = objectMapper.readValue(
                    jsonInput,
                    new TypeReference<List<AiCodeGeneratorCodeDatei>>() {}
            );


            return codeFiles;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void setCodeText(String text) {

    }
}
