/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.edcontext.ContextTreeNode;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.tree.*;
import java.awt.*;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class ContextTreePanel extends JPanel {
    @Getter
    private JTree contextTree;
    private JToolBar toolBar;
    private List<AiChatContext> contextList;
    @Getter
    private AiChatContext selectedContext;

    @Setter
    private Consumer<DefaultMutableTreeNode> treeSelectionListener;

    @Setter
    private Runnable saveAction;

    public ContextTreePanel() {

        initializeComponents();
        registerEvents();
    }

    private void registerEvents() {
        contextTree.addTreeSelectionListener((TreeSelectionEvent event) -> {
            selectedContext = null;
            DefaultMutableTreeNode selectedNode =
                    (DefaultMutableTreeNode) contextTree.getLastSelectedPathComponent();

            if (selectedNode != null) {
                if (selectedNode.getUserObject() instanceof AiChatContext) {
                    selectedContext = (AiChatContext) selectedNode.getUserObject();
                }
            }

            if (treeSelectionListener != null) {
                treeSelectionListener.accept(selectedNode);
            }
        });
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        add(toolBar, BorderLayout.NORTH);

        setMinimumSize(new Dimension(250, 100));

        contextTree = new JTree();
        contextTree.setRootVisible(false);
        contextTree.setShowsRootHandles(true);
        contextTree.setDropMode(DropMode.ON);
        contextTree.setDragEnabled(true);
        contextTree.setModel(new DefaultTreeModel(new ContextTreeNode("")));

        add(contextTree, BorderLayout.CENTER);
    }

    public void setContextList(List<AiChatContext> contextList) {
        this.contextList = contextList;
        rebuildTree();

        ContextTreeTransferHandler transferHandler =
                new ContextTreeTransferHandler(
                        contextTree,
                        contextList,
                        this::saveAndRebuildTree
                );

        contextTree.setTransferHandler(transferHandler);
    }


    private void saveAndRebuildTree() {

        if (saveAction!=null)
            saveAction.run();
        rebuildTree();
    }

    public void rebuildTree() {
        AiChatContext actContext = selectedContext;
        DefaultTreeModel treeModel =
                ContextTreeBuilder.createTreeModel(contextList);

        contextTree.setModel(treeModel);
        expandAllTreeNodes();

        contextTree.clearSelection();

        setSelectedContext(actContext);
    }

    public static DefaultMutableTreeNode findNode(JTree tree, Object userObject) {
        DefaultMutableTreeNode root =
                (DefaultMutableTreeNode) tree.getModel().getRoot();

        return findNode(root, userObject);
    }

    private static DefaultMutableTreeNode findNode(
            DefaultMutableTreeNode node, Object userObject) {

        if (userObject == null
                ? node.getUserObject() == null
                : userObject.equals(node.getUserObject())) {
            return node;
        }

        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child =
                    (DefaultMutableTreeNode) node.getChildAt(i);

            DefaultMutableTreeNode result = findNode(child, userObject);

            if (result != null) {
                return result;
            }
        }

        return null;
    }

    public void setSelectedContext(AiChatContext context) {

        DefaultMutableTreeNode node = findNode(contextTree, context);

        if (node != null) {
            TreePath path = new TreePath(node.getPath());
            contextTree.setSelectionPath(path);
            TreePath path2 = contextTree.getSelectionPath();
            if (!path.equals(path2)) {
                throw new RuntimeException("Fehler");
            }
            // contextTree.requestFocusInWindow();
        }
    }

    private void expandAllTreeNodes() {
        for (int row = 0; row < contextTree.getRowCount(); row++) {
            contextTree.expandRow(row);
        }
    }
}
