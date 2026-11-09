package com.eb.apps.ebvlc.app;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.inifile.api.IniFile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class VlcVideoManager {

    private final String favoriten;
    private final List<String> deletedFileNames;
    private final List<String> verzeichnisse;
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
        deletedFileNames = iniFile.getSectionValues("DeletedFiles");
        lastVerzeichnis = iniFile.getSectionValue("Einstellungen", "Verzeichnis", "");
        verzeichnisse = iniFile.getSectionValues("Verzeichnisse");
        favoriten = iniFile.getSectionValue("Einstellungen", "Favoriten", "");

    }

    public void setVerzeichnis(String fileName) {
        iniFile.setSectionValue("Einstellungen", "Verzeichnis", fileName);
        if (!fileName.equals(lastVerzeichnis)) {
            lastVerzeichnis = fileName;
            iniFile.setSectionValue("Einstellungen", "Verzeicnis", lastVerzeichnis);
            iniFile.Write();
        }
    }

    public void save() {
        iniFile.setSectionValues("DeletedFiles", getDeletedFileNames());
        iniFile.Write();
    }
}
