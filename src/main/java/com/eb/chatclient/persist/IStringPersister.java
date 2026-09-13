package com.eb.chatclient.persist;

public interface IStringPersister {
    void persistString(String key, String value);
    String getString(String key);
}
