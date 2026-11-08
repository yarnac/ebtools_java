package com.eb.apps.ebvlc.app;

import com.eb.base.inifile.api.IniFile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VlcVideoManager {

    private final String favoriten;
    private final List<String> deletedFiles;
    private final List<String> verzeichnisse;
    private final IniFile iniFile;

    private String lastVerzeichnis;

    public VlcVideoManager(IniFile iniFile) {
        this.iniFile = iniFile;
        lastVerzeichnis = iniFile.getSectionValue("Einstellungen", "Verzeichnis", "");
        verzeichnisse = iniFile.getSectionValues("Verzeichnisse");
        favoriten = iniFile.getSectionValue("Einstellungen", "Favoriten", "");
        deletedFiles = iniFile.getSectionValues("DeletedFiles");
    }

    public void setVerzeichnis(String fileName) {
        iniFile.setSectionValue("Einstellungen", "Verzeichnis", fileName);
        if (!fileName.equals(lastVerzeichnis)) {
            lastVerzeichnis = fileName;
            iniFile.setSectionValue("Einstellungen", "Verzeicnis", lastVerzeichnis);
            iniFile.Write();
        }
    }
}
