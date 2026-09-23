/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;

import java.util.List;

public class AiContextProvider {

    public static List<AiChatContext> getAvailableContexts() {

        if (true)
            return AiChatManager.getCurrent().getAvailableContexts();

        List<AiChatContext> contexts = List.of(
                new AiChatContext(
                        "Java 21 und Hibernate 7",
                        "Entwicklung/Java",
                        "Du bist ein Java-Entwickler.",
                        "Erstelle eine Hibernate-Entity."
                ),
                new AiChatContext(
                        "Java 21 GUI Entwicklung",
                        "Entwicklung/Java",
                        "Du bist ein Java-Swing-Experte.",
                        "Erstelle einen Dialog mit JSplitPane."
                ),
                new AiChatContext(
                        "C# und Datenbanken",
                        "Entwicklung/C#",
                        "Du bist C# Entwickler mit .NET 9.",
                        "Programmiere einen einfachen HTTP Client."
                ),

                new AiChatContext(
                        "C# und Datenbanken",
                        "Entwicklung/C#/GUI",
                        "Du bist C# Entwickler mit .NET 9.",
                        "Programmiere einen einfachen HTTP Client."
                )
        );

        return contexts;
    }
}
