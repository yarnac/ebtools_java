package com.eb.base.gui;

import com.eb.apps.ebchatclient.domain.context.gui.ContextEditorPanel;

import javax.swing.*;
import java.awt.*;

public class EbGridBagUtil {
    private Container currentContainer;
    int currentRow = 0;

    public EbGridBagUtil(Container container, int row) {
        currentContainer = container;
        currentRow = row;
    }

    public JToolBar addToolBarRow(boolean floatable) {
        GridBagConstraints gbc = new GridBagConstraints();

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(floatable);

        gbc.gridx = 0;
        gbc.gridy = currentRow;

        // Die Toolbar belegt beide Spalten.
        gbc.gridwidth = 2;

        // Toolbar soll die volle Breite erhalten.
        gbc.weightx = 1.0;
        gbc.weighty = 0.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(12, 4, 4, 0);

        currentContainer.add(toolBar, gbc);
        currentRow++;

        return toolBar;
    }

    public static void addLabel(Container container, String text, int row) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        gbc.weighty = 0.0;

        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.fill = GridBagConstraints.NONE;

        gbc.insets = new Insets(4, 4, 1, 1);

        container.add(new JLabel(text), gbc);
    }

    public <T extends JComponent> T addLabeledFieldRow(T edName, String fieldLabel, double weight, int constraints) {
        addLabel(currentContainer, fieldLabel, currentRow);
        addField(currentContainer, edName, currentRow, weight, constraints);
        currentRow++;
        return edName;
    }

    public <T extends JComponent> T addLabeledScrollPaneWithFieldRow(T edName, String fieldLabel, double weight, int constraints) {

        JScrollPane scrollPaneWithField = new JScrollPane(edName);

        addLabel(currentContainer, fieldLabel, currentRow);
        addField(currentContainer, scrollPaneWithField, currentRow, weight, constraints);
        currentRow++;
        return edName;
    }



    public static void addField(Container container, JComponent component, int row, double weighty, int fill) {
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 1;
        gbc.gridy = row;

        // Die rechte Spalte erhält die zusätzliche Breite.
        gbc.weightx = 1.0;

        // Nur der User Prompt erhält zusätzliche Höhe.
        gbc.weighty = weighty;

        gbc.fill = fill;
        gbc.anchor = GridBagConstraints.NORTHWEST;

        gbc.insets = new Insets(4, 8, 1, 2);

        container.add(component, gbc);
    }

    public void setCurrentContainer(ContextEditorPanel contextEditorPanel) {
        currentContainer = contextEditorPanel;
    }

    public void setCurrentRow(int i) {
        currentRow = i;
    }
}
