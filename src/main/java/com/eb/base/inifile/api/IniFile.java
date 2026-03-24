package com.eb.base.inifile.api;

import java.util.List;
import java.util.function.BiConsumer;

public interface IniFile {



    @Override
    String toString();

    void sectionValuesDo(String sectionName, BiConsumer<String, String> consumer);

    void Write();

    void Read();

    // IniFileSection getSection(String p, boolean create);

    String getSectionValue(String section, String value, String defaultString);

    List<String> getSectionValues(String string);

    List<String> getSectionValues(String string, boolean createSection);

    void setSectionValues(String string, List<String> list);

    void setSectionValue(String sectionName, String topic, String defaultValue);

    Integer getSectionValueAsInteger(String section, String name, Integer def);

    void setSectionValue(String sectionName, String topic, int x);

    boolean hasSection(String string);

    String getFileName();
}
