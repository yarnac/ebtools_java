/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchat_client.domain.context.app;

import com.eb.apps.ebchat_client.domain.context.gui.ContextEditDetailsPanel;
import com.eb.apps.ebchat_client.domain.context.gui.ContextEditorPanel;
import com.eb.apps.ebchat_client.domain.context.gui.ContextEditorPanelCtrl;
import com.eb.apps.ebchat_client.domain.context.gui.ContextTreePanel;
import com.eb.base.gui.decorator.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

public class ContextEditDlg extends JFrame {

    private IniFile myIniFile;
    private final GuiDecorator decorator;

    private JSplitPane splitPane;
    private ContextEditDetailsPanel contextEditDetailsPanel;
    private ContextTreePanel contextTreePanel;


    private JToolBar toolBarMain;
    private JComboBox<String> comboBox;

    private ContextEditorPanel contextEditorPanel;
    private ContextEditorPanelCtrl contextEditorPanelCtrl;


    public static ContextEditDlg Show() {

        ContextEditDlg dlg = new ContextEditDlg();
        dlg.setVisible(true);

        return dlg;
    }

    public static ContextEditDlg ShowWindow() {

        ContextEditDlg dlg = new ContextEditDlg();
        dlg.setNormalWindow();
        return dlg;
    }

    public ContextEditDlg() {
        initializeView();

        myIniFile = IniFileProvider.createIniFile("ContextEditor.ini");
        decorator = new GuiDecorator(this, myIniFile, "Einstellungen");
        initMenuAndToolBar();
        contextEditorPanel.setDividerLocation(300);

    }

    private void initMenuAndToolBar() {

        decorator.menuBars().setCurrentMenu("Kontext");
        ActionEvent e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "");
        decorator.menuBars().addMenuItem("Neu", ()->createNewContext(e), KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK);
        decorator.menuBars().addMenuItem("Löschen", ()->deleteContext(e), KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK );
        decorator.menuBars().addMenuSeparator();
        decorator.menuBars().addMenuItem("Speichern", ()->saveContext(e), KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK);
        decorator.menuBars().addMenuItem("Undo", ()->undoEdit(e), KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK);
        decorator.menuBars().addMenuItem("Copy Prompt", ()->copyPrompt(e), KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);

        decorator.toolBars().addContainer("mainToolBar", toolBarMain);

    }

    private void createNewContext(ActionEvent actionEvent) {
        contextEditorPanelCtrl.createNewContext();
    }


    private void deleteContext(ActionEvent e) {
        contextEditorPanelCtrl.deleteContext();
    }

    private void copyPrompt(ActionEvent e) {
        StringSelection selection = new StringSelection(contextEditDetailsPanel.getPromptText());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        clipboard.setContents(selection, null);
    }

    private void undoEdit(ActionEvent actionEvent) {
        contextEditorPanel.undo();
    }

    private void saveContext(ActionEvent actionEvent) {
        contextEditorPanelCtrl.saveContext();
    }

    public void setNormalWindow()
    {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void initializeView() {
        setTitle("Chat Kontexte bearbeiten");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1800, 600);
        setLocation(new Point(400,400));


        initializeComponents();
    }





    private void initializeComponents() {
        toolBarMain = new JToolBar();
        add(toolBarMain, BorderLayout.NORTH);
        add(getContextEditorPanel());
    }

    private Component getContextEditorPanel() {
        if (contextEditorPanel==null)
        {
            contextEditorPanel = new ContextEditorPanel(false);
            contextEditorPanelCtrl = new ContextEditorPanelCtrl(contextEditorPanel);
        }
        return contextEditorPanel;
    }
}
