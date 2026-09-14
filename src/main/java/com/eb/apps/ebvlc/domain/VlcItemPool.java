package com.eb.apps.ebvlc.domain;

import com.eb.base.inifile.api.IniFileProvider;
import lombok.Getter;

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
