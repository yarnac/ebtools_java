package com.eb.doubletten.actual.duplicates.domain;

import java.util.List;

public class SizeHashcodeDuplicateContainer extends ListBasedDuplikatContainer {
    private String hashCode;

    public SizeHashcodeDuplicateContainer(String label, List<String> duplikate) {
        super(label, duplikate);
    }

    @Override
    public String getLabel() {
        if (getDuplikate().isEmpty()) {
            return "<keine>";
        }
        return getDuplikate().get(0);
    }

    @Override
    public Object getKey() {
        return getLabel();
    }
}
