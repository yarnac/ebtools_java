package com.eb.apps.ebchatclient.components;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.extensions.StringExtensions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonFileAppendUtil {

    // File Prefix Constants
    private static final String FILE_PREFIX = "<$ ";
    private static final String DIRECTORY_PREFIX = "<$D ";
    private static final String ALL_FILES_PREFIX = "<$DA ";
    private static final String IMAGE_FILE_PREFIX = "<$P ";

    // Line Separator Constants
    private static final String LINE_SEPARATOR = File.separator;
    private static final String FILE_MARKER = LINE_SEPARATOR + FILE_PREFIX;
    private static final String DIRECTORY_MARKER = LINE_SEPARATOR + DIRECTORY_PREFIX;
    private static final String ALL_FILES_MARKER = LINE_SEPARATOR + ALL_FILES_PREFIX;

    // File Filter Constants
    private static final String JAVA_FILTER = "*.java";
    private static final String ALL_FILES_FILTER = "*.*";

    public static String appendDirectoryFiles(String inputString) {
        if (!inputString.contains(DIRECTORY_MARKER))
            return inputString;

        StringBuilder strb = new StringBuilder();
        String[] lines = inputString.split(LINE_SEPARATOR);

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith(DIRECTORY_PREFIX)) {
                String dirName = StringExtensions.ebTrimAll(line.substring(DIRECTORY_PREFIX.length()));
                List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, JAVA_FILTER);
                fileNames.forEach(fileName -> {
                    strb.append(FILE_PREFIX).append(fileName).append(LINE_SEPARATOR);
                });
            } else if (trimmedLine.startsWith(ALL_FILES_PREFIX)) {
                String dirName = StringExtensions.ebTrimAll(line.substring(ALL_FILES_PREFIX.length()));
                List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, ALL_FILES_FILTER);
                fileNames.forEach(fileName -> {
                    strb.append(FILE_PREFIX).append(fileName).append(LINE_SEPARATOR);
                });
            } else {
                strb.append(line);
                strb.append(LINE_SEPARATOR);
            }
        }
        return strb.toString();
    }

    public static String appendFiles(String inputString, List<String> imageFileNames) {

        List<String> dateien = new ArrayList<>();
        StringBuilder strb = new StringBuilder();

        if (!inputString.contains(FILE_MARKER))
            return inputString;

        String[] lines = inputString.split(LINE_SEPARATOR);
        for (String line : lines) {
            if (line.startsWith(IMAGE_FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(line.substring(IMAGE_FILE_PREFIX.length()));
                imageFileNames.add(fileName.trim());
                continue;
            }
            if (line.startsWith(FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(line.substring(FILE_PREFIX.length()));
                dateien.add(fileName.trim());
                continue;
            }

            strb.append(line);
            strb.append(LINE_SEPARATOR);
        }

        try {
            strb.append(new JasonFileContentProvider().getJsonFileString(dateien));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return strb.toString();
    }
}