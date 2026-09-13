package com.eb.chatclient.app;

import com.eb.chatclient.domain.chat.AiChatManager;

import javax.swing.*;

public class AiPlaygroundApp {

    public static void main(String[] args) {

        AiChatManager.getCurrent();

        SwingUtilities.invokeLater(() -> {
            AiPlaygroundCtrl ctrl = new AiPlaygroundCtrl();
        });
    }
}
