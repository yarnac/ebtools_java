package com.eb.apps.ebchat_client.domain.context.gui;

import com.eb.apps.ebchat_client.components.JsonFileAppendUtil;
import com.eb.apps.ebchat_client.domain.chat.AiContextProvider;
import com.eb.apps.ebchat_client.domain.context.domain.ContextManager;
import com.eb.apps.ebchat_client.domain.context.domain.ContextWithFiles;
import com.eb.base.gui.ICF;
import com.eb.base.gui.decorator.GuiDecoratorMenuBars;
import com.eb.base.gui.decorator.GuiDecoratorToolBars;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ContextEditorPanelCtrl {

    private final ContextEditorPanel contextEditorPanel;
    private final ContextTreePanel contextTreePanel;
    private final ContextEditDetailsPanel contextEditDetailsPanel;
    private GuiDecoratorToolBars toolBars;
    private JComboBox comboBox;
    private Predicate<String> filter;
    private final ContextManager contextManager;


    private List<ContextWithFiles> contextList;

    public ContextEditorPanelCtrl(ContextEditorPanel contextEditorPanel) {
        contextManager = AiContextProvider.getContextManager();
        this.contextEditorPanel = contextEditorPanel;
        contextTreePanel = contextEditorPanel.getContextTreePanel();
        contextEditDetailsPanel = contextEditorPanel.getContextEditDetailsPanel();
        registerEvents();
        setContextList(AiContextProvider.getAvailableContexts());
    }



    public void setContextList(List<ContextWithFiles> contextList)
    {
        contextEditorPanel.getContextTreePanel().setContextList(contextList);
    }


    public void registerEvents() {
        contextEditorPanel.registerEvents();

        toolBars = new GuiDecoratorToolBars();
        toolBars.addContainer("mainToolBar", contextEditorPanel.getSplitPane().getToolBar1());

        toolBars.addToolbarButton("New Context", ICF.BlankDocument_Add, (e)->createNewContext());
        toolBars.addToolbarButton("New Context", ICF.BlankDocument_Delete, (e) -> deleteContext());
        String[] Inhalte = new String[]{"Alle", "Prompts", "Snippets"};
        comboBox = toolBars.addToolbarComboBox("Inhalt", Inhalte, x->setFilter());
        comboBox.setMaximumSize(new Dimension(100,20));

        toolBars.addContainer("editorToolBar", contextEditorPanel.getContextEditDetailsPanel().getToolBar());
        toolBars.addToolbarButton("Save Context", ICF.Save, e -> saveContext());
        toolBars.addToolbarButton("Undo", ICF.UndoBlue, (e) -> undoEdit());
        toolBars.addToolbarButton("Get Output", ICF.MediaButtonPlayGreen, this::setOutputContent);
    }

    private void undoEdit() {
        contextEditorPanel.undo();
    }


    private void setOutputContent(ActionEvent actionEvent) {
        String code = contextEditDetailsPanel.getEdPrompt().getText();
        String expanded = JsonFileAppendUtil.appendFiles(code, new ArrayList<>());
        if (expanded != null) {

            if (code.contains("\n<$D")) {
                contextEditDetailsPanel.getEdPrompt().setText(expanded);
            }
            else {
                contextEditDetailsPanel.getEdOutput().setText(expanded);
            }
        }

    }


    public void saveContext() {
        contextEditDetailsPanel.transferViewToModel();
        saveContextList();
        contextTreePanel.rebuildTree();
    }

    public void deleteContext() {
        ContextWithFiles selectedContext = contextTreePanel.getSelectedContext();
        if (selectedContext == null)
            return;
        contextList.remove(selectedContext);
        saveContextList();
        contextTreePanel.rebuildTree();
        contextEditDetailsPanel.undo();
    }

    private void saveContextList() {
        try {
            contextManager.writeKontexte();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void createNewContext() {
        ContextWithFiles selectedContext = contextEditorPanel.getContextTreePanel().getSelectedContext();
        if (selectedContext == null)
            return;
        ContextWithFiles newContext = new ContextWithFiles("Neuer Knoten", selectedContext.getKnoten(), "");
        contextList.add(newContext);
        contextTreePanel.rebuildTree();
        contextTreePanel.setSelectedContext(newContext);
        filterContexts();
    }

    private void filterContexts() {
        if (filter!=null)
            contextTreePanel.setContextList(contextList.stream().filter(x->filter.test(x.getKnoten())).toList());
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
}
