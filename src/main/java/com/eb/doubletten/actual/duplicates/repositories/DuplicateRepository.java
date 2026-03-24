package com.eb.doubletten.actual.duplicates.repositories;

import com.eb.doubletten.actual.duplicates.api.IDuplikatContainer;

import java.util.List;

public class DuplicateRepository {

    private List<? extends IDuplikatContainer> containers = List.of();

    public void setContainers(List<? extends IDuplikatContainer> containers) {
        this.containers = containers;
    }

    public List<? extends IDuplikatContainer> getContainers() {
        return containers;
    }
}
