/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context.app;

import com.eb.apps.ebchatclient.domain.chat.AiContextProvider;
import com.eb.apps.ebchatclient.domain.context.domain.ContextManager;
import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;
import com.eb.apps.ebchatclient.domain.context.gui.ContextEditorPanel;
import com.eb.apps.ebchatclient.domain.context.gui.ContextTreePanel;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;
import java.util.function.Predicate;

public class ContextEditDlg extends JFrame {

    private static IniFile myIniFile;
    private final GuiDecorator decorator;
    private final ContextManager contextManager;
    private JSplitPane splitPane;
    private ContextEditorPanel contextEditorPanel;
    private ContextTreePanel contextTreePanel;

    private List<ContextWithFiles> contextList;
    private JToolBar toolBarMain;
    private JComboBox<String> comboBox;
    private Predicate<String> filter;


    public static ContextEditDlg Show() {
        ContextEditDlg dlg = new ContextEditDlg();
        dlg.setContextList(AiContextProvider.getAvailableContexts());
        return dlg;
    }

    public static ContextEditDlg ShowWindow() {
        ContextEditDlg dlg = new ContextEditDlg();
        dlg.setNormalWindow();
        dlg.setContextList(AiContextProvider.getAvailableContexts());
        return dlg;
    }

    public ContextEditDlg() {
        initializeView();

        contextManager = AiContextProvider.getContextManager();

        myIniFile = IniFileProvider.createIniFile("ContextEditor.ini");
        decorator = new GuiDecorator(this, myIniFile, "Einstellungen");
        initMenuAndToolBar();

        setVisible(true);

        splitPane.setDividerLocation(300);

    }

    private void initMenuAndToolBar() {

        decorator.setCurrentMenu("Kontext");
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
        decorator.addMenuItem("Neu", ()->createNewContext(e), KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        decorator.addMenuItem("Löschen", ()->deleteContext(e), KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK );
        decorator.addMenuSeparator();
        decorator.addMenuItem("Speichern", ()->saveContext(e), KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        decorator.addMenuItem("Undo", ()->undoEdit(e), KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        decorator.addMenuItem("Copy Prompt", ()->copyPrompt(e), KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

        decorator.addContainer("mainToolBar", toolBarMain);
        decorator.addContainer("editorToolBar", contextEditorPanel.getToolBar());

        decorator.addToolbarButton("mainToolBar", "New Context", ICF.BlankDocument_Add, this::createNewContext);
        decorator.addToolbarButton("mainToolBar", "New Context", ICF.BlankDocument_Delete, this::deleteContext);
        String[] Inhalte = new String[]{"Alle", "Prompts", "Snippets"};
        comboBox = decorator.addToolbarComboBox("mainToolBar", "Inhalt",Inhalte, x->setFilter());
        comboBox.setMaximumSize(new Dimension(100,20));
        decorator.addToolbarButton("editorToolBar", "Save Context", ICF.Save, this::saveContext);
        decorator.addToolbarButton("editorToolBar", "Undo", ICF.UndoBlue, this::undoEdit);
    }

    private void setFilter() {
        if (comboBox.getSelectedItem().equals("Prompts")) {
            filter = x->x != null && x.startsWith("Prompts");
        }
        else if (comboBox.getSelectedItem().equals("Snippets")) {
            filter = x->x != null && x.startsWith("Snippets");
        }
        else
            filter = x->true;

        filterContexts();

    }

    private void filterContexts() {
        if (filter!=null)
            contextTreePanel.setContextList(contextList.stream().filter(x->filter.test(x.getKnoten())).toList());
    }

    private void copyPrompt(ActionEvent e) {
        StringSelection selection = new StringSelection(contextEditorPanel.getPromptText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.setContents(selection, null);
    }

    private void undoEdit(ActionEvent actionEvent) {
        contextEditorPanel.undo();
    }

    private void saveContext(ActionEvent actionEvent) {
        saveContext();
    }

    private void saveContext() {
        contextEditorPanel.transferViewToModel();
        saveContextList();
        contextTreePanel.rebuildTree();
    }

    private void deleteContext(ActionEvent actionEvent) {
        ContextWithFiles selectedContext = contextTreePanel.getSelectedContext();
        if (selectedContext == null)
            return;
        contextList.remove(selectedContext);
        saveContextList();
        contextTreePanel.rebuildTree();
        contextEditorPanel.undo();
    }

    private void createNewContext(ActionEvent actionEvent) {
        ContextWithFiles selectedContext = contextTreePanel.getSelectedContext();
        if (selectedContext == null)
            return;
        ContextWithFiles newContext = new ContextWithFiles("Neuer Knoten", selectedContext.getKnoten(), "");
        contextList.add(newContext);
        contextTreePanel.rebuildTree();
        contextTreePanel.setSelectedContext(newContext);
        filterContexts();
    }


    public void setContextList(List<ContextWithFiles> contextList)
    {
        contextTreePanel.setContextList(contextList);
        this.contextList = contextList;
    }


    private void saveContextList() {
        try {
            contextManager.writeKontexte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JButton addToolBarButton(JToolBar bar, String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.addActionListener(e->action.run());
        bar.add(btn);
        return btn;
    }

    public void setNormalWindow()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initializeView() {
        setTitle("Chat Kontexte bearbeiten");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1800, 600);
        setLocation(new Point(400,400));


        initialieComponents();

        registerEvents();


    }

    private void registerEvents() {
        contextTreePanel.setTreeSelectionListener(x->transferSelectedContext());
        contextEditorPanel.setSaveListener(()->contextTreePanel.rebuildTree());
    }

    private void transferSelectedContext() {
        contextEditorPanel.transferModelToView(contextTreePanel.getSelectedContext());
    }


    private void initialieComponents() {
        toolBarMain = new JToolBar();
        add(toolBarMain, BorderLayout.NORTH);

        contextTreePanel = new ContextTreePanel();
        contextTreePanel.setSaveAction(this::saveContextList);

        contextEditorPanel = new ContextEditorPanel();

        splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                contextTreePanel,
                contextEditorPanel
        );
        splitPane.setResizeWeight(0.0);
        add(splitPane, BorderLayout.CENTER);
        splitPane.setDividerLocation(430);
    }
}
