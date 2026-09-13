package com.eb.chatclient.components.fileprovider;

import javax.swing.*;
import java.awt.*;

import java.util.List;
import java.util.function.Consumer;

public class GuiFileNameProvider extends JFrame {
    private FileNameListPanel fileNameListPanel;
    private Consumer<List<String>> listConsumer;

    public GuiFileNameProvider() {
        setTitle("File Name Provider");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        setResizable(true);

        fileNameListPanel = new FileNameListPanel();
        add(fileNameListPanel, BorderLayout.CENTER);

        fileNameListPanel.addCloseAction(()->this.dispose());
    }

    public void addListConsumer(Consumer<List<String>> consumer) {
        fileNameListPanel.addListConsumer(consumer);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GuiFileNameProvider frame = new GuiFileNameProvider();
            frame.setVisible(true);
        });
    }
}
