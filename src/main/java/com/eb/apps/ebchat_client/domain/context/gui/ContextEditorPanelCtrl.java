package com.eb.apps.ebchat_client.domain.context.gui;

import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.JsonFileAppendUtil;
import com.eb.apps.ebchat_client.domain.chat.AiContextProvider;
import com.eb.apps.ebchat_client.domain.context.domain.ContextManager;
import com.eb.apps.ebchat_client.domain.context.domain.ContextWithFiles;
import com.eb.base.gui.ICF;
import com.eb.base.gui.decorator.GuiDecoratorToolBars;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ContextEditorPanelCtrl {

    private final ContextEditorPanel contextEditorPanel;
    private final ContextTreePanel contextTreePanel;
    private final ContextEditDetailsPanel contextEditDetailsPanel;
    private GuiDecoratorToolBars toolBars;
    private JComboBox comboBox;
    private Predicate<String> filter;
    private final ContextManager contextManager;

    @Setter
    private Consumer<String> promptConsumer;
    @Setter
    private Consumer<String> promptOutputConsumer;



    public ContextEditorPanelCtrl(ContextEditorPanel contextEditorPanel) {
        contextManager = AiContextProvider.getContextManager();
        this.contextEditorPanel = contextEditorPanel;
        contextTreePanel = contextEditorPanel.getContextTreePanel();
        contextEditDetailsPanel = contextEditorPanel.getContextEditDetailsPanel();
        registerEvents();
        decorate();
        setContextList(AiContextProvider.getAvailableContexts());
    }

    private void decorate() {
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
        toolBars.addToolbarButton("Export Prompt", ICF.BlankDocument_Export, this::exportPrompt);
        toolBars.addToolbarButton("Export Output", ICF.TextDocument_Export, this::exportOutput);
    }


    public void setContextList(List<ContextWithFiles> contextList)
    {
        contextEditorPanel.getContextTreePanel().setContextList(contextList);
    }

    public List<ContextWithFiles> getContextList()
    {
        return contextEditorPanel.getContextTreePanel().getContextList();
    }


    public void registerEvents() {
        contextEditorPanel.registerEvents();
    }

    private void exportOutput(ActionEvent actionEvent) {
        if (promptOutputConsumer != null) {
            promptOutputConsumer.accept(contextEditDetailsPanel.getEdOutput().getText());
        }
    }

    private void exportPrompt(ActionEvent actionEvent) {
        if (promptConsumer != null) {
            promptConsumer.accept(contextEditDetailsPanel.getPromptText());
        }
    }



    private void undoEdit() {
        contextEditorPanel.undo();
    }


    private void setOutputContent(ActionEvent actionEvent) {
        String code = contextEditDetailsPanel.getEdPrompt().getText();
        String expanded = JsonFileAppendUtil.appendDirectoryFiles(code);
        expanded = JsonFileAppendUtil.appendFiles(expanded, new ArrayList<>());
        if (expanded != null) {
            contextEditDetailsPanel.getEdOutput().setText(expanded);
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
        getContextList().remove(selectedContext);
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
        if (selectedContext == null) {
            selectedContext = contextTreePanel.getLastSelectedNodeContext();
            if (selectedContext == null) {
                return;
            }
        }
        ContextWithFiles newContext = new ContextWithFiles("Neuer Knoten", selectedContext.getKnoten(), "");
        getContextList().add(newContext);
        contextTreePanel.rebuildTree();
        contextTreePanel.setSelectedContext(newContext);
        filterContexts();
    }

    private void filterContexts() {
        if (filter!=null)
            contextTreePanel.setContextList(getContextList().stream().filter(x->filter.test(x.getKnoten())).toList());
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
