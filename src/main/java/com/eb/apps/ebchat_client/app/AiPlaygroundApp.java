package com.eb.apps.ebchat_client.app;

import com.eb.apps.ebchat_client.domain.chat.AiChatManager;

import javax.swing.*;

public class AiPlaygroundApp {

    public static void main(String[] args) {

        AiChatManager.getCurrent();

        SwingUtilities.invokeLater(() -> {
            AiPlaygroundCtrl ctrl = new AiPlaygroundCtrl();
        });
    }
}
