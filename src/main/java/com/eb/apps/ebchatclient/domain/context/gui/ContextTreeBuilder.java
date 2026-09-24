/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchatclient.domain.context.gui;

import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ContextTreeBuilder {

    private ContextTreeBuilder() {
    }

    public static DefaultTreeModel createTreeModel(List<ContextWithFiles> contexts) {
        DefaultMutableTreeNode rootNode =
                new DefaultMutableTreeNode("Kontexte");

        /*
         * Speichert bereits erzeugte Verzeichnisknoten.
         *
         * Schlüssel beispielsweise:
         *   Entwicklung
         *   Entwicklung/Java
         *   Entwicklung/C#
         */
        Map<String, DefaultMutableTreeNode> folderNodes = new HashMap<>();

        for (ContextWithFiles context : contexts) {
            DefaultMutableTreeNode parentNode =
                    getOrCreateFolderNode(rootNode, folderNodes, context.getKnoten());

            // Das AiChatContext-Objekt wird direkt im Blatt gespeichert.
            DefaultMutableTreeNode contextNode =
                    new DefaultMutableTreeNode(context, false);

            parentNode.add(contextNode);
        }

        return new DefaultTreeModel(rootNode);
    }

    private static DefaultMutableTreeNode getOrCreateFolderNode(
            DefaultMutableTreeNode rootNode,
            Map<String, DefaultMutableTreeNode> folderNodes,
            String knotenPath) {

        if (knotenPath == null || knotenPath.isBlank()) {
            return rootNode;
        }

        DefaultMutableTreeNode currentNode = rootNode;
        StringBuilder fullPath = new StringBuilder();

        // Leerzeichen um "/" herum sind erlaubt.
        String[] parts = knotenPath.trim().split("\\s*/\\s*");

        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }

            if (!fullPath.isEmpty()) {
                fullPath.append('/');
            }

            fullPath.append(part);

            String pathKey = fullPath.toString();

            DefaultMutableTreeNode folderNode = folderNodes.get(pathKey);

            if (folderNode == null) {
                folderNode = new DefaultMutableTreeNode(part);
                folderNodes.put(pathKey, folderNode);
                currentNode.add(folderNode);
            }

            currentNode = folderNode;
        }

        return currentNode;
    }
}