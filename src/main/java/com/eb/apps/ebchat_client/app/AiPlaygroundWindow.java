package com.eb.apps.ebchat_client.app;

import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.base.extensions.StringExtensions;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.gui.ICF;
import com.eb.base.gui.adapter.JTextPaneAdapter;
import com.eb.base.gui.persist.IComponentPersister;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.gui.EbSplitPanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class AiPlaygroundWindow extends JFrame {

    private IniFile myIniFile;
    private JToolBar toolBar;


    @Getter
    private final GuiDecorator decorator;
    @Getter
    private JTextPaneAdapter textPaneAdapterOutput;
    @Getter
    private JTextPane textPaneInput;
    @Getter
    private JTextPane textPaneOutput;
    private JTree historyTree;

    private EbSplitPanel splitPanelInput;
    private EbSplitPanel splitPanelMessages;
    private EbSplitPanel splitPanelMain;

    private PanelWithToolBar panelWithToolBarAuswahl;
    private PanelWithToolBar panelWithToolBarInput;
    private PanelWithToolBar panelWithToolBarOutput;
    private JTextPane textFieldInputPraefix;
    private JScrollPane scrollPaneInputPraefix;

    private JScrollPane scrollPaneInputText;
    @Getter
    private JLabel statusBarOutputLabel;


    public AiPlaygroundWindow(IniFile iniFile)
    {
        myIniFile = iniFile;
        initializeView();

        setFontSizes(16);



        decorator = new GuiDecorator(this, myIniFile,"Einstellungen");
        decorator.addEditIniFileButton("MainToolbar");

        decorateMainToolbar();
        decorateMessagesAuswahl();
        decorateMessagesOutput();
    }

    private void setFontSizes(int fontSize) {

        Font font = new Font("JetBrains Mono", Font.PLAIN, fontSize);
        if (!font.getFontName().equals("Jetbrains Mono"))
            font = new Font("Consolas", Font.PLAIN, fontSize);
        if (!font.getFontName().equals("Consolas"))
            font = new Font("Monospaced", Font.PLAIN, fontSize);

        // Font font = new Font("Arial", Font.PLAIN, fontSize);

        getTextPaneInput().setFont(font);
        getTextPaneOutput().setFont(font);
        getTextFieldInputPraefix().setFont(font);
        statusBarOutputLabel.setFont(font);
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

        EventQueue.invokeLater(() -> {
            try {
                // Dein existierender Code, vielleicht mit einem kleinen Delay, falls nötig
                textPaneOutput.setText(answer);
                textPaneOutput.setCaretPosition(0);
            } catch (Exception e) {
            }
        });

    }

    public String getNewInputString() {
        StringBuilder builder = new StringBuilder();
        if (!StringExtensions.ebIsNilOrEmpty(textFieldInputPraefix.getText()))
            builder.append(textFieldInputPraefix.getText()).append("\n");
        builder.append(textPaneInput.getText());
        return builder.toString();
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

    public void registerPersister(IComponentPersister persister) {
        persister.addComponentItem(getTextFieldInputPraefix(),"InputPraefix");
        persister.addComponentItem(textPaneInput,"Input");
        persister.addComponentItem(textPaneOutput,"Output");
        persister.addComponentItem(splitPanelMain.getSplitPane(), "SplitPanelMain");
        persister.addComponentItem(splitPanelMessages.getSplitPane(), "SplitPanelMessages");
        persister.addComponentItem(splitPanelInput.getSplitPane(), "SplitPanelInput");

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
            panelWithToolBarOutput = new PanelWithToolBar(splitPanelMessages.getPanel2(), splitPanelMessages.getToolBar2());
        return panelWithToolBarOutput;
    }

    private void initializeView() {
        // Set frame properties
        setTitle("AI Playground");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Image image = new ImageIcon(ICF.BulbOn.getImage(24)).getImage();
        setIconImage(image);
        if (GlobaleEinstellungen.isMacOs()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(image);
        }

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

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    private void createAndAddSplitPanelMain() {

        textPaneInput = new JTextPane();
        textPaneOutput = new JTextPane();

        scrollPaneInputText = new JScrollPane(textPaneInput);
        JScrollPane scrollPaneOutput = new JScrollPane(textPaneOutput);
        textPaneAdapterOutput = new JTextPaneAdapter(scrollPaneOutput, textPaneOutput);

        UndoDecorator.registerForTextArea(getTextPaneInput());
        UndoDecorator.registerForTextArea(getTextPaneOutput());


        splitPanelInput = new EbSplitPanel("splitPanelInput", getScrollPaneInputPraefix(), scrollPaneInputText, 0);

        JPanel jPanelOutputAndStatus = new JPanel(new BorderLayout());
        jPanelOutputAndStatus.add(scrollPaneOutput, BorderLayout.CENTER);
        statusBarOutputLabel = new JLabel();
        statusBarOutputLabel.setText("Status");
        jPanelOutputAndStatus.add(statusBarOutputLabel, BorderLayout.SOUTH);

        splitPanelMessages = new EbSplitPanel("MessageSplitter", splitPanelInput.getMainPanel(), jPanelOutputAndStatus, JSplitPane.VERTICAL_SPLIT);
        splitPanelMessages.setDividerLocation(200);

        //jPanelMessage.add(splitPanelMessages.getMainPanel(), BorderLayout.CENTER);




        // Create the SplitPanem vor Auswahl and Messages
        splitPanelMain = new EbSplitPanel("MainSplitter", getHistoryTree() , splitPanelMessages.getMainPanel(), JSplitPane.HORIZONTAL_SPLIT);
        splitPanelMain.setDividerLocation(400);

        add(splitPanelMain.getMainPanel(), BorderLayout.CENTER);



        splitPanelMain.getToolBar2().setVisible(false);

    }

    private JScrollPane getScrollPaneInputPraefix() {
        if (scrollPaneInputPraefix==null) {
            scrollPaneInputPraefix = new JScrollPane(getTextFieldInputPraefix());
        }
        return scrollPaneInputPraefix;
    }

    private JTextPane getTextFieldInputPraefix() {
        if (textFieldInputPraefix == null) {
            textFieldInputPraefix = new JTextPane();
            Dimension fixedHeight = new Dimension(0, 40); // 0 = Breite passt sich an, 120px = feste Höhe
            textFieldInputPraefix.setPreferredSize(fixedHeight);
            textFieldInputPraefix.setMaximumSize(fixedHeight);
            textFieldInputPraefix.setText("Übersetze ins Deutsche:");
        }
        return textFieldInputPraefix;
    }

    public JTree getHistoryTree() {
        if (historyTree == null)
            historyTree = new JTree();
        return historyTree;
    }
}
