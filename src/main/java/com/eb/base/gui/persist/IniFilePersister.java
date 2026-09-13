package com.eb.base.gui.persist;

import com.eb.base.inifile.api.IniFile;

class IniFilePersister implements IStringPersister{

    private IniFile iniFile;
    private String sectionName;

    public IniFilePersister(IniFile iniFile) {
        this.iniFile = iniFile;
        this.sectionName = "Control-States";

    }

    public IniFilePersister(IniFile iniFile, String sectionName) {
        this.iniFile = iniFile;
        this.sectionName = sectionName;
    }

    @Override
    public void setString(String key, String value) {
        iniFile.setSectionValue(sectionName, key, value);
    }

    @Override
    public String getString(String key) {
        return iniFile.getSectionValue(sectionName, key, "");
    }

    @Override
    public void commit() {
        iniFile.Write();
    }
}
