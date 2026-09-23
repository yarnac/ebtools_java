package com.eb.apps.ebchatclient.edcontext;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;

import java.util.HashMap;
import java.util.List;

public class TreeBuilder {

    public ContextTreeNode buildTree() {
        AiChatManager manager = AiChatManager.getCurrent();
        List<AiChatContext> availableContexts = manager.getAvailableContexts();
        ContextTreeNode rootNode = new ContextTreeNode("Root");
        HashMap<String, ContextTreeNode> set = new HashMap<>();
        for (AiChatContext context : availableContexts) {
            ContextTreeNode node = set.computeIfAbsent(context.getKnoten(), x -> addKategorieNode(rootNode, context.getKnoten()));
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

