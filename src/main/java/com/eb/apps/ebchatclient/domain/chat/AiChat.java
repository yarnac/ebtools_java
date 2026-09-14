package com.eb.apps.ebchatclient.domain.chat;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.inifile.api.IniFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AiChat {
    private String name;
    private String title;
    private List<AiChatMessage> messages;

    private String model;
    private String url;
    private AiChatContext context;

    private AiChatContext loadedContext;
    private IniFile myIniFile;

    private double preis;

    // runtime only / mapped from ini in C# version
    private String contextName;

    public AiChat() {
        this.messages = new ArrayList<>();
    }

    public AiChat(IniFile iniFile) {

        this.messages = new ArrayList<>();
        myIniFile = iniFile;
        myIniFile.Read();
        title = myIniFile.getSectionValue("Einstellungen", "title", "unknown");
        name = myIniFile.getSectionValue("Einstellungen", "name", "unknown");
    }

    public AiChat(String name, String title) {
        this.name = name;
        this.title = title;
        this.messages = new ArrayList<>();
    }

    public String getName() { return name; }
    public void setName(String name) { 
        this.name = name; 
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public AiChatContext getContext() { return context; }
    public void setContext(AiChatContext context) { this.context = context; }

    public AiChatContext getLoadedContext() { return loadedContext; }
    public void setLoadedContext(AiChatContext loadedContext) { this.loadedContext = loadedContext; }

    public String getContextName() { return contextName; }
    public void setContextName(String contextName) { this.contextName = contextName; }

    public IniFile getMyIniFile() { return myIniFile; }
    public void setMyIniFile(IniFile myIniFile) { 
        this.myIniFile = myIniFile; 
    }

    public List<AiChatMessage> getMessages() { 
        if (messages == null) messages = new ArrayList<>();
        return messages; 
    }

    public void setMessages(List<AiChatMessage> messages) {
        this.messages = messages;
        if (this.messages != null) {
            this.preis = 0.0;
            int i = 1;
            for (AiChatMessage message : this.messages) {
                if (message != null) {
                    this.preis += Kosten.getPreisEuroMwst(message);
                    message.setId(i++);
                }
            }
        }
    }

    public double getPreis() { return preis; }
    public void setPreis(double preis) { this.preis = preis; }

    // equivalent to FilePath logic in C#
    public String getFilePath() {

        if (myIniFile != null && myIniFile.getFileName() != null) {
            return FileExtensions.ebDirectoryName(myIniFile.getFileName());
        }
        return AiEinstellungen.AiPfad + "\\Chats\\" + name;
    }

    public String getChunkPath() {
        return AiEinstellungen.AiPfad + "\\Chats\\" + name + "\\chunks";
    }

    public String getMessageFile() {
        return FileExtensions.ebFileNameInDirectory("message.json", getFilePath());
    }

    @Override
    public String toString() {

        return title;
    }

    public int getTotalTokens() {
        return getSum(AiChatMessage::getTotalTokens);
    }

    public int getInputTokens() {
        return getSum(AiChatMessage::getInputTokens);
    }

    public int getOutputTokens() {
        return getSum(AiChatMessage::getOutputTokens);
    }

    private int getSum(java.util.function.Function<AiChatMessage, Integer> getValue) {
        int sum = 0;
        for (AiChatMessage message : getMessages()) {
            if (message != null) sum += Objects.requireNonNullElse(getValue.apply(message), 0);
        }
        return sum;
    }

    public void store() {
        if (myIniFile == null) return;
        myIniFile.setSectionValue("Einstellungen", "Name", name);
        myIniFile.setSectionValue("Einstellungen", "Title", title);
        myIniFile.setSectionValue("Einstellungen", "Model", model);
        myIniFile.setSectionValue("Einstellungen", "Url", url);
        myIniFile.setSectionValue("Einstellungen", "Context", contextName);
        myIniFile.Write();
    }

    public void addMessage(AiChatMessage message) {
        if (message == null) return;
        if (message.getId() == 0) {
            List<AiChatMessage> list = getMessages();
            if (!list.isEmpty() && list.get(list.size() - 1) != null) {
                message.setId(list.get(list.size() - 1).getId() + 1);
            } else {
                message.setId(1);
            }
        }
        getMessages().add(message);
        this.preis += Kosten.getPreisEuroMwst(message);
    }

    public void clearMessages() {
        this.preis = 0.0;
        getMessages().clear();
    }

    public AiChatMessage getLastMessage() {
        List<AiChatMessage> list = getMessages();
        if (list.isEmpty()) return null;
        return list.get(list.size() - 1);
    }
}
