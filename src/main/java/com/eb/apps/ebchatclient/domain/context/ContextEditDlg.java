/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class ContextEditDlg extends JFrame {

    private static IniFile myIniFile;
    private final GuiDecorator decorator;
    private JSplitPane splitPane;
    private ContextEditorPanel contextEditorPanel;
    private ContextTreePanel contextTreePanel;

    private List<AiChatContext> contextList;
    private JToolBar toolBarMain;


    public static ContextEditDlg Show() {
        ContextEditDlg dlg = new ContextEditDlg();
        dlg.setContextList(AiContextProvider.getAvailableContexts());
        return dlg;
    }

    public ContextEditDlg() {
        initializeView();

        myIniFile = IniFileProvider.createIniFile("ContextEditor.ini");
        decorator = new GuiDecorator(this, myIniFile, "Einstellungen");
        initMenuAndToolBar();
    }

    private void initMenuAndToolBar() {

        decorator.setCurrentMenu("Kontext");
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
        decorator.addMenuItem("Neu", ()->createNewContext(e), KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        decorator.addMenuItem("Löschen", ()->deleteContext(e), KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK);
        decorator.addMenuSeparator();
        decorator.addMenuItem("Speichern", ()->saveContext(e), KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        decorator.addMenuItem("Undo", ()->undoEdit(e), KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);

        decorator.addContainer("mainToolBar", toolBarMain);
        decorator.addContainer("editorToolBar", contextEditorPanel.getToolBar());

        decorator.addToolbarButton("mainToolBar", "New Context", ICF.BlankDocument_Add, this::createNewContext);
        decorator.addToolbarButton("mainToolBar", "New Context", ICF.BlankDocument_Delete, this::deleteContext);
        decorator.addToolbarButton("editorToolBar", "Save Context", ICF.Save, this::saveContext);
        decorator.addToolbarButton("editorToolBar", "Undo", ICF.UndoBlue, this::undoEdit);
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
        AiChatContext selectedContext = contextTreePanel.getSelectedContext();
        if (selectedContext == null)
            return;
        contextList.remove(selectedContext);
        saveContextList();
        contextTreePanel.rebuildTree();
        contextEditorPanel.undo();
    }

    private void createNewContext(ActionEvent actionEvent) {
        AiChatContext selectedContext = contextTreePanel.getSelectedContext();
        if (selectedContext == null)
            return;
        AiChatContext newContext = new AiChatContext("Neuer Knoten", selectedContext.getKnoten(), "", "");
        contextList.add(newContext);
        contextTreePanel.rebuildTree();
        contextTreePanel.setSelectedContext(newContext);
    }


    public void setContextList(List<AiChatContext> contextList)
    {
        contextTreePanel.setContextList(contextList);
        this.contextList = contextList;
    }


    private void saveContextList() {
        try {
            AiChatManager.getCurrent().writeKontexte();
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

    private void initializeView() {
        setTitle("Chat Kontexte bearbeiten");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(1800, 600);
        setLocation(new Point(400,400));


        initialieComponents();

        setVisible(true);

        splitPane.setDividerLocation(300);

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
