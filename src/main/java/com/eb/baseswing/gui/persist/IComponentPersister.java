package com.eb.baseswing.gui.persist;

public interface IComponentPersister {

    void addComponentItem(Object component, String key);

    void removeComponentItem(Object component);
    Object getComponentItem(String key);
    void persistComponentItems();
    void loadAndSetComponentItems();
}
