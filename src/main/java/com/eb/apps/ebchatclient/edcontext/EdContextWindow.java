package com.eb.apps.ebchatclient.edcontext;

import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;
import com.eb.base.gui.EbSplitPanel;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
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

        GridBagConstraints gbcLeft = new GridBagConstraints();
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;
        gbcLeft.anchor = GridBagConstraints.WEST;
        gbcLeft.anchor = GridBagConstraints.NORTHWEST;
        gbcLeft.insets = new Insets(5, 5, 5, 5);  // Padding around components


        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.anchor = GridBagConstraints.NORTHWEST;
        gbcRight.insets = new Insets(5, 5, 5, 5);  // Padding around components

        // Name Label and JTextField

        gbcLeft.weightx = 0.5;
        gbcLeft.weighty = 0.5;
        gbcLeft.gridx = 0;
        gbcLeft.gridheight = 20;


        gbcRight.weightx = 1.0;
        gbcRight.weighty = 1.0;
        gbcRight.gridx = 1;
        gbcLeft.gridheight = 20;

        gbcLeft.gridy = 0;
        mainPanel.add(new JLabel("Name"), gbcLeft);

        gbcRight.gridy = 0;
        mainPanel.add(edContextName, gbcRight);

        // Kategorie Label and JTextField
        gbcLeft.gridy = 1;
        mainPanel.add(new JLabel("Kategorie"), gbcLeft);

        gbcRight.gridy = 1;
        mainPanel.add(edKategorie, gbcRight);

        gbcLeft.weighty = 2.0;
        gbcLeft.gridy = 2;
        mainPanel.add(new JLabel("Message"), gbcLeft);

        gbcRight.weighty = 2.0;
        gbcRight.gridy = 2;
        gbcRight.fill = GridBagConstraints.BOTH;
        mainPanel.add(new JScrollPane(edMessage), gbcRight);





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
            edKategorie.setText(context.getKnoten());
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
