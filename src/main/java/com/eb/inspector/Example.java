/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.inspector;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Example {

    String name;
    Example parent;
    List<Example> children = new ArrayList<>();
    java.util.Date dateTime;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }


    public static Example createExample()
    {
        Example root = new Example();
        root.name = "RootExample";
        root.addChild("Child 1");
        root.addChild("Child 2");
        return root;
    }

    private void addChild(String s) {
        Example child = new Example();
        child.name = s;
        children.add(child);
    }

    public int getChildrenCount(){
        return children.size();
    }

}
