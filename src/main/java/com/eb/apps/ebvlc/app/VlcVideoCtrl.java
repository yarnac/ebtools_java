package com.eb.apps.ebvlc.app;

import com.eb.base.gui.*;
import com.eb.base.gui.persist.ComponentPersisterFactory;
import com.eb.base.gui.persist.IComponentPersister;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.FileUtil;
import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.apps.ebmusic.tobj.MusicPlayer;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.System.out;

public class VlcVideoCtrl {

    public static final String C_DATA_SHOWED_FILES_TXT = GlobaleEinstellungen.getDataPfadJUser("ShowedFiles.txt");

    @Getter
    private static JFrame frame;

    @Getter
    private final Container toolBar;

    @Getter
    private final IniFile iniFile;
    @Getter
    private final GuiDecorator guiDecorator;
    private final VlcVideoForm vlcVideoForm;
    private final JComboBox<String> cbDirectories;
    private final VlcVideoManager vlcManager;
    List<String> allFileNames;

    boolean shuffle = true;

    public static void main(String[] args) {
        new VlcVideoCtrl();
    }

    public VlcVideoCtrl() {
        vlcVideoForm = new VlcVideoForm();

        iniFile = IniFileProvider.createIniFile(GlobaleEinstellungen.getDataPfadJUser("VlcVideoCtrl.ini"));
        vlcManager = new VlcVideoManager(iniFile);

        vlcVideoForm.setDeletedFiles(vlcManager.getDeletedFileNames());

        // vlcVideoForm.setFileNames(allFileNames);
        toolBar = vlcVideoForm.getMainToolBar();
        frame = vlcVideoForm.getFrame();
        initIcon();
        toolBar.invalidate();
        toolBar.repaint();


        guiDecorator = new GuiDecorator(frame, iniFile, "Einstellungen");
        cbDirectories = decorate(vlcManager.getVerzeichnisse());

        IComponentPersister persister = ComponentPersisterFactory.createIniFilePersister(iniFile);
        persister.addComponentItem(cbDirectories,"SelectedVerzeichnis");
        persister.loadAndSetComponentItems();
        guiDecorator.addCloseAction(persister::persistComponentItems);

    }

    private JComboBox<String> decorate(List<String> verzeichnisse) {
        final JComboBox<String> cbDirectories;
        frame.setIconImage(guiDecorator.getImage(IC.Triangle_Red));


        guiDecorator.addContainer("MainToolbar", toolBar);

        guiDecorator.addEditIniFileButton("MainToolbar");

        cbDirectories = guiDecorator.addToolbarComboBox("MainToolbar", "Verzeichnisse", verzeichnisse, x -> handleChangedVerzeichnis(x));

        guiDecorator.addToolbarButton("MainToolbar", "Refresh", IC.REFRESHPAGE, e -> DeleteShowedFiles());
        guiDecorator.addToolbarButton("MainToolbar", "Refresh", IC.REFRESHPAGE, e -> ShowActVlcFile());

        guiDecorator.addMouseDoubleClickAction(vlcVideoForm.getLstFiles(), this::playSelectedFiles);
        guiDecorator.addKeyPressAction(vlcVideoForm.getLstFiles(),"N",()->SelectAndPlayNextFiles(1));
        guiDecorator.addKeyPressAction(vlcVideoForm.getLstFiles(),"P",()->SelectAndPlayNextFiles(-1));

        guiDecorator.addContainer("FilesToolBar", vlcVideoForm.getFilesToolBar());
        guiDecorator.addToolbarButton("FilesToolBar", "Play files", IC.MB_PLAY, e -> SelectAndPlayNextFiles(0));
        guiDecorator.addToolbarButton("FilesToolBar", "Select previous files", IC.PREV, e -> SelectAndPlayNextFiles(-1));
        guiDecorator.addToolbarButton("FilesToolBar", "Select next files", IC.NEXT, e -> SelectAndPlayNextFiles(1));
        guiDecorator.addToolbarButton("FilesToolBar", "Shuffle Files", IC.MALE_USER, e -> ShuffleFiles());
        guiDecorator.addToolbarButton("FilesToolBar", "Delete selected files", ICF.Database_Delete, e -> markFileAsDeleted());
        guiDecorator.addToolbarButton("FilesToolBar", "Refresh", IC.REFRESHPAGE, e -> DeleteShowedFiles());


        guiDecorator.addContainer("DeletedFilesToolBar", vlcVideoForm.getDeletedFilesToolBar());
        guiDecorator.addToolbarButton("DeletedFilesToolBar", "Play files", IC.MB_PLAY, e -> playDeletedFiles());
        guiDecorator.addToolbarButton("DeletedFilesToolBar", "Undo delete", ICF.UndoBlue, e -> UndoDelete());
        guiDecorator.addToolbarButton("DeletedFilesToolBar", "Delete selected files", ICF.DeleteRed, e -> deleteSelectedDeletedFiles());


        guiDecorator.addContainer("FavoritenToolBar", vlcVideoForm.getFavoritenToolBar());
        guiDecorator.addToolbarButton("FavoritenToolBar", "Play files", IC.MB_PLAY, e -> SelectAndPlayNextFiles(0));



        return cbDirectories;
    }


    private void deleteSelectedDeletedFiles() {
        List<String> selectedFileNames = vlcVideoForm.getSelectedDeletedFiles();
        for(String selectedFileName : selectedFileNames) {
            if (vlcManager.deleteFile(selectedFileName))
                vlcVideoForm.deleteDeletedFileName(selectedFileName);
        }
    }

    private void deleteSelectedFiles() {
        List<String> selectedFileNames = vlcVideoForm.getSelectedFiles();
        for(String selectedFileName : selectedFileNames) {
            if (new File(selectedFileName).delete())
                vlcVideoForm.deleteFileName(selectedFileName);
        }
    }

    private void UndoDelete() {
        List<String> deletedSelectedFiles = vlcVideoForm.getSelectedDeletedFiles();
        for(String deletedFile : deletedSelectedFiles) {
            vlcManager.removeDeleteFile(deletedFile);
            vlcVideoForm.removeDeletedFile(deletedFile);
        }
        vlcManager.saveDeletedFiles();
    }

    private void markFileAsDeleted() {
        List<String> fileNames = vlcVideoForm.getSelectedFiles();
        List<String> deletedFiles = vlcManager.getDeletedFileNames();

        for(String fileName : fileNames) {
            if (!deletedFiles.contains(fileName)) {
                vlcVideoForm.addDeletedFileName(fileName);
                vlcVideoForm.deleteFileName(fileName);
            }
        }
        vlcManager.saveDeletedFiles();
    }

    private static void initIcon() {
        Image image = new ImageIcon(ICF.Tablet.getImage(24)).getImage();
        frame.setIconImage(image);
        if (GlobaleEinstellungen.isMacOs()) {
            Taskbar taskbar = Taskbar.getTaskbar();
            taskbar.setIconImage(image);
        }
    }

    private List<String> getSelectedFileNames()
    {
        return vlcVideoForm.getSelectedFiles();
    }


    private String getVerzeichnis() {
        return cbDirectories.getSelectedItem().toString();
    }

    private void ShowActVlcFile() {
        test();
    }

    public void test() {
        try {
            String[] cmd = {
                    "osascript",
                    "-e",
                    "tell application \"VLC\" to get path of current item"
            };

            Process process = Runtime.getRuntime().exec(cmd);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Aktuelle Datei: " + line);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleChangedVerzeichnis(String dirName) {
        vlcManager.setVerzeichnis(dirName);

        List<String> temp = vlcManager.loadAllFileNames();
        if (shuffle)
            Collections.shuffle(temp);
        setFileNames(temp);
    }

    private void setFileNames(List<String> fileFullNames) {

        vlcVideoForm.setFileNames(fileFullNames);
    }


    private void DeleteShowedFiles() {
        File file = new File(C_DATA_SHOWED_FILES_TXT);
        if (file.exists()) {
            file.delete();
        }
    }

    private void ShuffleFiles() {
        ArrayList<String> shuffledFiles = new ArrayList<>(allFileNames);
        Collections.shuffle(shuffledFiles);
        vlcVideoForm.setFileNames(shuffledFiles);
    }

    private void SelectAndPlayNextFiles(int i) {
        JListUtils.selectNextBlock(vlcVideoForm.getLstFiles(), i);
        playSelectedFiles();
    }

    List<String> showedFiles = new ArrayList<>();
    private void loadVideoFiles(String fileName) {
        List<String> temp = FileUtil.getFileNamesAll(fileName);

        Collections.shuffle(temp);



        try {
            showedFiles = FileUtil.readLines("UTF-8", C_DATA_SHOWED_FILES_TXT);
        } catch (IOException e) {
            showedFiles = new ArrayList<>();
        }


        allFileNames = temp
                .stream()
                .filter(this::isAllowdVideo)
                .filter(x -> !(showedFiles.contains(x)))
                .toList();
        out.println("Loaded " + allFileNames.size() + " files");
    }

    private boolean isAllowdVideo(String s) {
        String lowerCase = s.toLowerCase();
        if (lowerCase.endsWith(".ebindex"))
            return false;

        return !lowerCase.endsWith(".ini");
    }

    private void playDeletedFiles() {
        new MusicPlayer().play(vlcVideoForm.getSelectedDeletedFiles());
    }


    private void playSelectedFiles() {
        List<String> selectedFileNames = getSelectedFileNames();
        if (selectedFileNames == null || selectedFileNames.isEmpty()) {
            return;
        }

        JListUtils.selectLineInterval(vlcVideoForm.getLstFiles(), 8);
        selectedFileNames = getSelectedFileNames();

        saveShowedFiles(selectedFileNames);
        new MusicPlayer().play(selectedFileNames);
    }

    private void saveShowedFiles(List<String> selectedFileNames) {

        StringBuilder builder = new StringBuilder();
        for(String s : selectedFileNames) {
            builder.append(s);
            builder.append("\r\n");
        }
        FileUtil.appendLine(C_DATA_SHOWED_FILES_TXT, builder.toString());
    }
}
