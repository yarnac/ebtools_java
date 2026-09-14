package com.eb.apps.ebchatclient.components.fileprovider;

import javax.swing.*;
import java.util.*;

class FileNameListModel extends AbstractListModel<String> {
    private final List<String> fileNames = new ArrayList<>();
    private final Set<String> uniqueFileNames = new HashSet<>();

    public void addFileName(String fileName) {
        if (fileName != null && !fileName.trim().isEmpty()) {
            String trimmedName = fileName.trim();
            if (!uniqueFileNames.contains(trimmedName)) {
                fileNames.add(trimmedName);
                uniqueFileNames.add(trimmedName);
                int index = fileNames.size() - 1;
                fireIntervalAdded(this, index, index);
            }
        }
    }

    public void addFileNames(List<String> names) {
        if (names == null || names.isEmpty()) {
            return;
        }

        List<String> newNames = new ArrayList<>();
        for (String name : names) {
            if (name != null && !name.trim().isEmpty()) {
                String trimmedName = name.trim();
                if (!uniqueFileNames.contains(trimmedName)) {
                    fileNames.add(trimmedName);
                    uniqueFileNames.add(trimmedName);
                    newNames.add(trimmedName);
                }
            }
        }

        if (!newNames.isEmpty()) {
            int startIndex = fileNames.size() - newNames.size();
            int endIndex = fileNames.size() - 1;
            fireIntervalAdded(this, startIndex, endIndex);
        }
    }

    public void removeElementAt(int index) {
        if (index >= 0 && index < fileNames.size()) {
            String removed = fileNames.remove(index);
            uniqueFileNames.remove(removed);
            fireIntervalRemoved(this, index, index);
        }
    }

    public void clear() {
        int size = fileNames.size();
        fileNames.clear();
        uniqueFileNames.clear();
        if (size > 0) {
            fireIntervalRemoved(this, 0, size - 1);
        }
    }

    public List<String> getFileNames() {
        return new ArrayList<>(fileNames);
    }

    public boolean contains(String fileName) {
        return uniqueFileNames.contains(fileName);
    }

    @Override
    public int getSize() {
        return fileNames.size();
    }

    @Override
    public String getElementAt(int index) {
        return fileNames.get(index);
    }
}
