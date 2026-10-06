package com.eb.base.ai_service.llmreqstore;

import com.eb.apps.ebchatclient.domain.chat.AiChatMessage;
import com.eb.apps.ebchatclient.domain.chat.AiEinstellungen;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.extensions.FileExtensions;
import com.eb.base.io.FileUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class LlmRequestManager {
    private final List<LlmRequestStoreItem> llmRequestStoreItemList = new ArrayList<>();
    private final Map<Long, LlmRequestStoreItem> llmRequestStoreItemsMap = new HashMap<>();

    public LlmRequestManager() {
        loadRequests();
    }

    public List<LlmRequest> getLlmRequests() {
        return llmRequestStoreItemList.stream().map(x->x.getLlmRequest()).toList();
    }

    public LlmRequest getLlmRequest(Long id) {
        LlmRequestStoreItem llmRequestStoreItem = llmRequestStoreItemsMap.get(id);
        if (llmRequestStoreItem == null) {
            return null;
        }
        return llmRequestStoreItem.getLlmRequest();
    }

    public void store(LlmRequest request)
    {
        LlmRequestStoreItem llmRequestStoreItem = findLlmRequestStoreItem(request);
        if (llmRequestStoreItem == null) {
            storeNewRequest(request);
            return;
        }
        if (llmRequestStoreItem.getLlmRequest()==request)
        {
            llmRequestStoreItem.setLlmResponse(request.getLastResponse());
        }
        else
        {
            // Combine new and old Requests
        }
        storeRequestStoreItem(llmRequestStoreItem);
    }

    private LlmRequestStoreItem findLlmRequestStoreItem(LlmRequest request) {
        if (request.getRequestId() == null)
            return null;

        return llmRequestStoreItemsMap.get(request.getRequestId());
    }

    private void storeNewRequest(LlmRequest request) {
        LlmRequestStoreItem llmRequestStoreItem = new LlmRequestStoreItem();
        request.setRequestId(new Date().getTime());
        llmRequestStoreItem.setLlmRequest(request);
        llmRequestStoreItem.setLlmResponse(request.getLastResponse());

        storeRequestStoreItem(llmRequestStoreItem);

        llmRequestStoreItemList.add(llmRequestStoreItem);
        llmRequestStoreItemsMap.put(request.getRequestId(), llmRequestStoreItem);
    }

    private void storeRequestStoreItem(LlmRequestStoreItem llmRequestStoreItem) {
        String storeFileName = "Request_" + llmRequestStoreItem.getLlmRequest().getRequestId() + ".json";
        String storeFilePathName = FileExtensions.ebFileNameInDirectory(storeFileName, GetRequestsOrdner());

        try {
            String jsonString;
            jsonString = createMapper().writeValueAsString(llmRequestStoreItem);
            FileUtil.ensureDirectory(GetRequestsOrdner());
            Files.writeString(Paths.get(storeFilePathName), jsonString, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private static String GetRequestsOrdner() {
        return AiEinstellungen.AiPfad + "/requests";
    }

    private void loadRequests() {
        ObjectMapper objectMapper = createMapper();
        llmRequestStoreItemList.clear();
        llmRequestStoreItemsMap.clear();
        String[] fileNames = FileExtensions.ebGetFiles(GetRequestsOrdner(), "Request_*.json");
        llmRequestStoreItemList.addAll(Arrays.stream(fileNames)
                .map(x->readItem(x, objectMapper) )
                .toList());
        for(LlmRequestStoreItem llmRequestStoreItem : llmRequestStoreItemList)
        {
            llmRequestStoreItemsMap.put(llmRequestStoreItem.getRequestId(), llmRequestStoreItem);
        }
    }

    private static LlmRequestStoreItem readItem(String filePath, ObjectMapper objectMapper) {
        try {
            String json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            // In C#: AiChatSerializer.DeserializeAiChatMessages(json)
            // Here we assume direct mapping to AiChatMessage list.

            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, AiChatMessage.class);
            LlmRequestStoreItem result = objectMapper.readValue(json, LlmRequestStoreItem.class);
            return result;
        } catch (Exception e) {
            return null;
        }
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
