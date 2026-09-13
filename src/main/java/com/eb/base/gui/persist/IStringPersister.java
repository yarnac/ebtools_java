package com.eb.base.gui.persist;

interface IStringPersister {
    void setString(String key, String value);
    String getString(String key);

    void commit();
}
