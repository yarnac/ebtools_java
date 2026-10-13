package com.eb.apps.ebchat_utils.edcontext;

import com.eb.apps.ebchat_client.domain.context.domain.ContextWithFiles;
import com.eb.apps.ebchat_client.domain.chat.AiChatManager;
import com.eb.apps.ebchat_client.domain.context.domain.ContextManager;

import java.util.HashMap;
import java.util.List;

public class TreeBuilder {

    public ContextTreeNode buildTree() {

        ContextManager manager = AiChatManager.getCurrent().getContextManager();
        List<ContextWithFiles> availableContexts = manager.getContextList();

        ContextTreeNode rootNode = new ContextTreeNode("Root");
        HashMap<String, ContextTreeNode> set = new HashMap<>();
        for (ContextWithFiles context : availableContexts) {
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

