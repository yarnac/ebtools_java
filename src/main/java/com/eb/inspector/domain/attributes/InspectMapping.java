package com.eb.inspector.domain.attributes;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class InspectMapping<T> {
    private String name;
    private Class theClass;
    private Function<T, ?> toStringFunction;
    private Function<T, List<Object>> getSubObjectsFunction;



    List<InspectMappingAttribute> attributes;

    public InspectMapping(Class aClass) {
        name = aClass.getSimpleName();
    }

    public String getName(T o) {
        if (toStringFunction == null)
            return getName();
        return getName() + ": " + toStringFunction.apply(o);
    }

    public List getSubObjects(T obj) {
        if (getSubObjectsFunction == null)
            return new ArrayList<>();
        return getSubObjectsFunction.apply(obj);
    }
}
