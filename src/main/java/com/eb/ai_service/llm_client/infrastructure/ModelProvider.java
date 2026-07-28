package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.AiServiceConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ModelProvider {

    private List<LlmModel> models;
    private AiServiceConfig config;

    public List<LlmModel> getModels() {
        return models;
    };

    public ModelProvider()
    {
        try {
            models = readModels("d:\\Develop\\Visual22\\Shared\\AiPlayground\\Provider\\provider.txt");
        } catch (IOException e) {
            models = new ArrayList<>();
        }
    }



    private List<LlmModel> readModels(String fileName) throws IOException {

        List<LlmModel> models = new ArrayList<>();

        config = AiServiceConfig.current();

        List<String> lines = Files.readAllLines(Path.of(config.getModelFileName()));

        for (String line : lines) {
            line = line.trim();

            // Leere Zeilen überspringen
            if (line.isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\t");

            if (parts.length < 2) {
                throw new IllegalArgumentException(
                        "Ungültiges Format in Zeile: " + line);
            }

            LlmModel model = new LlmModel();
            model.setCompany(parts[0].trim());
            model.setModelName(parts[1].trim());
            models.add(model);
        }

        return models;
    }

    public void getAllModels() {
    }

    public LlmModel getModel(String modelName) {
        for (LlmModel model : models) {
            if (model.getModelName().equals(modelName)) {
                return model;
            }
        }
        return null;
    }
}