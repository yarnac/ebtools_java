package com.eb.doubletten.actual.duplicates.api;

import java.util.List;

public interface IDuplikatContainer {
    String getLabel();
    Object getKey();
    List<String> getDuplikate();
}
