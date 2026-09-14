package com.eb.apps.ebchatclient.domain.chat;

import com.eb.base.io.FileUtil;

import java.util.List;
import java.util.stream.Collectors;

public class FileHelpers {
    public static void ensureDirectory(String chunkPath) {

    }

    public static List<String> getAllFiles(String directory, String s1) {
        return FileUtil
                .getFileNamesAll(directory)
                .stream()
                .filter(f -> FileUtil.getFileName(f).equals(s1))
                .collect(Collectors.toUnmodifiableList());
    }
}
