package com.eb.apps.ebvlc.app;

import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.base.gui.EbSplitPanel;
import com.eb.base.gui.ICF;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VlcVideoForm {

    @Getter
    private JFrame frame;
    @Getter
    private JToolBar mainToolBar;
    @Getter
    JList<String> lstFiles;

    List<String> allFiles;
    private DefaultListModel<String> model;
    private EbSplitPanel mainSplitPanel;
    private EbSplitPanel secondSplitPanel;
    private JScrollPane scrollPaneDeleted;
    private JScrollPane scrollPaneFiles;

    public void setFileNames(List<String> files) {
        model.clear();
        model.addAll(files);
    }

    public VlcVideoForm() {
        initialize();

        Image image = new ImageIcon(ICF.Tablet.getImage(24)).getImage();
        frame.setIconImage(image);
        if (GlobaleEinstellungen.isMacOs()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(image);
        }

        frame.setVisible(true);
    }

    private void initialize() {
        initFrame();
        initMenuBar();

        frame.getContentPane().add(getMainToolBar(), BorderLayout.NORTH);

        JScrollPane scrollPaneFiles = getScrollPaneFiles();
        JScrollPane scrollPaneDeleted = getScrollPaneDeleted();

        secondSplitPanel =  new EbSplitPanel("SecondSplitPanel", scrollPaneDeleted, new JPanel(),0);
        mainSplitPanel = new EbSplitPanel("MainSplitPanel", scrollPaneFiles, secondSplitPanel, 1);
        frame.getContentPane().add(mainSplitPanel, BorderLayout.CENTER);
    }

    public JToolBar getMainToolBar() {
        if (mainToolBar == null) {
            mainToolBar = new JToolBar();
            mainToolBar.setName("MainToolbar");
            mainToolBar.setLayout(new FlowLayout(FlowLayout.LEFT));
            mainToolBar.revalidate();
            mainToolBar.repaint();
        }
        return mainToolBar;
    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu file = menuBar.add(new JMenu("File"));
        file.add(new JMenuItem("Open File"));
        file.add(new JMenuItem("Save File"));
        JMenu edit = menuBar.add(new JMenu("Edit"));
        edit.add(new JMenuItem("Copy File"));
        edit.add(new JMenuItem("Paste File"));
        frame.setJMenuBar(menuBar);
    }

    private void initFrame() {
        frame = new JFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.setTitle("Vlc Video Form");
        frame.setVisible(true);
        frame.setBounds(100, 100, 450, 300);
        frame.setLocation(new Point(400, 300));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private JScrollPane getScrollPaneDeleted() {
        JList<String> lstDeleted = new JList<String>();
        lstDeleted.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // DragSelectionDecorator.enableDragSelection(lstFiles);

        DefaultListModel<String> modelDeleted = new DefaultListModel<String>();
        modelDeleted.add(0,"Dummy");
        lstDeleted.setModel(modelDeleted);
        scrollPaneDeleted = new JScrollPane(lstDeleted);
        return scrollPaneDeleted;
    }

    private JScrollPane getScrollPaneFiles() {
        if (scrollPaneFiles == null)
        {
            lstFiles = new JList<>();
            lstFiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            // DragSelectionDecorator.enableDragSelection(lstFiles);

            model = new DefaultListModel<String>();
            model.add(0,"Dummy");
            lstFiles.setModel(model);
            scrollPaneFiles = new JScrollPane(lstFiles);
        }
        return scrollPaneFiles;
    }

    public List<String> getSelectedFiles() {
        return lstFiles.getSelectedValuesList();
    }

    public void deleteFileName(String selectedFileName) {
        int n = model.indexOf(selectedFileName);
        if (n<0)
            return;
        model.remove(n);
    }
}
