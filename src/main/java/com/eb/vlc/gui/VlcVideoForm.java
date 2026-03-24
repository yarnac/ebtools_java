package com.eb.vlc.gui;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.FileUtil;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VlcVideoForm {

    @Getter
    private JFrame frame;
    @Getter
    private Container toolBar;
    @Getter
    JList lstFiles;

    List<String> allFiles;
    private DefaultListModel model;

    public void setFileNames(List<String> files) {
        model.clear();
        model.addAll(files);
    }

    public VlcVideoForm() {
        initialize();
        frame.setVisible(true);
    }

    private void initialize() {
        frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setTitle("Vlc Video Form");
        frame.setVisible(true);
        frame.setBounds(100, 100, 450, 300);
        frame.setLocation(new Point(400, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu file = menuBar.add(new JMenu("File"));
        file.add(new JMenuItem("Open File"));
        file.add(new JMenuItem("Save File"));
        JMenu edit = menuBar.add(new JMenu("Edit"));
        edit.add(new JMenuItem("Copy File"));
        edit.add(new JMenuItem("Paste File"));
        frame.setJMenuBar(menuBar);


        toolBar = new JToolBar();
        toolBar.setName("MainToolbar");
        toolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.getContentPane().add(toolBar, BorderLayout.NORTH);
        toolBar.revalidate();
        toolBar.repaint();


        JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout());
        lstFiles = new JList();
        lstFiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // DragSelectionDecorator.enableDragSelection(lstFiles);

        model = new DefaultListModel();
        lstFiles.setModel(model);
        JScrollPane scrollPane = new JScrollPane(lstFiles);
        panel1.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(panel1, BorderLayout.CENTER);
    }

    public List<String> getSelectedFiles() {
        return lstFiles.getSelectedValuesList();
    }
}
