package com.eb.apps.ebchatclient;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class EbSplitPanel {

    @Getter
    private JPanel mainPanel;
    @Getter
    private JPanel panel1;
    @Getter
    private JPanel panel2;
    @Getter
    private JToolBar toolBar1;
    @Getter
    private JToolBar toolBar2;

    private JSplitPane splitPane;
    private JPanel outerPanel1;
    private JPanel outerPanel2;


    public EbSplitPanel(String panelName, JComponent component1, JComponent component2, int splitOrientation) {
        mainPanel = new JPanel();
        mainPanel.setName(panelName);
        mainPanel.setLayout(new BorderLayout());
        splitPane = new JSplitPane(splitOrientation);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerSize(6);
        outerPanel1 = new JPanel();
        outerPanel2 = new JPanel();
        outerPanel1.setLayout(new BorderLayout());
        outerPanel1.setLayout(new BorderLayout());
        outerPanel2.setLayout(new BorderLayout());
        outerPanel2.setLayout(new BorderLayout());

        toolBar1 = new JToolBar();
        toolBar1.setName(mainPanel.getName() + "_ToolBar_1");
        toolBar1.setFloatable(false);
        toolBar2 = new JToolBar();
        toolBar2.setName(mainPanel.getName() + "_ToolBar_2");
        toolBar2.setFloatable(false);

        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        panel2 = new JPanel();
        panel2.setLayout(new BorderLayout());

        outerPanel1.add(toolBar1, BorderLayout.NORTH );
        outerPanel1.add(panel1, BorderLayout.CENTER);
        outerPanel2.add(toolBar2, BorderLayout.NORTH );
        outerPanel2.add(panel2, BorderLayout.CENTER);

        splitPane.setLeftComponent(outerPanel1);
        splitPane.setRightComponent(outerPanel2);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        panel1.add(component1, BorderLayout.CENTER);
        panel2.add(component2, BorderLayout.CENTER);
    }

    public void setDividerLocation(int i) {
        splitPane.setDividerLocation(i);
    }

    public void setDividerSize  (int i) {
        splitPane.setDividerSize(i);
    }

    public void setOrientation(int verticalSplit) {
        splitPane.setOrientation(verticalSplit);
    }

    public String getToolBar1Name() {
        return toolBar1.getName();
    }

    public String getToolBar2Name() {
        return toolBar2.getName();
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }
}
