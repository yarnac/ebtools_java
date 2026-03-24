package com.eb.vlc.gui;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.FileUtil;
import com.eb.ebmusic.tobj.MusicPlayer;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.lang.System.out;

public class VlcVideoCtrl {

    public static final String C_DATA_SHOWED_FILES_TXT = "c:\\data\\ShowedFiles.txt";

    @Getter
    private static JFrame frame;

    @Getter
    private final Container toolBar;

    @Getter
    private final IniFile iniFile;
    @Getter
    private final GuiDecorator guiDecorator;
    private final VlcVideoForm vlcVideoForm;
    List<String> allFileNames;

    public static void main(String[] args) {

        new VlcVideoCtrl();
    }

    public VlcVideoCtrl() {
        vlcVideoForm = new VlcVideoForm();
        iniFile = IniFileProvider.createIniFile("VlcVideos.ini");

        loadVideoFiles();
        ShuffleFiles();
        // vlcVideoForm.setFileNames(allFileNames);
        toolBar = vlcVideoForm.getToolBar();
        frame = vlcVideoForm.getFrame();

        guiDecorator = new GuiDecorator(frame, iniFile, "Einstellungen");
        frame.setIconImage(guiDecorator.getImage(IC.Triangle_Red));

        guiDecorator.addContainer("MainToolbar", toolBar);

        guiDecorator.addEditIniFileButton("MainToolbar");
        List<String> verzeichnisse = iniFile.getSectionValues("Verzeichnisse");
        guiDecorator.addToolbarComboBox("MainToolbar", "Verzeichnisse", verzeichnisse, x -> handleChangedVerzeichnis(x));
        guiDecorator.addToolbarButton("MainToolbar", "Play files", IC.MB_PLAY, e -> playSelectedFiles());
        guiDecorator.addToolbarButton("MainToolbar", "Select previous files", IC.PREV, e -> SelectAndPlayNextFiles(-1));
        guiDecorator.addToolbarButton("MainToolbar", "Select next files", IC.NEXT, e -> SelectAndPlayNextFiles(1));
        guiDecorator.addToolbarButton("MainToolbar", "Select next files", IC.MALE_USER, e -> ShuffleFiles());
        guiDecorator.addToolbarButton("MainToolbar", "Refresh", IC.REFRESHPAGE, e -> DeleteShowedFiles());

        guiDecorator.addMouseDoubleClickAction(vlcVideoForm.getLstFiles(), this::playSelectedFiles);
        guiDecorator.addKeyPressAction(vlcVideoForm.getLstFiles(),"N",()->SelectAndPlayNextFiles(1));
        guiDecorator.addKeyPressAction(vlcVideoForm.getLstFiles(),"P",()->SelectAndPlayNextFiles(-1));

        toolBar.invalidate();
        toolBar.repaint();
    }

    private void handleChangedVerzeichnis(String x) {
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

    List<String> showedFiles = null;
    private void loadVideoFiles() {
        List<String> temp = FileUtil.getFileNamesAll("d:\\Medien\\dwhelper2\\dwhelper");
        temp.sort(Comparator.naturalOrder());


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


    private void playSelectedFiles() {
        List<String> selectedFileNames = vlcVideoForm.getSelectedFiles();
        if (selectedFileNames.size()<=1)
        {
            JListUtils.selectLineInterval(vlcVideoForm.getLstFiles(), 8);
            selectedFileNames = vlcVideoForm.getSelectedFiles();
        }

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
