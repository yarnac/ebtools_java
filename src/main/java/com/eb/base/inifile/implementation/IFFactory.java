package com.eb.base.inifile.implementation;

import com.eb.base.inifile.api.IniFile;

public class IFFactory {

    public IniFile createIniFile(String fileName)
    {
        IniFile iniFile = new IniFileImpl(fileName);
        iniFile.Read();
        return iniFile;
    }
}
