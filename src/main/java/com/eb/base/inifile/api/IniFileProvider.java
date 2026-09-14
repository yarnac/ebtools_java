package com.eb.base.inifile.api;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.inifile.implementation.IFFactory;
import com.eb.chatclient.domain.chat.GlobaleEinstellungen;

public class IniFileProvider {

    private static IFFactory factory = new IFFactory();

    public static IniFile createIniFile(String fileName)
    {
        if (FileExtensions.ebIsValidAbsoluteOsFileName(fileName))
            return  factory.createIniFile(fileName);

        String dataFileName = GlobaleEinstellungen.getDataPfadJUser(fileName);
        return factory.createIniFile(dataFileName);
    }
}
