package com.eb.apps.ebchat_client.app;

import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.apps.ebchat_client.domain.context.gui.ContextEditorPanel;
import com.eb.apps.ebchat_client.domain.context.gui.ContextEditorPanelCtrl;
import com.eb.base.extensions.StringExtensions;
import com.eb.base.gui.*;
import com.eb.base.gui.adapter.JTextAreaAdapter;
import com.eb.base.gui.adapter.JTextPaneAdapter;
import com.eb.base.gui.decorator.GuiDecorator;
import com.eb.base.gui.persist.IComponentPersister;
import com.eb.base.inifile.api.IniFile;
import lombok.Getter;

import java.util.List;
import javax.swing.*;
import java.awt.*;

public class AiPlaygroundWindow extends JFrame {

    public static int FONTSIZE = 14;

    private IniFile myIniFile;
    private JToolBar toolBar;


    @Getter
    private final GuiDecorator decorator;
    private JTree historyTree;

    private EbSplitPanel splitPanelMain;

    private PanelWithToolBar panelWithToolBarAuswahl;
    private PanelWithToolBar panelWithToolBarInput;
    private PanelWithToolBar panelWithToolBarOutput;

    @Getter
    private JToolBar toolBarHistoryTree;
    private JPanel treePanel;
    @Getter
    private String toolbarTreePanelName;
    private ContextEditorPanel contextEditorPanel;

    @Getter
    private ContextEditorPanelCtrl contextEditorPanelCtrl;
    private AiInputOutputPanel aiInputOutputPanel;
    @Getter
    private AiInputOutputPanelCtrl aiInputOutputPanelCtrl;


    public AiPlaygroundWindow(IniFile iniFile)
    {
        myIniFile = iniFile;
        initializeView();

        setFontSizes(FONTSIZE);

        decorator = new GuiDecorator(this, myIniFile,"Einstellungen");

        decorateMainToolbar();
        decorateMessagesAuswahl();
        decorateMessagesOutput();
    }

    private void setFontSizes(int fontSize, Component... components) {
        Font font = FontUtil.determineMonospaceFont(fontSize, "JetBrains Mono", "Consolas", "Monospaces");
        FontUtil.setFont(font, components);
    }

    private void setFontSizes(int fontSize) {
        FontUtil.setMonospaceFont(fontSize,
                aiInputOutputPanel.getTextPaneInput(),
                aiInputOutputPanel.getTextPaneOutput(),
                aiInputOutputPanel.getTextFieldInputPraefix(),
                getTreePanel(),
                aiInputOutputPanel.getStatusBarOutputLabel()
        );
    }

    private void decorateMessagesInput() {
        decorator.toolBars().setCurrentContainer(aiInputOutputPanel.getSplitPanelMessages().getToolBar1Name());
        decorator.toolBars().addToolbarButton("Speichern Input", IC.MB_PLAY, (x)->{});
    }

    private void decorateMessagesOutput() {
        decorator.toolBars().setCurrentContainer(aiInputOutputPanel.getSplitPanelMessages().getToolBar2Name());
        decorator.toolBars().addToolbarButton("Speichern Output", IC.MB_PLAY, (x)->{});
    }

    private void decorateMessagesAuswahl() {
        decorator.toolBars().setCurrentContainer(splitPanelMain.getToolBar1Name());
        decorator.toolBars().addToolbarButton("Speichern Auswahl", IC.MB_PLAY, (x)->{});
    }

    private void decorateMainToolbar() {
    }

    public void setOutputText(String answer) {

        EventQueue.invokeLater(() -> {
            try {
                // Dein existierender Code, vielleicht mit einem kleinen Delay, falls nötig
                aiInputOutputPanel.getTextPaneOutput().setText(answer);
                aiInputOutputPanel.getTextPaneOutput().setCaretPosition(0);
            } catch (Exception e) {
            }
        });

    }


    public String getInputString() {
        return aiInputOutputPanel.getTextPaneInput().getText();
    }

    public void setInputText(String inputString2) {
        aiInputOutputPanel.getTextPaneInput().setText(inputString2);
    }


    public void setInOutEnabled(boolean b) {
        aiInputOutputPanel.getTextPaneInput().setEnabled(b);
        aiInputOutputPanel.getTextPaneOutput().setEnabled(b);
        aiInputOutputPanel.getSplitPanelMessages().getToolBar1().setEnabled(b);
        aiInputOutputPanel.getSplitPanelMessages().getToolBar2().setEnabled(b);
        splitPanelMain.getToolBar1().setEnabled(b);
        splitPanelMain.getToolBar2().setEnabled(b);
    }

    public void registerPersister(IComponentPersister persister) {
        persister.addComponentItem(getTextFieldInputPraefix(),"InputPraefix");
        persister.addComponentItem(aiInputOutputPanel.getTextPaneInput(),"Input");
        persister.addComponentItem(aiInputOutputPanel.getTextPaneOutput(),"Output");
        persister.addComponentItem(splitPanelMain.getSplitPane(), "SplitPanelMain");
        persister.addComponentItem(aiInputOutputPanel.getSplitPanelInput().getSplitPane(), "SplitPanelInput");

    }

    public void setInputPraefixText(String userString) {
        aiInputOutputPanel.getTextFieldInputPraefix().setText(userString);
    }

    public ContextEditorPanelCtrl getContextEditorCtrl() {
        return contextEditorPanelCtrl;
    }

    public void setInput(String sn) {

        String s =
                sn
                        .replace("<user>\n","")
                        .replace("<system>\n","")
                        .replace("<assistant>\n","");


        if (s.trim().startsWith("<<"))
        {
            setInputPraefixText(StringExtensions.ebSubstringBeforeFirst(s.trim(),">>") + ">>");
            setInputText(StringExtensions.ebSubstringAfterFirst(s.trim(),">>"));
        }
        else
        {
            setInputPraefixText("");
            setInputText(s);
        }
    }

    public JTextPane getTextPaneInput() {
        return aiInputOutputPanel.getTextPaneInput();
    }

    public JTextPane getTextPaneOutput() {
        return aiInputOutputPanel.getTextPaneOutput();
    }

    public JTextPaneAdapter getTextPaneAdapterOutput() {
        return aiInputOutputPanel.getTextPaneAdapterOutput();
    }

    public JLabel getStatusBarOutputLabel() {
        return aiInputOutputPanel.getStatusBarOutputLabel();
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
            panelWithToolBarInput = new PanelWithToolBar(aiInputOutputPanel.getSplitPanelMessages().getPanel1(), aiInputOutputPanel.getSplitPanelMessages().getToolBar1());
        return panelWithToolBarInput;
    }

    public PanelWithToolBar getPanelWithToolBarOutput()
    {
        if (panelWithToolBarOutput == null)
            panelWithToolBarOutput = new PanelWithToolBar(aiInputOutputPanel.getSplitPanelMessages().getPanel2(), aiInputOutputPanel.getSplitPanelMessages().getToolBar2());
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
        setJMenuBar(menuBar);
    }

    public AiInputOutputPanel getAiInputOutputPanel()
    {
        if (aiInputOutputPanel == null)
        {
            aiInputOutputPanel = new AiInputOutputPanel();
            aiInputOutputPanelCtrl = new AiInputOutputPanelCtrl(aiInputOutputPanel, this);
        }
        return aiInputOutputPanel;
    }

    private void createAndAddSplitPanelMain() {



        splitPanelMain = new EbSplitPanel("MainSplitter", getContextEditorPanel() , getAiInputOutputPanel(), JSplitPane.HORIZONTAL_SPLIT);
        toolbarTreePanelName = splitPanelMain.getToolBar1Name();

        add(splitPanelMain.getMainPanel(), BorderLayout.CENTER);

        splitPanelMain.getToolBar2().setVisible(false);
    }

    private JTextPane getTextFieldInputPraefix() {
        return aiInputOutputPanel.getTextPaneInput();
    }

    private JComponent getContextEditorPanel() {
        if (contextEditorPanel==null)
        {
            contextEditorPanel = new ContextEditorPanel(true);
            contextEditorPanel.getSplitPane().setOrientation(JSplitPane.VERTICAL_SPLIT);
            contextEditorPanelCtrl = new ContextEditorPanelCtrl(contextEditorPanel);
        }
        return contextEditorPanel;
    }

    public JPanel getTreePanel()
    {
        if (treePanel==null) {
            treePanel = new JPanel(new BorderLayout());
            toolBarHistoryTree = new JToolBar();

            treePanel.add(toolBarHistoryTree, BorderLayout.NORTH);
            JScrollPane pane = new JScrollPane(getHistoryTree());
            treePanel.add(pane, BorderLayout.CENTER);
        }
        return treePanel;
    }

    public JTree getHistoryTree() {
        if (historyTree == null) {
            historyTree = new JTree();
            // historyTree.setMinimumSize(new Dimension(100,100));
        }
        return historyTree;
    }
}
