package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

import com.eb.apps.ebchat_client.components.JasonFileContentProvider;
import com.eb.base.extensions.FileExtensions;
import com.eb.base.extensions.StringExtensions;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class JsonFileAppendUtil {

    // File Prefix Constants
    private static final String FILE_PREFIX = "<$ ";
    private static final String DIRECTORY_PREFIX = "<$D ";
    private static final String ALL_FILES_PREFIX = "<$DA ";
    private static final String IMAGE_FILE_PREFIX = "<$P ";

    // Line Separator Constants
    private static final String LINE_SEPARATOR = "\n";
    private static final String DIRECTORY_MARKER = LINE_SEPARATOR + DIRECTORY_PREFIX;
    // File Filter Constants
    private static final String ALL_FILES_FILTER = "*";

    private static final HashSet<Path> allowedPaths = new HashSet<>();


    public static void addAllowedDirectories(Iterable<String> directories) {
        for (String dir : directories) {
            Path dirPath = Paths.get(dir).toAbsolutePath().normalize();
            allowedPaths.add(dirPath);
        }
    }


    public static String appendDirectoryFiles(String inputString) {
        if (StringExtensions.ebIsNilOrEmpty(inputString))
            return "";
        
        if (!inputString.contains(DIRECTORY_MARKER))
            return inputString;

        StringBuilder strb = new StringBuilder();
        String[] lines = inputString.split(LINE_SEPARATOR);

        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith(DIRECTORY_PREFIX)) {
                String dirName = StringExtensions.ebTrimAll(line.substring(DIRECTORY_PREFIX.length()));
                if (!isAllowedFileOrDirName(dirName))
                    continue;
                List<String> fileNames = Arrays.stream(FileExtensions.ebGetFiles(dirName, ALL_FILES_FILTER)).toList();
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

    private static boolean isAllowedFileOrDirName(String name) {
        if (Paths.get(name).isAbsolute())
            return isInAllowedDirectory(Paths.get(name));
        return false;
    }

    private static boolean isInAllowedDirectory(Path inputPath) {
        Path normalized = inputPath.toAbsolutePath().normalize();
        String normStr = normalized.toString();

        return allowedPaths.stream().anyMatch(allowedDir -> {
            String dirStr = allowedDir.toString();
            // Exakte Übereinstimmung oder Start mit Verzeichnis-Separator
            return normStr.equals(dirStr) || normStr.startsWith(dirStr + FileSystems.getDefault().getSeparator());
        });
    }

    public static String appendFiles(String inputString, List<String> imageFileNamesFromUser) {

        List<String> imageFileNames = imageFileNamesFromUser == null
            ? new ArrayList<>()
            : new ArrayList<>(imageFileNamesFromUser);

        Set<String> fileNames = new HashSet<>();
        StringBuilder stringBuilder = new StringBuilder();


        String[] lines = inputString.split(LINE_SEPARATOR);
        for (String line : lines) {
            if (isAllowedFileOrDirName(line))
            {
                fileNames.add(line.trim());
                continue;
            }
            if (line.startsWith(IMAGE_FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(line.substring(IMAGE_FILE_PREFIX.length()));
                if (isAllowedFileOrDirName(fileName) && !imageFileNames.contains(fileName))
                    imageFileNames.add(fileName.trim());
                continue;
            }
            if (line.startsWith(FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(line.substring(FILE_PREFIX.length()));
                if (isAllowedFileOrDirName(fileName))
                    fileNames.add(fileName.trim());
                continue;
            }

            stringBuilder.append(line);
            stringBuilder.append(LINE_SEPARATOR);
        }

        try {
            stringBuilder.append(new JasonFileContentProvider().getJsonFileString(fileNames.stream().toList()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    public static String appendAllFiles(String content) {
        return  appendFiles(appendDirectoryFiles(content), new ArrayList<>());
    }
}