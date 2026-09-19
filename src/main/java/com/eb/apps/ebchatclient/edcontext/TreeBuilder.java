package com.eb.apps.ebchatclient.edcontext;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;

import javax.naming.Context;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class TreeBuilder {

    public ContextTreeNode buildTree() {
        AiChatManager manager = AiChatManager.getCurrent();
        List<AiChatContext> availableContexts = manager.getAvailableContexts();
        ContextTreeNode rootNode = new ContextTreeNode("Root");
        HashMap<String, ContextTreeNode> set = new HashMap<>();
        for (AiChatContext context : availableContexts) {
            ContextTreeNode node = set.computeIfAbsent(context.getKategorie(), x -> addKategorieNode(rootNode, context.getKategorie()));
            node.add(new ContextTreeNode(context));
        }
        return rootNode;
    }


    private ContextTreeNode addKategorieNode(ContextTreeNode rootNode, String kategorie) {
        ContextTreeNode childNode = new ContextTreeNode(kategorie);
        rootNode.add(childNode);
        return childNode;
    }

}

