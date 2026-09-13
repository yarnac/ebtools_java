package com.eb.chatclient.app;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.chatclient.EbSplitPanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AiPlaygroundWindow extends JFrame {

    @Getter
    private final GuiDecorator decorator;
    @Getter
    private JTextPane textPane1;
    @Getter
    private JTextPane textPaneInput;
    @Getter
    private JTextPane textPaneOutput;

    EbSplitPanel splitPanelMain;
    private EbSplitPanel splitPanelMessages;
    private IniFile myIniFile;
    private JToolBar toolBar;

    private PanelWithToolBar panelWithToolBarAuswahl;
    private PanelWithToolBar panelWithToolBarInput;
    private PanelWithToolBar panelWithToolBarOutput;

    public AiPlaygroundWindow()
    {
        myIniFile = IniFileProvider.createIniFile("SplitWindow.ini");
        initializeView();

        setFontSizes(16);


        decorator = new GuiDecorator(this, myIniFile,"Einstellungen");

        decorateMainToolbar();
        decorateMessagesAuswahl();
        decorateMessagesOutput();
    }

    private void setFontSizes(int fontSize) {
        Font font = new Font("Arial", Font.PLAIN, fontSize);
        getTextPane1().setFont(new Font("Arial", Font.PLAIN, fontSize));
        getTextPaneInput().setFont(new Font("Arial", Font.PLAIN, fontSize));
        getTextPaneOutput().setFont(new Font("Arial", Font.PLAIN, fontSize));
    }

    private void decorateMessagesInput() {
        decorator.addToolbarButton(splitPanelMessages.getToolBar1Name(), "Speichern Input", IC.MB_PLAY, (x)->{});
    }

    private void decorateMessagesOutput() {
        decorator.addToolbarButton(splitPanelMessages.getToolBar2Name(), "Speichern Output", IC.MB_PLAY, (x)->{});
    }

    private void decorateMessagesAuswahl() {
        decorator.addToolbarButton(splitPanelMain.getToolBar1Name(),"Speichern Auswahl", IC.MB_PLAY, (x)->{});
    }

    private void decorateMainToolbar() {
        decorator.addToolbarButton("MainToolbar", "Speichern Main", IC.MB_PLAY, (x)->{});
    }

    public void setOutputText(String answer) {
        textPaneOutput.setText(answer);

    }

    public String getInputString() {
        return textPaneInput.getText();
    }

    public void setInputText(String inputString2) {
        textPaneInput.setText(inputString2);
    }

    public void setInOutEnabled(boolean b) {
        textPaneInput.setEnabled(b);
        textPaneOutput.setEnabled(b);
        splitPanelMessages.getToolBar1().setEnabled(b);
        splitPanelMessages.getToolBar2().setEnabled(b);
        splitPanelMain.getToolBar1().setEnabled(b);
        splitPanelMain.getToolBar2().setEnabled(b);
    }

    public class PanelWithToolBar {
        @Getter
        private JPanel panel;
        @Getter
        private JToolBar toolbar;
        public PanelWithToolBar(JPanel panel, JToolBar toolbar)
        {
            this.panel = panel;
            this.toolbar = toolbar;
        }
    }

    public PanelWithToolBar getPanelWithToolBarAuswahl()
    {
        if (panelWithToolBarAuswahl == null)
            panelWithToolBarAuswahl = new PanelWithToolBar(splitPanelMain.getPanel1(), splitPanelMain.getToolBar1());
        return panelWithToolBarAuswahl;
    }

    public PanelWithToolBar getPanelWithToolBarInput()
    {
        if (panelWithToolBarInput == null)
            panelWithToolBarInput = new PanelWithToolBar(splitPanelMessages.getPanel1(), splitPanelMessages.getToolBar1());
        return panelWithToolBarInput;
    }

    public PanelWithToolBar getPanelWithToolBarOutput()
    {
        if (panelWithToolBarOutput == null)
            panelWithToolBarOutput = new PanelWithToolBar(splitPanelMessages.getPanel1(), splitPanelMessages.getToolBar1());
        return panelWithToolBarOutput;
    }

    private void initializeView() {
        // Set frame properties
        setTitle("Split Window Example");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window




        createAndAddSplitPanelMain();
        createAndAddMenuBar();
        createAndAddMainToolBar();
    }

    private void createAndAddMainToolBar() {
        toolBar = new JToolBar();
        toolBar.setName("MainToolbar");
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);
    }

    private void createAndAddMenuBar() {
        // Create menu and toolbar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Datei");

        JMenuItem saveMenuItem = new JMenuItem("Speichern");
        saveMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createAndAddSplitPanelMain() {
        textPane1 = new JTextPane();
        textPaneInput = new JTextPane();
        textPaneOutput = new JTextPane();

        JScrollPane scrollPane1 = new JScrollPane(textPane1);
        JScrollPane scrollPane2 = new JScrollPane(textPaneInput);
        JScrollPane scrollPane3 = new JScrollPane(textPaneOutput);


        // Create the SplitPanel for Messages
        splitPanelMessages = new EbSplitPanel("MessageSplitter", scrollPane2, scrollPane3, JSplitPane.VERTICAL_SPLIT);
        splitPanelMessages.setDividerLocation(200);

        // Create the SplitPanem vor Auswahl and Messages
        splitPanelMain = new EbSplitPanel("MainSplitter", new JScrollPane(textPane1), splitPanelMessages.getMainPanel(), JSplitPane.HORIZONTAL_SPLIT);
        splitPanelMain.setDividerLocation(400);

        add(splitPanelMain.getMainPanel(), BorderLayout.CENTER);

        splitPanelMain.getToolBar2().setVisible(false);

    }

    private void saveFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showSaveDialog(this);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            java.io.File selectedFile = fileChooser.getSelectedFile();
            try {
                java.io.FileWriter fileWriter = new java.io.FileWriter(selectedFile);
                fileWriter.write(textPane1.getText());
                fileWriter.close();
                JOptionPane.showMessageDialog(this, "Datei gespeichert: " + selectedFile.getCanonicalPath());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Fehler beim Speichern der Datei: " + ex.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


}
