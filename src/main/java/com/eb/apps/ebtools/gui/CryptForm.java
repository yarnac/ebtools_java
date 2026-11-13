package com.eb.apps.ebtools.gui;

import com.eb.base.gui.EbSplitPanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class CryptForm extends JFrame {

    private JTextField edPassword;
    private JTextArea edInput;
    private JTextArea edOutput;
    private JScrollPane scrollPaneInput;
    private JScrollPane scrollPaneOutput;

    public static void main(String[] args) {
        CryptForm cryptForm = new CryptForm();


        SwingUtilities.invokeLater(() -> {
            cryptForm.setVisible(true);
        });
    }

    public CryptForm() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        initializeView();
    }

    private void initializeView() {
        EbSplitPanel splitPanel = new EbSplitPanel("MainPanel", getInputScrollPane(), getOutputScrollPane(), 1);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(getEdPassword(), BorderLayout.NORTH);
        mainPanel.add(splitPanel, BorderLayout.CENTER);

        add(new JToolBar(), BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
    }

    public JTextField getEdPassword() {
        if (edPassword == null) {
            edPassword = new JTextField();
        }
        return edPassword;
    }

    public JScrollPane getInputScrollPane() {
        if (scrollPaneInput==null)
        {
            scrollPaneInput = new JScrollPane(getEdInput());
        }
        return scrollPaneInput;
    }

    public JTextArea getEdInput() {
        if (edInput==null)
        {
            edInput = new JTextArea();
        }
        return edInput;
    }

    public JScrollPane getOutputScrollPane() {
        if (scrollPaneOutput==null)
        {
            scrollPaneOutput = new JScrollPane(getEdOutput());
        }
        return scrollPaneOutput;
    }

    public JTextArea getEdOutput() {
        if (edOutput==null)
        {
            edOutput = new JTextArea();
        }
        return edOutput;
    }

}
