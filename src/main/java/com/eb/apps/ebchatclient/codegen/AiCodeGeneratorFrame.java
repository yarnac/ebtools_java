package com.eb.apps.ebchatclient.codegen;

import com.eb.base.gui.EbGridBagUtil;

import javax.swing.*;
import java.awt.*;

public class AiCodeGeneratorFrame extends JFrame {
    private JTextField edNamespace;
    private JTextField edTargetDir;
    private JTextArea edCode;
    private JToolBar toolBar;
    private JToolBar toolBar2;

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
        toolBar2 = gbUtil.addLabeledFieldRow(new JToolBar(),"Code",0,GridBagConstraints.HORIZONTAL);
        toolBar2.setFloatable(false);
        edCode = gbUtil.addLabeledScrollPaneWithFieldRow(new JTextArea(),"",1,GridBagConstraints.BOTH);

    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public JToolBar getToolBarCode() {
        return toolBar2;
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
