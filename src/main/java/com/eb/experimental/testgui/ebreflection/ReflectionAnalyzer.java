package com.eb.experimental.testgui.ebreflection;

import java.util.ArrayList;
import java.util.List;

public class ReflectionAnalyzer {
    List<String> methods;
    List<String> attributes;

    public ReflectionAnalyzer(Class theClass) {
        methods = new ArrayList<String>();
        attributes  = new ArrayList<String>();

        theClass.getMethods();
    }

}
