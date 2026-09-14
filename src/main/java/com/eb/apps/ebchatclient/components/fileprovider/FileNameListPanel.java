package com.eb.apps.ebchatclient.components.fileprovider;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.function.Consumer;

class FileNameListPanel extends JPanel {
    private FileNameListModel listModel;
    private JList<String> fileNameList;
    private Consumer<List<String>> listConsumer;
    private Runnable closeAction;

    public FileNameListPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header
        JLabel headerLabel = new JLabel("Dateinamen (Drag & Drop zum Hinzufügen)");
        headerLabel.setFont(headerLabel.getFont().deriveFont(Font.BOLD, 14f));
        add(headerLabel, BorderLayout.NORTH);

        // List Model und JList
        listModel = new FileNameListModel();
        fileNameList = new JList<>(listModel);
        fileNameList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        fileNameList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(fileNameList);
        add(scrollPane, BorderLayout.CENTER);

        // Drag and Drop Support
        new FileNameDropTarget(fileNameList, listModel);

        // Button Panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));

        JButton clearButton = new JButton("Löschen");
        clearButton.addActionListener(e -> {
            int[] selectedIndices = fileNameList.getSelectedIndices();
            for (int i = selectedIndices.length - 1; i >= 0; i--) {
                listModel.removeElementAt(selectedIndices[i]);
            }
        });

        JButton clearAllButton = new JButton("Alle löschen");
        clearAllButton.addActionListener(e -> listModel.clear());

        JButton okButton = new JButton("Ok");
        okButton.addActionListener(e -> okButtonClicked());

        JButton abbrechenButton = new JButton("Abbrechen");
        abbrechenButton.addActionListener(e -> {
            if (closeAction != null) {
                closeAction.run();
            }
        });


        panel.add(clearButton);
        panel.add(clearAllButton);
        panel.add(okButton);
        panel.add(abbrechenButton);

        return panel;
    }

    private void okButtonClicked() {
        if (listConsumer != null) {
            listConsumer.accept(listModel.getFileNames());
        }
    }

    public void addListConsumer(Consumer<List<String>> consumer) {
        listConsumer = consumer;
    }

    public void addCloseAction(Runnable action) {
        closeAction = action;
    }
}
