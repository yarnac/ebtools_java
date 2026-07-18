package com.eb.ai_service;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

public class AiServiceConfig {
    private static AiServiceConfig instance;
    private IniFile iniFile;

    public static AiServiceConfig current()
    {
        if (instance==null)
            instance = new AiServiceConfig();

        return instance;
    }

    private AiServiceConfig() {
        iniFile = IniFileProvider.createIniFile("AiServiceConfig.ini");
    }

    public IniFile getIniFile() {
        return iniFile;
    }


    public String getModelFileName() {
        return iniFile.getSectionValue("Einstellungen", "AiModelFileName", "");
    }

    public String getGeheimnisFileName() {
        return iniFile.getSectionValue("Einstellungen", "AiGeheimnisFileName", "");
    }

    public String getData1() {
        return iniFile.getSectionValue("Data", "Data1", "");
    }

    public String getData2() {
        return iniFile.getSectionValue("Data", "Data2", "");
    }

    public String getTokenFileName() {
        return iniFile.getSectionValue("Einstellungen", "AiTokenFileName", "");
    }
}
