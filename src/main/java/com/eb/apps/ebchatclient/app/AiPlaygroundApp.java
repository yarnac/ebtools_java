package com.eb.apps.ebchatclient.app;

import com.eb.apps.ebchatclient.domain.chat.AiChatManager;

import javax.swing.*;

public class AiPlaygroundApp {

    public static void main(String[] args) {

        AiChatManager.getCurrent();

        SwingUtilities.invokeLater(() -> {
            AiPlaygroundCtrl ctrl = new AiPlaygroundCtrl();
        });
    }
}
