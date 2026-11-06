/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

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