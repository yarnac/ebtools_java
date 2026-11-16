package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

import com.eb.apps.ebchat_client.components.JasonFileContentProvider;
import com.eb.base.extensions.FileExtensions;
import com.eb.base.extensions.StringExtensions;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
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
    // File Filter Constants
    private static final String ALL_FILES_FILTER = "*";

    private static final List<Path> allowedPaths = new ArrayList<>();


    public static void addAllowedDirectories(Iterable<String> directories) {
        if (directories == null) {
            return;
        }
        for (String dir : directories) {
            Path dirPath = null;
            try {
                dirPath = Paths.get(dir).toAbsolutePath().toRealPath().normalize();
                if (Files.isDirectory(dirPath)) {
                    allowedPaths.add(dirPath);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }


    public static String appendDirectoryFiles(String inputString) {

        if (inputString==null)
            return "";

        if (allowedPaths.isEmpty())
            return inputString;

        if (!inputString.contains(DIRECTORY_PREFIX) && !inputString.contains(ALL_FILES_PREFIX)) {
            return inputString;
        }

        StringBuilder strb = new StringBuilder();
        String[] lines = inputString.split(LINE_SEPARATOR);

        for (String untrimmedLine : lines) {
            String trimmedLine = untrimmedLine.trim();

            if (trimmedLine.startsWith(DIRECTORY_PREFIX)) {
                String dirName = StringExtensions.ebTrimAll(trimmedLine.substring(DIRECTORY_PREFIX.length()));
                if (!isAllowedFileOrDirName(dirName)||!Files.isDirectory(Paths.get(dirName))) {
                    continue;
                }
                List<String> fileNames = Arrays.stream(FileExtensions.ebGetFiles(dirName, ALL_FILES_FILTER)).toList();
                fileNames.forEach(fileName -> {
                    strb.append(FILE_PREFIX).append(fileName).append(LINE_SEPARATOR);
                });
            } else if (trimmedLine.startsWith(ALL_FILES_PREFIX)) {
                String dirName = StringExtensions.ebTrimAll(trimmedLine.substring(ALL_FILES_PREFIX.length()));
                if (!isAllowedFileOrDirName(dirName)||!Files.isDirectory(Paths.get(dirName))) {
                    continue;
                }
                List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, ALL_FILES_FILTER);
                fileNames.forEach(fileName -> {
                    strb.append(FILE_PREFIX).append(fileName).append(LINE_SEPARATOR);
                });
            } else {
                strb.append(untrimmedLine);
                strb.append(LINE_SEPARATOR);
            }
        }
        return strb.toString();
    }

    private static boolean isAllowedFileOrDirName(String name) {
        try {
            if (Paths.get(name).isAbsolute())
                return isInAllowedDirectory(Paths.get(name).toRealPath());
            return false;
        }
        catch(Exception e) {
            return false;
        }
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

        if (inputString==null)
            return "";

        if (allowedPaths.isEmpty())
            return inputString;

        Set<String> imageFileNames = determineAllowedImageFileNames(imageFileNamesFromUser);




        Set<String> fileNames = new LinkedHashSet<>();
        StringBuilder stringBuilder = new StringBuilder();


        String[] lines = inputString.split(LINE_SEPARATOR);
        for (String untrimmedLine : lines) {
            String trimmedLine = untrimmedLine.trim();


            if (addAllowedRegularFile(fileNames, trimmedLine))
            {
                continue;
            }
            if (trimmedLine.startsWith(IMAGE_FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(trimmedLine.substring(IMAGE_FILE_PREFIX.length()));
                if (addAllowedRegularFile(imageFileNames, fileName))
                    continue;
            }
            if (trimmedLine.startsWith(FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(trimmedLine.substring(FILE_PREFIX.length()));
                if (addAllowedRegularFile(fileNames, fileName))
                    continue;
            }

            stringBuilder.append(untrimmedLine).append(LINE_SEPARATOR);
        }

        try {
            /* Mehrere JSON Dokumente werden akzeptiert */
            stringBuilder.append(new JasonFileContentProvider().getJsonFileString(fileNames.stream().toList()));
            stringBuilder.append(new JasonFileContentProvider().getJsonFileString(imageFileNames.stream().toList()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stringBuilder.toString();
    }

    private static boolean addAllowedRegularFile(Set<String> fileNames, String fileName) {
        if (isAllowedFileOrDirName(fileName)
                &&  Files.isRegularFile(Paths.get(fileName))) {
            fileNames.add(fileName.trim());
            return true;
        }
        return false;
    }

    private static Set<String> determineAllowedImageFileNames(List<String> imageFileNamesFromUser) {

        Set<String> imageFileNames = new LinkedHashSet<>();
        if (imageFileNamesFromUser == null || imageFileNamesFromUser.isEmpty())
            return imageFileNames;

        for(String imageFileName : imageFileNamesFromUser) {
            if (isAllowedFileOrDirName(imageFileName) && Files.isRegularFile(Paths.get(imageFileName)))
            {
                imageFileNames.add(imageFileName);
            }
        }
        return imageFileNames;
    }

    public static String appendAllFiles(String content) {
        return  appendFiles(appendDirectoryFiles(content), new ArrayList<>());
    }
}