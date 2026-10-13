package com.eb.apps.ebchat_client.app;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class AiHistoryTreeCtrl {
    private JTree historyTree;

    public AiHistoryTreeCtrl(JTree historyTree) {
        this.historyTree = historyTree;
        historyTree.setModel(new DefaultTreeModel(LoadHistoryTree()));

        // LoadHistoryTree();
    }

    private DefaultMutableTreeNode LoadHistoryTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("History");
        DefaultMutableTreeNode januar = add(root, new DefaultMutableTreeNode("Januar"));
        add(januar, new DefaultMutableTreeNode("Neujahr"));
        add(januar, new DefaultMutableTreeNode("3 Könige"));
        DefaultMutableTreeNode februar = add(root, new DefaultMutableTreeNode("Februar"));
        return root;
    }

    private DefaultMutableTreeNode add(DefaultMutableTreeNode root, DefaultMutableTreeNode januar) {
        root.add(januar);
        return januar;
    }

}
