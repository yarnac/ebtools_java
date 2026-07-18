package com.eb.android;

import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
@Setter
public class DialogItem {
    private final String name;
    private final DialogItemType type;
    private final Object [] enumType;
    private final Object defaultValue;
    private final Supplier<Object> readFunction;
    Consumer<Object> writeFunction;

    public DialogItem(String name, DialogItemType type, Consumer<Object> writeFunction, Supplier<Object> readFunction) {

        this.name = name;
        this.type = type;
        this.enumType = null;
        this.defaultValue = null;
        this.writeFunction = writeFunction;
        this.readFunction = readFunction;
    }

    public DialogItem(String name, DialogItemType type, Object[] enumType, Enum<?> defaultValue, Consumer<Object> writeFunction, Supplier<Object> readFunction) {

        this.name = name;
        this.type = type;
        this.enumType = enumType;
        this.defaultValue = defaultValue;
        this.writeFunction = writeFunction;
        this.readFunction = readFunction;

    }


    public void setWert(String wert) {
        writeFunction.accept(wert);
    }

    public Object getWert()
        {
            return readFunction.get();
        }
}
