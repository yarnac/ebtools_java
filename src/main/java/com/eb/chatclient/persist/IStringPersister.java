package com.eb.chatclient.persist;

public interface IStringPersister {
    void setString(String key, String value);
    String getString(String key);

    void commit();
}
