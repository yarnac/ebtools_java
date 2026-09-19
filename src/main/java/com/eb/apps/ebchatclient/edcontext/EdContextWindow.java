package com.eb.apps.ebchatclient.edcontext;

import com.eb.apps.ebchatclient.app.AiPlaygroundCtrl;
import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;
import com.eb.base.gui.EbSplitPanel;
import com.sun.source.tree.Tree;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;

public class EdContextWindow extends JFrame {

    @Getter
    private final JTree contextTree;
    private final JTextField edContextName;
    private final JTextField edKategorie;
    private final JTextArea edMessage;

    public EdContextWindow() {


        setTitle("Editiere Kontexte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        contextTree = new JTree();

        // Erstelle Felder für den Namen und Kategorie
        edContextName = new JTextField("Name");
        edKategorie = new JTextField("Kategorie");

        // Erstelle einen TextArea für die Nachricht
        edMessage = new JTextArea("Nachricht...");
        edMessage.setLineWrap(true);
        edMessage.setWrapStyleWord(true);


        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);  // Padding around components

        // Name Label and JTextField
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Name"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;

        mainPanel.add(edContextName, gbc);

        // Kategorie Label and JTextField
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Kategorie"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;

        mainPanel.add(edKategorie, gbc);

        // Message Label
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;  // Span the label across two columns
        mainPanel.add(new JLabel("Message"), gbc);

        // JTextArea for message
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;  // Span the textarea across two columns
        gbc.fill = GridBagConstraints.BOTH; // Fill the space available

        JScrollPane scrollPane = new JScrollPane(edMessage);  // Add a scroll pane for better usability
        mainPanel.add(scrollPane, gbc);





        EbSplitPanel mainSplitPanel = new EbSplitPanel("Test", new JScrollPane(contextTree), mainPanel,1);
        mainSplitPanel.getPanel2().setLayout(new GridLayout());
        add(mainSplitPanel.getSplitPane(), BorderLayout.CENTER);
        contextTree.addTreeSelectionListener(e->handleSelectionChanged(e));
    }

    private void handleSelectionChanged(TreeSelectionEvent e) {
        contextTree.getSelectionModel().setSelectionPath(e.getPath());
        TreePath path = e.getPath();
        ContextTreeNode node = (ContextTreeNode) path.getLastPathComponent();
        node.toString();
        AiChatContext context = (AiChatContext) node.getUserObject();
        if (context == null) {
            edContextName.setText("");
            edKategorie.setText("");
            edMessage.setText("");
        }
        else
        {
            edContextName.setText(context.getName());
            edKategorie.setText(context.getKategorie());
            edMessage.setText(context.getRequestMessage());
        }


    }

    public static void main(String[] args) {

        AiChatManager.getCurrent();

        SwingUtilities.invokeLater(() -> {
            EdContextWindow ctrl = new EdContextWindow();
            ContextTreeNode contextTreeNode = new TreeBuilder().buildTree();
            ctrl.getContextTree().setModel(new DefaultTreeModel(contextTreeNode));
            ctrl.setVisible(true);
        });
    }
}
