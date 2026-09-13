package com.eb.chatclient.domain.chat;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

public class IniFileFactory {
    public IniFile create(String s) {
        return IniFileProvider.createIniFile(s);
    }
}
