package com.eb.apps.ebchat_client.domain.context.gui;

import com.eb.baseswing.gui.EbSplitPanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class ContextEditorPanel extends JPanel {

    @Getter
    private final ContextTreePanel contextTreePanel;
    @Getter
    private final ContextEditDetailsPanel contextEditDetailsPanel;
    @Getter
    private final EbSplitPanel splitPane;

    public ContextEditorPanel(boolean ignoreLabels) {
        setLayout(new BorderLayout());
        contextTreePanel = new ContextTreePanel();
        contextTreePanel.setSaveAction(this::saveContextList);

        contextEditDetailsPanel = new ContextEditDetailsPanel(ignoreLabels, false);

        splitPane = new EbSplitPanel("mainPanel",
                new JScrollPane(contextTreePanel),
                contextEditDetailsPanel,
                JSplitPane.HORIZONTAL_SPLIT
        );

        splitPane.setDividerLocation(430);
        add(splitPane, BorderLayout.CENTER);
    }

    public void undo() {
        contextEditDetailsPanel.undo();
    }

    private void saveContextList() {
    }

    public void setDividerLocation(int i) {
        splitPane.setDividerLocation(300);
    }


    public void registerEvents() {
        getContextTreePanel().setTreeSelectionListener(x->transferSelectedContext());
        getContextEditDetailsPanel().setSaveListener(()->contextTreePanel.rebuildTree());
    }

    private void transferSelectedContext() {
        contextEditDetailsPanel.transferModelToView(contextTreePanel.getSelectedContext());
    }
}
