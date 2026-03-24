package com.eb.vlc.domain;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import lombok.Getter;

import java.io.File;
import java.util.ArrayList;


public class VlcItemPool {
    @Getter
    private final VlcItemPoolConfig config;

    VlcItemPool(String inifileName)
    {
        config = new VlcItemPoolConfig();
        config.setIniFile(IniFileProvider.createIniFile(inifileName));
        config.setEntries(new ArrayList<>());
        config.setModifiedEntries(new ArrayList<>());
        config.setModified(false);
    }
}
