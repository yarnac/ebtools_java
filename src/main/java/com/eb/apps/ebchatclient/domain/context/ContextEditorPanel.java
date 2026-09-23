/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebookreader.tobj.StringUtil;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public final class ContextEditorPanel extends JPanel {

    private final JTextField edName = new JTextField();
    private final JTextField edKnoten = new JTextField();

    private final JTextArea edPrompt = createTextArea();
    private final JTextArea edUserPrompt = createTextArea();

    @Getter     @Setter
    private JToolBar toolBar;
    private AiChatContext actContext;
    private Runnable saveListener;

    public ContextEditorPanel() {
        super(new GridBagLayout());

        initializeComponents();
    }

    private void initializeComponents() {
        setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));

        toolBar = new JToolBar();
        toolBar.setFloatable(false);


        JScrollPane systemPromptScroll = new JScrollPane(edPrompt);
        JScrollPane userPromptScroll = new JScrollPane(edUserPrompt);

        // Die bevorzugte Höhe des System-Prompts.
        systemPromptScroll.setPreferredSize(new Dimension(300, 80));

        int row = 0;
        addToolBar(toolBar, row++);

        addLabel("Name", row);
        addField(edName, row++, 0.0, GridBagConstraints.HORIZONTAL);

        addLabel("Knoten", row);
        addField(edKnoten, row++, 0.0, GridBagConstraints.HORIZONTAL);

        addLabel("Prompt", row);
        addField(systemPromptScroll, row++, 1.0, GridBagConstraints.BOTH);
    }

    public void undo()
    {
        transferModelToView(actContext);
    }

    public void transferModelToView(AiChatContext aiChatContext) {
        this.actContext = aiChatContext == null ? new AiChatContext() : aiChatContext;
        edName.setText(actContext.getName());
        edKnoten.setText(actContext.getKnoten());

        StringBuilder strb = new StringBuilder();
        if (!StringUtil.isNullOrEmpty(actContext.getSystemPrompt())) {
            strb.append("<<" + actContext.getSystemPrompt() + ">>\n\n");
        }
        if (!StringUtil.isNullOrEmpty(actContext.getUserPrompt())) {
            strb.append(actContext.getUserPrompt());
        }
        edPrompt.setText(strb.toString());
    }

    public void transferViewToModel(AiChatContext aiChatContext) {
        aiChatContext.setName(edName.getText());
        aiChatContext.setKnoten(edKnoten.getText());

        String prompt = edPrompt.getText().trim();
        String userPrompt = prompt;
        if (prompt.startsWith("<<")) {
            int index = prompt.indexOf(">>");
            aiChatContext.setSystemPrompt(prompt.substring(2, index));
            userPrompt = prompt.substring(index + 2).trim();
        }
        aiChatContext.setUserPrompt(userPrompt);
    }
    



    private void addToolBar(JToolBar toolBar, int row) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = row;

        // Die Toolbar belegt beide Spalten.
        gbc.gridwidth = 2;

        // Toolbar soll die volle Breite erhalten.
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(0, 0, 4, 0);

        add(toolBar, gbc);
    }

    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
    }

    private void addLabel(String text, int row) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        gbc.insets = new Insets(6, 0, 1, 1);

        add(new JLabel(text), gbc);
    }

    private void addField(Component component, int row, double weighty, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = row;

        // Die rechte Spalte erhält die zusätzliche Breite.
        gbc.weightx = 1.0;

        // Nur der User Prompt erhält zusätzliche Höhe.
        gbc.weighty = weighty;

        gbc.fill = fill;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.insets = new Insets(1, 2, 1, 2);

        add(component, gbc);
    }

    public JTextField getEdName() {
        return edName;
    }

    public JTextField getEdKnoten() {
        return edKnoten;
    }

    public JTextArea getEdPrompt() {
        return edPrompt;
    }

    public void setSaveListener(Runnable listener){
        saveListener = listener;
    }

    public void transferViewToModel() {
        if (actContext != null) {
            transferViewToModel(actContext);
        }
    }

    public String getPromptText() {
        return edPrompt.getText();
    }
}