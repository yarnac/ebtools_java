package com.eb.apps.ebvlc.app;

import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.base.gui.EbSplitPanel;
import com.eb.base.gui.ICF;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VlcVideoForm {

    @Getter
    private JFrame frame;
    private JToolBar mainToolBar;
    @Getter
    JList<VlcFileItem> lstFiles;
    @Getter
    private List<String> deletedFiles;
    private JList<VlcFileItem> lstFavoriten;

    private JList<VlcFileItem> lstDeletedFiles;

    List<String> allFiles;
    private DefaultListModel<VlcFileItem> modelAllFiles;
    private DefaultListModel<VlcFileItem> modelDeleted;
    private DefaultListModel<VlcFileItem> modelFavoriten;

    private EbSplitPanel mainSplitPanel;
    private EbSplitPanel secondSplitPanel;
    private JScrollPane scrollPaneDeleted;
    private JScrollPane scrollPaneFiles;
    private JScrollPane scrollPaneFavoriten;




    public void setFileNames(List<String> files) {
        modelAllFiles.clear();
        modelAllFiles.addAll(getVlcFileItems(files));
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

        secondSplitPanel =  new EbSplitPanel("SecondSplitPanel", scrollPaneDeleted, getScrollPaneFavoriten(),0);
        mainSplitPanel = new EbSplitPanel("MainSplitPanel", scrollPaneFiles, secondSplitPanel, 1);
        frame.getContentPane().add(mainSplitPanel, BorderLayout.CENTER);
    }

    public JToolBar getMainToolBar() {
        if (mainToolBar == null) {
            mainToolBar = new JToolBar();
            mainToolBar.setName("MainToolbar");
            mainToolBar.setFloatable(false);
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
        lstDeletedFiles = new JList<VlcFileItem>();
        lstDeletedFiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // DragSelectionDecorator.enableDragSelection(lstFiles);

        modelDeleted = new DefaultListModel<VlcFileItem>();
        lstDeletedFiles.setModel(modelDeleted);
        scrollPaneDeleted = new JScrollPane(lstDeletedFiles);
        return scrollPaneDeleted;
    }

    private JScrollPane getScrollPaneFavoriten() {
        lstFavoriten = new JList<>();
        lstFavoriten.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // DragSelectionDecorator.enableDragSelection(lstFiles);

        modelFavoriten = new DefaultListModel<VlcFileItem>();
        lstFavoriten.setModel(modelFavoriten);
        scrollPaneFavoriten = new JScrollPane(lstFavoriten);
        return scrollPaneFavoriten;
    }

    private JScrollPane getScrollPaneFiles() {
        if (scrollPaneFiles == null)
        {
            lstFiles = new JList<>();
            lstFiles.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
            // DragSelectionDecorator.enableDragSelection(lstFiles);

            modelAllFiles = new DefaultListModel<>();
            lstFiles.setModel(modelAllFiles);
            scrollPaneFiles = new JScrollPane(lstFiles);
        }
        return scrollPaneFiles;
    }

    void setDeletedFiles(List<String> fileNames){
        deletedFiles = fileNames;
        modelDeleted = new DefaultListModel<>();
        modelDeleted.addAll(getVlcFileItems(fileNames));
        lstDeletedFiles.setModel(modelDeleted);
    }

    private static List<VlcFileItem> getVlcFileItems(List<String> fileNames) {
        return fileNames
                .stream()
                .map(VlcFileItem::new)
                .toList();
    }

    private static List<String> getFileNames(List<VlcFileItem> fileItems) {
        return fileItems
                .stream()
                .map(VlcFileItem::getFileName)
                .toList();
    }


    public List<String> getSelectedFiles() {
        return lstFiles.getSelectedValuesList().stream().map(VlcFileItem::getFileName).collect(Collectors.toList());
    }

    public void deleteFileName(String selectedFileName) {
        VlcFileItem item = new VlcFileItem(selectedFileName);
        int n = modelAllFiles.indexOf(item);
        if (n<0)
            return;
        modelAllFiles.remove(n);
    }

    public void deleteDeletedFileName(String selectedFileName) {
        VlcFileItem item = new VlcFileItem(selectedFileName);
        int n = modelDeleted.indexOf(item);
        if (n<0)
            return;
        modelDeleted.remove(n);
    }

    public JToolBar getFilesToolBar() {
        return mainSplitPanel.getToolBar1();
    }

    public JToolBar getDeletedFilesToolBar() {
        return secondSplitPanel.getToolBar1();
    }

    public JToolBar getFavoritenToolBar() {
        return secondSplitPanel.getToolBar2();
    }

    public void addDeletedFileName(String fileName) {
        modelDeleted.add(0, new VlcFileItem(fileName));
        deletedFiles.add(fileName);
        lstDeletedFiles.setModel(modelDeleted);
    }

    public List<String> getSelectedDeletedFiles() {
        return lstDeletedFiles.getSelectedValuesList().stream().map(VlcFileItem::getFileName).collect(Collectors.toList());
    }

    public void removeDeletedFile(String deletedFile) {
        VlcFileItem item = new VlcFileItem(deletedFile);
        deletedFiles.remove(deletedFile);
        int index = modelDeleted.indexOf(item);
        modelDeleted.remove(index);
        // lstDeletedFiles.remove(index);
    }
}
