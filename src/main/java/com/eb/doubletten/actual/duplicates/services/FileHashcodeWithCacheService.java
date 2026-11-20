package com.eb.doubletten.actual.duplicates.services;

import com.eb.base.extensions.FileExtensions;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileHashcodeWithCacheService implements IFileHashcodeService {

    private final Map<String, String> cachedFileHashcodes = new ConcurrentHashMap<>();
    private final Set<Path> indexFiles = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final FileHashcodeService fileHashcodeService = new FileHashcodeService(HashCodeConstants.Index_Buffer_Size);

    @Override
    public String getHashCode(Path file) {
        String absolutePath = file.toAbsolutePath().toString();
        String indexFileName = FileExtensions.ebFileNameInDirectory(HashCodeConstants.Index_File_Name, file.getParent().toString());
        Path indexPath = Path.of(indexFileName);

        if (!indexFiles.contains(indexPath)) {
            appendIndexFile(indexFileName, true);
        }
        return cachedFileHashcodes.computeIfAbsent(absolutePath, x -> createCachedEntry(x, indexFileName));
    }

    private String createCachedEntry(String fileName, String indexFileName) {
        String hash = fileHashcodeService.getHashCode(fileName);
        appendLine(indexFileName, hash + "\t" + Path.of(fileName).getFileName().toString());
        indexFiles.add(Path.of(indexFileName));
        return hash;
    }

    private void appendIndexFile(String indexFileName) {
        appendIndexFile(indexFileName, false);
    }

    private void appendIndexFile(String indexFileName, boolean createFullIndexIfMissing) {
        Path path = Path.of(indexFileName);
        if (!Files.exists(path)) {
            if (!createFullIndexIfMissing) {
                return;
            }
            System.out.println("Indexing " + path.getParent());
            appendDirectoryFilesToIndex(path.getParent());
            return;
        }

        try {
            System.out.println("Reading index " + path.getParent());
            List<String> lines = Files.readAllLines(path);
            String parentDir = path.getParent().toAbsolutePath().toString();

            for (String line : lines) {
                if (line.isEmpty()) continue;
                String[] parts = line.split("\t");
                if (parts.length >= 2) {
                    String fileName = FileExtensions.ebFileNameInDirectory(parts[1], parentDir);
                    cachedFileHashcodes.put(fileName, parts[0]);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read index file: " + path, e);
        }
        indexFiles.add(path);
    }

    private void appendDirectoryFilesToIndex(Path parent) {
        List<Path> files = getFiles(parent);
        StringBuilder stringBuilder = new StringBuilder();
        int total = files.size();
        for (int i = 0; i < total; i++) {
            Path file = files.get(i);
            if (i % 50 == 0) {
                System.out.println(file + "\t(" + i + " / " + total + ")");
            }
            if (Files.isRegularFile(file)) {
                String localFileName = file.getFileName().toString();
                if (isAllowedFileName(localFileName)) {
                    String fileHashCode = fileHashcodeService.getHashCode(file);
                    cachedFileHashcodes.put(file.toAbsolutePath().toString(), fileHashCode);
                    try {
                        long length = Files.size(file);
                        stringBuilder.append("%s\t%s\t%s%n".formatted(fileHashCode, localFileName, length));
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to get file size: " + file, e);
                    }
                }
            }
        }

        try {
            Path indexPath = Path.of(parent.toString() + HashCodeConstants.Index_File_Name);
            Files.write(indexPath, stringBuilder.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write index file: " + parent, e);
        }
    }

    private boolean isAllowedFileName(String localFileName) {
        return !localFileName.startsWith(".");
    }

    private List<Path> getFiles(Path dirName) {
        List<Path> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dirName)) {
            for (Path p : stream) {
                files.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read directory: " + dirName, e);
        }
        return files;
    }

    public static void appendLine(String fileName, String line) {
        Path path = Path.of(fileName);
        try {
            Files.write(path, (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to append line to index: " + path, e);
        }
    }
}