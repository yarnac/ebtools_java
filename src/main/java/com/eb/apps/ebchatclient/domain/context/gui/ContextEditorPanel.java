/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context.gui;

import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;
import com.eb.base.gui.EbGridBagUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public final class ContextEditorPanel extends JPanel {

    private JTextField edName;
    private JTextField edKnoten;

    private JTextArea edPrompt;
    private JTextArea edOutput;

    @Getter     @Setter
    private JToolBar toolBar;
    private ContextWithFiles actContext;
    private Runnable saveListener;

    public ContextEditorPanel() {
        super(new GridBagLayout());

        initializeComponents();
    }

    private void initializeComponents() {
        setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));


        JScrollPane systemPromptScroll = new JScrollPane(edPrompt);
        JScrollPane outputPromptScroll = new JScrollPane(edOutput);

        // Die bevorzugte Höhe des System-Prompts.
        systemPromptScroll.setPreferredSize(new Dimension(300, 80));


        EbGridBagUtil util = new EbGridBagUtil(this, 0);

        toolBar = util.addToolBarRow(false);
        edName = util.addLabeledFieldRow(new JTextField(), "Name", 0.0, GridBagConstraints.HORIZONTAL);
        edKnoten = util.addLabeledFieldRow(new JTextField(), "Knoten", 0.0, GridBagConstraints.HORIZONTAL);
        edPrompt = util.addLabeledScrollPaneWithFieldRow(new JTextArea(), "Prompt", 0.4, GridBagConstraints.BOTH);
        edOutput = util.addLabeledScrollPaneWithFieldRow(new JTextArea(), "Output", 0.6, GridBagConstraints.BOTH);
    }

    public void undo()
    {
        transferModelToView(actContext);
    }

    public void transferModelToView(ContextWithFiles contextWithFiles) {
        this.actContext = contextWithFiles == null ? new ContextWithFiles() : contextWithFiles;
        edName.setText(actContext.getName());
        edKnoten.setText(actContext.getKnoten());
        edPrompt.setText(actContext.getUserString());
    }

    public void transferViewToModel(ContextWithFiles contextWithFiles) {
        contextWithFiles.setName(edName.getText());
        contextWithFiles.setKnoten(edKnoten.getText());
        contextWithFiles.setUserString(edPrompt.getText());
    }


    private JTextArea createTextArea() {
        JTextArea textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        return textArea;
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

    public JTextArea getEdOutput() {
        return edOutput;
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