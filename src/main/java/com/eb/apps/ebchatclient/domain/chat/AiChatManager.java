package com.eb.apps.ebchatclient.domain.chat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eb.apps.ebchatclient.domain.context.domain.ContextManager;
import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;
import com.eb.base.ai_service.llm_client.infrastructure.LlmModelProvider;
import com.eb.base.extensions.FileExtensions;
import com.eb.base.inifile.api.IniFile;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class AiChatManager {

    private List<AiChat> availableChats = new ArrayList<>();
    private List<String> availableKategorien;
    private List<ContextWithFiles> availableEntwicklungsKontexte;
    private List<AiChat> availableSessions = new ArrayList<>();
    private final IniFileFactory iniFileFactory;
    private final ObjectMapper objectMapper;

    private static AiChatManager current;
    private ContextManager contextManager;

    public static AiChatManager getCurrent()
    {
        if (current == null)
            current = new AiChatManager();
        return current;
    }

    public AiChatManager() {
        this(new DefaultIniFileFactory(), createMapper());
        contextManager = AiContextProvider.getContextManager();
    }

    public AiChatManager(IniFileFactory iniFileFactory, ObjectMapper objectMapper) {
        this.iniFileFactory = iniFileFactory;
        this.objectMapper = objectMapper;
        contextManager = AiContextProvider.getContextManager();

        loadChats();
        loadSessions();
        availableKategorien = contextManager.getKnoten();
    }

    public ContextManager getContextManager() {
        return contextManager;
    }

    public LlmModelProvider getLlmModelProvider() {
        return LlmModelProvider.getCurrent();
    }


    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        mapper.registerModule(new JavaTimeModule());

        return mapper;
    }

    private void loadChats() {
        // placeholder: you need your folder helper like EbGetAllFiles
        List<String> iniFiles = FileHelpers.getAllFiles(GetChatOrdner(), "chat.ini");
        this.availableChats = iniFiles.stream().map(this::readChat)
                .collect(Collectors.toList());
    }

    private void loadSessions() {
        List<String> iniFiles = FileHelpers.getAllFiles(GetSessionOrdner(), "chat.ini");
        this.availableSessions = iniFiles.stream().map(this::readChat)
                .collect(Collectors.toList());
    }

    private static String GetChatOrdner() {
        return AiEinstellungen.AiPfad + "/chats";
    }

    private static String GetSessionOrdner() {
        return AiEinstellungen.AiPfad + "/sessions";
    }

    public AiChat createChat(String name, String title) {
        AiChat chat = new AiChat(name, title);
        // Directory.CreateDirectory(chat.ChunkPath);
        FileHelpers.ensureDirectory(chat.getChunkPath());

        IniFile iniFile = iniFileFactory.create(FileExtensions.ebFileNameInDirectory("chat.ini",chat.getFilePath()));
        chat.setMyIniFile(iniFile);
        chat.store();
        availableChats.add(chat);
        return chat;
    }

    public AiChat readChat(String name) {
        AiChat chat = readChatIniFile(name);
        readChatMessages(chat);
        return chat;
    }

    private AiChat readChatIniFile(String name) {
        if (!Files.exists(Paths.get(name))) {
            throw new IllegalArgumentException(name + " existiert nicht");
        }
        IniFile ini = iniFileFactory.create(name);
        return new AiChat(ini);
    }

    private List<AiChatMessage> readChatMessages(AiChat chat) {
        String filePath = getChatFilePath(chat, "message.json");
        if (!Files.exists(Paths.get(filePath))) {
            chat.setMessages(new ArrayList<>());
            return chat.getMessages();
        }

        try {
            String json = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
            // In C#: AiChatSerializer.DeserializeAiChatMessages(json)
            // Here we assume direct mapping to AiChatMessage list.
            CollectionType type = objectMapper.getTypeFactory().constructCollectionType(List.class, AiChatMessage.class);
            List<AiChatMessage> result = objectMapper.readValue(json, type);
            chat.setMessages(result);
            if (result.isEmpty() || result.get(0).getVersion() == null) {
                // Deprecated conversion omitted for brevity.
            }
            return result;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public void writeChatMessages(AiChat chat) throws IOException {
        for (AiChatMessage message : chat.getMessages()) {
            message.setVersion("1.0");
        }
        String json = AiChatSerializer.serializeAiChatMessages(chat.getMessages());
        String filePath = getChatFilePath(chat, "message.json");
        FileHelpers.ensureDirectory(FileExtensions.ebFileDirectory(filePath));
        Files.writeString(Paths.get(filePath), json, StandardCharsets.UTF_8);
    }

    private String getChatFilePath(AiChat chat, String fileName) {
        String directory = FileExtensions.ebFileDirectory(chat.getMyIniFile().getFileName());
        return directory + "\\" + fileName;
    }

    public AiChat getChat(String v1, String v2) {
        return availableChats.stream().filter(c -> v1.equals(c.getName())).findFirst().orElseGet(() -> {
            AiChat chat = new AiChat(v1, v2);
            chat.setMyIniFile(iniFileFactory.create(getChatFilePath(chat, "chat.ini")));
            return chat;
        });
    }

    public AiChat getChatWithFileName(String chatFile) {
        AiChat chat = availableChats.stream()
                .filter(x -> x.getMyIniFile() != null && chatFile.equals(x.getMyIniFile().getFileName()))
                .findFirst().orElse(null);

        if (chat == null) {
            chat = availableChats.stream()
                    .filter(x -> x.getFilePath() != null && x.getFilePath().equalsIgnoreCase(chatFile))
                    .findFirst().orElse(null);
        }
        return chat;
    }

    public List<AiChat> getAvailableChats() {
        return availableChats;
    }
}
