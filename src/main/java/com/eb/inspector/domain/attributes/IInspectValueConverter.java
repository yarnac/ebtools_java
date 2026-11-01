package com.eb.inspector.domain.attributes;

public interface IInspectValueConverter {
    public Object convertValue(Object value, InspectMapping<?> mapping);
}
