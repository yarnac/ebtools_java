/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.List;

public final class ContextTreeTransferHandler extends TransferHandler {

    private static final DataFlavor AI_CHAT_CONTEXT_FLAVOR =
            new DataFlavor(AiChatContext.class, "AiChatContext");

    private final JTree tree;
    private final List<AiChatContext> contexts;
    private final Runnable refreshTree;

    public ContextTreeTransferHandler(
            JTree tree,
            List<AiChatContext> contexts,
            Runnable refreshTree) {

        this.tree = tree;
        this.contexts = contexts;
        this.refreshTree = refreshTree;
    }

    @Override
    public int getSourceActions(JComponent component) {
        return MOVE;
    }

    @Override
    protected Transferable createTransferable(JComponent component) {
        TreePath selectionPath = tree.getSelectionPath();

        if (selectionPath == null) {
            return null;
        }

        DefaultMutableTreeNode selectedNode =
                (DefaultMutableTreeNode) selectionPath.getLastPathComponent();

        Object userObject = selectedNode.getUserObject();

        // Nur Kontexte, keine Ordner, dürfen gezogen werden.
        if (!(userObject instanceof AiChatContext context)) {
            return null;
        }

        return new ContextTransferable(context);
    }

    @Override
    public boolean canImport(TransferSupport support) {
        if (!support.isDrop()) {
            return false;
        }

        if (!support.isDataFlavorSupported(AI_CHAT_CONTEXT_FLAVOR)) {
            return false;
        }

        JTree.DropLocation dropLocation =
                (JTree.DropLocation) support.getDropLocation();

        TreePath targetPath = dropLocation.getPath();

        if (targetPath == null) {
            return false;
        }

        DefaultMutableTreeNode targetNode =
                (DefaultMutableTreeNode) targetPath.getLastPathComponent();

        Object targetUserObject = targetNode.getUserObject();

        /*
         * Ein Ziel muss ein Ordner sein.
         *
         * Ordner enthalten bei unserem Tree-Modell Strings.
         * Blätter enthalten ein AiChatContext-Objekt.
         */
        return targetUserObject instanceof String;
    }

    @Override
    public boolean importData(TransferSupport support) {
        if (!canImport(support)) {
            return false;
        }

        try {
            AiChatContext context = (AiChatContext) support.getTransferable()
                    .getTransferData(AI_CHAT_CONTEXT_FLAVOR);

            JTree.DropLocation dropLocation =
                    (JTree.DropLocation) support.getDropLocation();

            TreePath targetPath = dropLocation.getPath();

            DefaultMutableTreeNode targetNode =
                    (DefaultMutableTreeNode) targetPath.getLastPathComponent();

            String targetFolderPath = getFolderPath(targetNode);

            /*
             * Beispiel:
             *
             * Baum:
             * Kontexte
             *   Entwicklung
             *     Java
             *
             * Ergebnis für Java:
             * Entwicklung/Java
             */
            context.setKnoten(targetFolderPath);

            // Da der JTree aus contexts abgeleitet wird:
            // Baum nach Datenänderung neu aufbauen.
            refreshTree.run();

            return true;

        } catch (UnsupportedFlavorException | IOException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    /**
     * Ermittelt den fachlichen Ordnerpfad eines Ordnerknotens.
     *
     * Der unsichtbare Root-Knoten "Kontexte" wird ignoriert.
     *
     * Beispiel:
     *   Kontexte -> Entwicklung -> Java
     *
     * Ergebnis:
     *   Entwicklung/Java
     */
    private String getFolderPath(DefaultMutableTreeNode folderNode) {
        TreePath path = new TreePath(folderNode.getPath());

        Object[] pathComponents = path.getPath();

        StringBuilder folderPath = new StringBuilder();

        // Index 0 ist immer der künstliche Root-Knoten.
        for (int index = 1; index < pathComponents.length; index++) {
            DefaultMutableTreeNode node =
                    (DefaultMutableTreeNode) pathComponents[index];

            Object userObject = node.getUserObject();

            if (!(userObject instanceof String folderName)) {
                continue;
            }

            if (!folderPath.isEmpty()) {
                folderPath.append('/');
            }

            folderPath.append(folderName);
        }

        return folderPath.toString();
    }

    private static final class ContextTransferable implements Transferable {

        private final AiChatContext context;

        private ContextTransferable(AiChatContext context) {
            this.context = context;
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{AI_CHAT_CONTEXT_FLAVOR};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return AI_CHAT_CONTEXT_FLAVOR.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor)
                throws UnsupportedFlavorException {

            if (!isDataFlavorSupported(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }

            return context;
        }
    }
}