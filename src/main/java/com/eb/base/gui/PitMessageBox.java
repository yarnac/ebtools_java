package com.eb.base.gui;


import javax.swing.*;
import java.awt.*;

public class PitMessageBox {

    JFrame frame;
    JTextArea textArea;

    public static void show(String message) {
        PitMessageBox messageBox = new PitMessageBox();
        messageBox.initialize();
        messageBox.textArea.setText(message);
        messageBox.frame.setTitle("Hinweis");
        messageBox.frame.setVisible(true);
    }

    public static void show(String title, String message) {
        PitMessageBox messageBox = new PitMessageBox();
        messageBox.initialize();
        messageBox.textArea.setText(message);
        messageBox.frame.setTitle(title);
        messageBox.frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();

        frame.setTitle("Hinweis");
        frame.setBounds(100, 100, 615, 372);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GuiDecorator decorator = new GuiDecorator();
        frame.setIconImage(decorator.getImage(IC.BOOKS_RED));

        textArea = new JTextArea();

        frame.getContentPane().add(textArea, BorderLayout.CENTER);

    }
}
