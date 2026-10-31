package com.eb.inspector.domain.attributes;

import java.util.function.Function;

public final class InspectMappingAttribute<T>  {
    private final String name;
    private final Function<T, ?> valueProvider;

    public InspectMappingAttribute(String name, Function<T, ?> valueProvider) {
        this.name = name;
        this.valueProvider = valueProvider;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public <R> R get(T instance, Class<R> valueType) {
        return (R) valueProvider.apply(instance);
    }

    public Function<T, ?> getValueProvider() {
        return valueProvider;
    }
}