package com.eb.base.gui.persist;

import java.util.function.Consumer;
import java.util.function.Supplier;


class ComponentItem <T>  {
    T component;
    String key;
    Supplier<String> getValue;
    Consumer<String> setValue;

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }



    public String getString()
    {
        return getValue.get();
    }

    public void setString(String value)
    {
        setValue.accept(value);
    }

    public ComponentItem(T component, String key, Supplier<String> getValue, Consumer<String> setValue)
    {
        this.component = component;
        this.key = key;
        this.getValue = getValue;
        this.setValue = setValue;
    }
}


