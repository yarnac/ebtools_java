package com.eb.vlc.domain;

import com.eb.base.inifile.api.IniFile;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VlcItemPoolConfig {
    private IniFile iniFile;
    private String itemFileName;
    private List<VlcItem> entries;
    private List<VlcItem> modifiedEntries;
    private boolean modified;
}
