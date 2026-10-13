package com.eb.apps.snippets.gui;

import com.eb.base.gui.EbSplitPanel;

import javax.swing.*;
import java.awt.*;


public class SnippetWindowFrame extends JFrame {
    JMenuBar menuBar;
    EbSplitPanel splitPanel;
    private JTextArea edSnippetText;
    private JPanel treePanel;
    private JTree jTree;
    private JToolBar mainToolBar;


    public SnippetWindowFrame() {
        initializeView();
    }

    public void initializeView() {
        setTitle("Snippets");

        setJMenuBar(new JMenuBar());
        add(getMainToolBar(),BorderLayout.NORTH);
        add(getSplitPanel().getSplitPane(), BorderLayout.CENTER);
    }

    public JToolBar getMainToolBar() {
        if (mainToolBar==null)
        {
            mainToolBar = new JToolBar();
        }
        return mainToolBar;
    }

    public EbSplitPanel getSplitPanel() {
        if (splitPanel != null)
            return splitPanel;

        splitPanel = new EbSplitPanel("MainPanel", getTreePanel(), getEdSnippetText(), JSplitPane.HORIZONTAL_SPLIT);
        return splitPanel;
    }

    public JTextArea getEdSnippetText() {
        if (edSnippetText == null)
        {
            edSnippetText = new JTextArea();
        }
        return edSnippetText;
    }

    public JTree getJTree() {
        if (jTree == null) {
            jTree = new JTree();
        }
        return jTree;
    }

    public  JComponent getTreePanel() {
        return getJTree();
    }
}


