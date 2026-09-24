package com.eb.apps.ebchatclient.domain.chat;

import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;

import java.util.List;

public interface IAiChatContextRepository {

    List<ContextWithFiles> getAllContexts();
    ContextWithFiles getContext(String name);
    ContextWithFiles createContext(String name);
    void deleteContext(String name);
    void saveContext(ContextWithFiles context);
}
