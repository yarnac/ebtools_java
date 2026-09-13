package com.eb.base.gui.persist;

import com.eb.base.inifile.api.IniFile;

public class ComponentPersisterFactory {

    public static IComponentPersister createIniFilePersister(IniFile iniFile)
    {
        ComponentPersister persister = new ComponentPersister();
        persister.setStringPersister(new IniFilePersister(iniFile));
        return persister;
    }
}
