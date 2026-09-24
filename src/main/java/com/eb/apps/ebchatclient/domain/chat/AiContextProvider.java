/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.chat;

import com.eb.apps.ebchatclient.domain.context.domain.ContextManager;
import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;

import java.util.List;

public class AiContextProvider {

    public static final String AI_SERVICES_KONTEXTE_TXT = "AiServices/Kontexte_Java.txt";
    private static ContextManager contextManager;

    public static ContextManager getContextManager() {
        if (contextManager == null)
            contextManager = new ContextManager(AiEinstellungen.getFilePath(AI_SERVICES_KONTEXTE_TXT));
        return contextManager;
    }

    public static List<ContextWithFiles> getAvailableContexts() {

        ContextManager manager = AiChatManager.getCurrent().getContextManager();
        return manager.getContextList();
    }
}
