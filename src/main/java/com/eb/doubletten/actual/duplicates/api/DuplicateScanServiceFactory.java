package com.eb.doubletten.actual.duplicates.api;

import com.eb.doubletten.actual.duplicates.services.FilenameDuplicateScanService;
import com.eb.doubletten.actual.duplicates.services.SizeHashcodeDuplicateScanService;

public class DuplicateScanServiceFactory {
    public enum DuplicateScanServiceType {
        FileName,
        SizeAndFileName,
        HashCode
    }

    public static IDuplicateScanService getDuplicateScanService(final DuplicateScanServiceType type) {

        switch (type) {
            case FileName:
                return new FilenameDuplicateScanService();
            case SizeAndFileName:
                return null;
            case HashCode:
                return new SizeHashcodeDuplicateScanService();
        }
        throw new IllegalArgumentException("Invalid duplicate scan service type: " + type);
    }
}
