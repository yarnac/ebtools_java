package com.eb.inspector.domain.attributes;

import com.eb.base.gui.EbSplitPanel;
import lombok.Getter;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;

@Getter
public class InspectWindow extends JFrame {

    private JTree tree;
    private JTable table;
    private JScrollPane treePane;
    private JScrollPane tablePane;
    private EbSplitPanel splitPane;
    private PropertyDetailPane propertyDetailPane;

    public InspectWindow() {
        initializeView();
    }

    public InspectTreeNode getSelectedNode() {
        TreePath path = getTree().getSelectionPath();

        Object lastComp = path.getLastPathComponent();

        return lastComp instanceof InspectTreeNode ? (InspectTreeNode) lastComp : null;
    }

    public void initializeView() {
        this.setTitle("Inspect Window");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setBounds(100, 100, 450, 300);
        this.setLocationRelativeTo(null);

        tree = new JTree();
        treePane = new JScrollPane(tree);
        table = new JTable();
        tablePane = new JScrollPane(table);

        propertyDetailPane = new PropertyDetailPane(this);
        JScrollPane detailScroll = new JScrollPane(propertyDetailPane);

        splitPane = new EbSplitPanel("mainSplitPane", treePane, detailScroll, 1);
        add(splitPane, BorderLayout.CENTER);
    }


}
