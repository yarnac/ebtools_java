package com.eb.apps.ebvlc.app;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.inifile.api.IniFile;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class VlcVideoManager {

    private final String favoriten;
    private List<String> deletedFileNames;
    private List<String> verzeichnisse;
    private final IniFile iniFile;

    private String lastVerzeichnis;

    public List<String> loadAllFileNames(String dirName)
    {

        return FileExtensions.ebGetAllFiles(dirName)
                .stream()
                .filter(x -> !(FileExtensions.ebLocalFileName(x).startsWith(".") || deletedFileNames.contains(x)))
                .collect(Collectors.toList());
    }



    public List<String> loadAllFileNames()
    {
        return loadAllFileNames(getLastVerzeichnis());
    }

    public List<String> loadFavoriten()
    {
        return loadAllFileNames(getFavoriten());
    }



    public VlcVideoManager(IniFile iniFile) {
        this.iniFile = iniFile;

        lastVerzeichnis = iniFile.getSectionValue("Einstellungen", "Verzeichnis", "");
        verzeichnisse = iniFile.getSectionValues("Verzeichnisse");
        favoriten = iniFile.getSectionValue("Einstellungen", "Favoriten", "");

        String delFileFileName = getFileNameDeletedFiles();
        loadDeletedFiles();
    }

    private String getFileNameDeletedFiles() {
        return FileExtensions.ebFileNameInDirectory(".deletedFiles", getLastVerzeichnis());
    }

    public void setVerzeichnis(String fileName) {
        iniFile.setSectionValue("Einstellungen", "Verzeichnis", fileName);
        if (!fileName.equals(lastVerzeichnis)) {
            lastVerzeichnis = fileName;
            iniFile.setSectionValue("Einstellungen", "Verzeicnis", lastVerzeichnis);
            iniFile.Write();
            loadDeletedFiles();
        }
    }

    public void loadDeletedFiles()
    {

        String delFileFileName = getFileNameDeletedFiles();
        if (FileExtensions.ebFileExists(delFileFileName)) {
            deletedFileNames = Arrays.stream(FileExtensions.ebGetFileTextLines(delFileFileName)).collect(Collectors.toList());
        }
        else
            deletedFileNames = new ArrayList<>();

    }

    public void saveDeletedFiles()
    {
        FileExtensions.ebWriteFileLinesUTF8(getFileNameDeletedFiles(), getDeletedFileNames());
    }

    public void save() {
        iniFile.setSectionValues("DeletedFiles", getDeletedFileNames());
        iniFile.Write();
    }

    public boolean deleteFile(String selectedFileName) {
        if (new File(selectedFileName).delete())
        {
            deletedFileNames.remove(selectedFileName);
            save();
            return true;
        }
        return false;
    }

    public void removeDeleteFile(String deletedFile) {
        deletedFileNames.remove(deletedFile);
    }
}
