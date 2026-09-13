package com.eb.chatclient.persist;

import java.awt.*;

public interface IComponentPersister {

    void setStringPersister(IStringPersister persister);
    void addComponentItem(Component component, String key);
    void removeComponentItem(Component component);
    Component getComponentItem(String key);

    void persistComponentItems();
    void loadAndSetComponentItems();
}
