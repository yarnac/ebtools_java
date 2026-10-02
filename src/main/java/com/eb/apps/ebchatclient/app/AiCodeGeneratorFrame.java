package com.eb.apps.ebchatclient.app;

import com.eb.base.gui.EbGridBagUtil;

import javax.swing.*;
import java.awt.*;

public class AiCodeGeneratorFrame extends JFrame {
    private JTextField edNamespace;
    private JTextField edTargetDir;
    private JTextArea edCode;
    private JToolBar toolBar;

    public static void main(String[] args) {
        AiCodeGeneratorFrame frame = new AiCodeGeneratorFrame();
        SwingUtilities.invokeLater(()-> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });

    }

    public AiCodeGeneratorFrame() {
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window
        initializeForm();
        JPanel panel = new JPanel(new GridBagLayout());
    }

    private void initializeForm() {
        setTitle("AiCodeGenerator");
        setLayout(new GridBagLayout());
        EbGridBagUtil gbUtil = new EbGridBagUtil(getContentPane(), 0);

        toolBar = gbUtil.addToolBarRow(false);
        edNamespace = gbUtil.addLabeledFieldRow(new JTextField(),"Namespace",0,GridBagConstraints.HORIZONTAL);
        edTargetDir = gbUtil.addLabeledFieldRow(new JTextField(),"Targetdirectory",0,GridBagConstraints.HORIZONTAL);
        edCode = gbUtil.addLabeledScrollPaneWithFieldRow(new JTextArea(),"Code",1,GridBagConstraints.BOTH);

    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public JTextField getEdNamespace() {
        return edNamespace;
    }

    public JTextField getEdTargetDir() {
        return edTargetDir;
    }

    public JTextArea getEdCode() {
        return edCode;
    }
}
