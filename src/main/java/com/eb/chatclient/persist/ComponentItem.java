package com.eb.chatclient.persist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class ComponentItem <T>  {
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


