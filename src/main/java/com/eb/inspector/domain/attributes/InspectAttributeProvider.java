package com.eb.inspector.domain.attributes;

import java.util.ArrayList;
import java.util.List;

import com.eb.inspector.Example;

/**
 * Generischer Provider, der Attribute für eine Zielklasse T bereitstellt.
 * Die Typparameter-Kette bleibt somit vom Compiler streng validiert.
 */
public final class InspectAttributeProvider {

    public static InspectMapping getInspectMapping(Class aClass) {
        if (aClass == Example.class)
        {
            InspectMapping<Example> mapping = new InspectMapping<Example>(aClass);
            mapping.setToStringFunction(Example::getName);
            mapping.setGetSubObjectsFunction(x -> getListOfSubobjects(x.getParent(), x.getChildren()));
            mapping.setAttributes(List.of(
                    new InspectMappingAttribute<Example>("name", Example::getName),
                    new InspectMappingAttribute<Example>("parent", Example::getParent),
                    new InspectMappingAttribute<Example>("children", Example::getChildren)));
            return mapping;
        }
        return GenericsInspectAttributeProvider.getMapping(aClass);

    }

    private static List getListOfSubobjects(Object ...obj) {
        List list = new ArrayList();
        for(Object o : obj)
            if (o != null)
                list.add(o);
        return list;
    }
}