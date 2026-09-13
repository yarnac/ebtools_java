package com.eb.chatclient.persist;

import java.awt.*;
import java.util.function.Function;

public interface IComponentPersister {

    void setStringPersister(IStringPersister persister);
    void addComponentItem(Object component, String key);
    IStringPersister getStringPersister();

    void removeComponentItem(Object component);
    Object getComponentItem(String key);
    void persistComponentItems();
    void loadAndSetComponentItems();
}
