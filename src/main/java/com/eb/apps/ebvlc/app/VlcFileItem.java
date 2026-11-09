package com.eb.apps.ebvlc.app;

import com.eb.base.extensions.FileExtensions;
import lombok.Getter;

import java.util.Objects;

@Getter
public class VlcFileItem {

    private final String name;
    private final String fileName;

    public VlcFileItem(String newFileName) {
        fileName = newFileName;
        name = FileExtensions.ebLocalFileName(fileName);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        VlcFileItem that = (VlcFileItem) o;
        return Objects.equals(fileName, that.fileName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fileName);
    }
}
