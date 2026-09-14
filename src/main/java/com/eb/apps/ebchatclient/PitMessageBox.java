package com.eb.apps.ebchatclient;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.apps.ebchatclient.clients.CheckNetworkService;
import com.eb.apps.ebchatclient.clients.OllamaClientDeprecated;
import com.eb.apps.ebchatclient.domain.LlmRequestDeprecated;
import com.eb.apps.ebchatclient.domain.LlmResponseDeprecated;


import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class PitMessageBox {

    JFrame frmWrterbuch;
    JTextArea textArea;

    public static void main(String[] args) {
        // String API_KEY = System.getenv("API_KEY_OPENAI");
        String API_KEY = System.getenv("API_KEY_ANTHROPIC");
        OllamaClientDeprecated client = new OllamaClientDeprecated();
        LlmRequestDeprecated request = new LlmRequestDeprecated();
        request.setModel("qwen3:14b");
        request.addSystemMessage("Du bist ein freundlicher AI Agent");
        request.addUserMessage("Sag Hallo");

        CheckNetworkService checkNetworkService = new CheckNetworkService();
        checkNetworkService.checkIsAvailable("macbook-air-von-ekkart");

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    LlmResponseDeprecated response = client.sendRequest(request);
                    PitMessageBox.show(response.getAnswer());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void show(String message) {
        PitMessageBox messageBox = new PitMessageBox();
        messageBox.initialize();
        messageBox.textArea.setText(message);
        messageBox.frmWrterbuch.setTitle("Hinweis");
        messageBox.frmWrterbuch.setVisible(true);
    }

    private void initialize() {
        frmWrterbuch = new JFrame();

        frmWrterbuch.setTitle("W\u00F6rterbuch");
        frmWrterbuch.setBounds(100, 100, 615, 372);
        frmWrterbuch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GuiDecorator decorator = new GuiDecorator();
        frmWrterbuch.setIconImage(decorator.getImage(IC.BOOKS_RED));


        JMenuBar menuBar = new JMenuBar();
        frmWrterbuch.setJMenuBar(menuBar);

        textArea = new JTextArea();

        frmWrterbuch.getContentPane().add(textArea, BorderLayout.CENTER);

    }
}
