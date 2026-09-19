package com.eb.apps.ebchatclient.domain.chat;

import java.util.List;

public interface IAiChatContextRepository {

    List<AiChatContext> getAllContexts();
    AiChatContext getContext(String name);
    AiChatContext createContext(String name);
    void deleteContext(String name);
    void saveContext(AiChatContext context);
}
