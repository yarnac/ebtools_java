package com.eb.doubletten.actual.duplicateapp.gui;

import com.eb.doubletten.actual.duplicates.api.IDuplikatContainer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DuplicateView extends JFrame {


    private JToolBar mainToolBar;
    private JSplitPane splitPane;
    private JComboBox cbPaths = new JComboBox();
    JList<IDuplikatContainer> containerList = new JList<>();
    JList<String> duplicateList = new JList<>();

    private JToolBar leftToolBar;
    private JToolBar rightToolBar;
    private DefaultComboBoxModel cbPathModel;

    public JComboBox getCbPaths() {
        return cbPaths;
    }
    public JToolBar getLeftToolBar() {
        return leftToolBar;
    }

    public JToolBar getRightToolBar() {
        return rightToolBar;
    }

    public JToolBar getMainToolBar() {
        return mainToolBar;
    }

    public JList<IDuplikatContainer> getContainerList() {
        return containerList;
    }

    public JList<String> getDuplicateList() {
        return duplicateList;
    }

    public DuplicateView() {
        setTitle("Duplicate Finder");
        setSize(800, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        mainToolBar = new JToolBar("main");

        JPanel upperPanel = new JPanel();
        upperPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        upperPanel.add(mainToolBar);
        upperPanel.add(new JLabel("Verzeichnis:"));
        upperPanel.add(cbPaths);

        /*
        upperPanel.setLayout(new BorderLayout());
        upperPanel.add(mainToolBar, BorderLayout.NORTH);
        upperPanel.add(cbPaths, BorderLayout.SOUTH);
         */
        add(upperPanel, BorderLayout.NORTH);

        JPanel leftSplitPanel = new JPanel();
        leftSplitPanel.setLayout(new BorderLayout());
        leftToolBar = new JToolBar("Toolbar");
        leftSplitPanel.add(leftToolBar, BorderLayout.NORTH);
        leftSplitPanel.add(new JScrollPane(containerList), BorderLayout.CENTER);

        JPanel rightSplitPanel = new JPanel();
        rightSplitPanel.setLayout(new BorderLayout());
        rightToolBar = new JToolBar("Toolbar ");
        rightSplitPanel.add(rightToolBar, BorderLayout.NORTH);
        rightSplitPanel.add(new JScrollPane(duplicateList), BorderLayout.CENTER);



        splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                leftSplitPanel,
                rightSplitPanel
        );

        cbPaths.setEditable(true);
        add(splitPane, BorderLayout.CENTER);
    }

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public void setPathList(List<String> pathList) {
        cbPathModel = new DefaultComboBoxModel(pathList.toArray());
        cbPaths.setModel(cbPathModel);
    }

    public List<String> getPathList() {
        List<String> items = new ArrayList<>();

        ComboBoxModel<?> model = cbPaths.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            items.add((String) model.getElementAt(i));
        }
        return items;
    }

    public void addPath(String directoryPath) {
        cbPaths.addItem(directoryPath);
    }
}
