package com.eb.doubletten.actual.duplicates.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FileHashcodeWithCacheService implements IFileHashcodeService {

    final String Index_File_Name = "/.ebsh256_16";
    final long Index_Buffer_Size = 16 * 1024;


    Map<String, String> cachedFileHashcodes = new HashMap<String, String>();
    Set<String> indexFiles = new HashSet<String>();
    private FileHashcodeService fileHashcodeService = new FileHashcodeService(Index_Buffer_Size);

    @Override
    public String getHashCode(Path file) {
        String fileName = file.toAbsolutePath().toString();

        String indexFileName = file.getParent() + "\\" + Index_File_Name;
        if (!indexFiles.contains(indexFileName)) {
            appendIndexFile(indexFileName, true);
        }
        return cachedFileHashcodes.computeIfAbsent(fileName, x -> createCachedEntry(x, indexFileName));
    }

    private String createCachedEntry(String fileName, String indexFileName) {
         String hash = fileHashcodeService.getHashCode(fileName);
        appendLine(indexFileName, hash + "\t" + Path.of(fileName).getFileName().toString());
        indexFiles.add(indexFileName);
        return hash;
    }

    private void appendIndexFile(String indexFileName) {
        appendIndexFile(indexFileName, false);
    }

    private void appendIndexFile(String indexFileName, boolean createFullIndexIfMissing) {

        Path path = Path.of(indexFileName);
        if ( !Files.exists(path) ) {
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

            String parent = path.getParent().toAbsolutePath().toString();

            for (String line : lines) {
                String[] parts = line.split("\t");
                cachedFileHashcodes.put(parent + "\\" + parts[1], parts[0]);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        indexFiles.add(indexFileName);
    }

    private void appendDirectoryFilesToIndex(Path parent) {
        List<Path> files = getFiles(parent);
        StringBuilder strb = new StringBuilder();
        int n = files.size();
        int i=0;
        for (Path file : files) {
            if (i%50 == 0)
                System.out.println(file.toAbsolutePath() + "\t(" + i + " / " + n + ")");
            i++;
            if (Files.isRegularFile(file))
            {
                strb
                        .append(fileHashcodeService.getHashCode(file))
                        .append("\t")
                        .append(file.getFileName().toString())
                        .append("\n");
            }
        }
        try {
            Files.writeString(Path.of(parent.toString() + Index_File_Name), strb);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Path> getFiles(Path dirName) {
        List<Path> files = new ArrayList<>();

        try (DirectoryStream<Path> stream =
                     Files.newDirectoryStream(dirName)) {
            for (Path p : stream) {
                files.add(p);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return files;
    }

    public static void appendLine(String fileName, String line)  {
        Path path = Path.of(fileName);

        try {
            Files.write(
                    path,
                    (line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
