package com.eb.base.inifile.api;

import com.eb.base.MainGlobals;
import com.eb.base.inifile.implementation.IFFactory;

public class IniFileProvider {

    private static IFFactory factory = new IFFactory();

    public static IniFile createIniFile(String fileName)
    {
        String dataFileName = MainGlobals.getDataFileName(fileName);
        return factory.createIniFile(dataFileName);
    }
}
