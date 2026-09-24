package com.eb.apps.ebchatclient.domain.context.domain;

import com.eb.apps.ebchatclient.domain.chat.AiEinstellungen;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ContextManager {

    private final ObjectMapper objectMapper;
    private final String contextFilePath;
    private List<ContextWithFiles> availableKontexte = new ArrayList<>();

    public List<String> getRootKnoten()
    {
        return availableKontexte
                .stream()
                .map(ContextWithFiles::getKnoten)
                .collect(Collectors.toList());

    }

    public ContextManager(String filePath) {
        objectMapper = createMapper();
        contextFilePath = filePath;
        readKontexte();
    }

    public void writeKontexte() throws IOException {
        String json = objectMapper.writeValueAsString(availableKontexte);
        Files.writeString(Paths.get(AiEinstellungen.getFilePath(contextFilePath)), json, StandardCharsets.UTF_8);
    }

    public void readKontexte() {
        try {
            String filePath = contextFilePath;
            String json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, ContextWithFiles.class);
            this.availableKontexte = objectMapper.readValue(json, type);
        } catch (Exception e) {
            this.availableKontexte = new ArrayList<>();
        }
    }

    public List<String> getKnoten() {
        return availableKontexte
                .stream()
                .map(ContextWithFiles::getKnoten)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
    }

    public ContextWithFiles getContext(String contextName) {
        if (contextName == null || contextName.isEmpty()) {
            return null;
        }
        return availableKontexte
                .stream()
                .filter(x -> contextName.equals(x.getName()))
                .findFirst()
                .orElse(null);
    }

    public List<ContextWithFiles> getContextList() {
        return availableKontexte;
    }

    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }
}
