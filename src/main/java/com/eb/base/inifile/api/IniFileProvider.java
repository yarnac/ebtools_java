package com.eb.base.inifile.api;

import com.eb.base.inifile.implementation.IFFactory;

public class IniFileProvider {

    private static IFFactory factory = new IFFactory();

    public static IniFile createIniFile(String fileName)
    {
        return factory.createIniFile(fileName);
    }
}
