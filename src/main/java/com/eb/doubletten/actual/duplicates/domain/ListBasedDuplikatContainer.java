package com.eb.doubletten.actual.duplicates.domain;

import com.eb.doubletten.actual.duplicates.api.IDuplikatContainer;

import java.util.List;

public class ListBasedDuplikatContainer implements IDuplikatContainer {

    private final String label;
    private final List<String> duplikate;

    public ListBasedDuplikatContainer(String label, List<String> duplikate) {
        this.label = label;
        this.duplikate = duplikate;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public Object getKey() {
        return getLabel();
    }

    @Override
    public List<String> getDuplikate() {
        return duplikate;
    }

    @Override
    public String toString() {
        return getLabel();
    }
}
