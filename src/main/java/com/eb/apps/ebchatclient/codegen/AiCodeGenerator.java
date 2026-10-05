package com.eb.apps.ebchatclient.codegen;

import com.eb.base.extensions.FileExtensions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class AiCodeGenerator {
    private String namespace;
    private String targetDirectory;

    public AiCodeGenerator(String namespace, String targetDirectory) {
        this.namespace = namespace;
        this.targetDirectory = targetDirectory;
    }

    public void generateCodeDateien(String jsonInput)
    {
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
