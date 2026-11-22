package com.eb.doubletten.actual.duplicates.services;

import com.eb.base.extensions.FileExtensions;
import com.eb.doubletten.actual.duplicates.api.IDuplikatContainer;
import com.eb.doubletten.actual.duplicates.api.IDuplicateScanService;
import com.eb.doubletten.actual.duplicates.domain.SizeHashcodeDuplicateContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SizeHashcodeDuplicateScanService implements IDuplicateScanService {

    Map<String, List<String>> files = new HashMap<>();
    IFileHashcodeService fileHashcodeService = new FileHashcodeWithCacheService();

    @Override
    public List<? extends IDuplikatContainer> scan(Path root) {
        files.clear();

        scanFiles(files, root);

        String rootPath = root.toString();

        try {
            String[] dirs = FileExtensions.ebGetDirectories(root.toString());



            for (String dir : dirs) {
                if (!isAllowedDir(dir))
                    continue;
                String ebIgnoreFileName = FileExtensions.ebFileNameInDirectory(".ebignore", dir);
                if (FileExtensions.ebFileExists(ebIgnoreFileName))
                    continue;
                String[] ebGetFiles = FileExtensions.ebGetFiles(dir);
                for (String ebGetFile : ebGetFiles) {
                    Path p = Paths.get(ebGetFile);
                    if (!isAllowedFile(p))
                        continue;

                    String key = fileHashcodeService.getHashCode(p);
                    if (!files.containsKey(key))
                        files.put(key, new ArrayList<>());
                    else
                    {
                        if (files.get(key).contains(ebGetFile))
                            continue;
                    }

                    files.get(key).add(ebGetFile);
                }
            }
        } catch (Exception e) {
            return List.of();
        }

        List<SizeHashcodeDuplicateContainer> list = files.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(e ->
                        new SizeHashcodeDuplicateContainer(
                                e.getKey(),
                                e.getValue()
                        )
                )
                .collect(Collectors.toList());
        list.sort((x,y) -> x.getLabel().compareTo(y.getLabel()));
        return list;
    }

    private void scanFiles(Map<String, List<String>> filesMap, Path root) {
        String rootPath = root.toString();
        if (!isAllowedDir(rootPath))
            return;

        String[] ebGetFiles = FileExtensions.ebGetFiles(rootPath);
        for (String ebGetFile : ebGetFiles) {
            if (!isAllowedFile(Path.of(ebGetFile)))
                continue;
            String key = fileHashcodeService.getHashCode(Paths.get(ebGetFile));
            List<String> files = filesMap.computeIfAbsent(key, k -> new ArrayList<>());
            files.add(ebGetFile);
        }

        String[] ebGetDirs = FileExtensions.ebGetDirectories(rootPath);
        for (String ebGetDir : ebGetDirs) {
            scanFiles(filesMap, Paths.get(ebGetDir));
        }

    }

    private boolean isAllowedDir(String dir) {

        String localDirName = FileExtensions.ebLocalFileName(dir);
        if (localDirName.startsWith("."))
            return false;
        String ebIgnoreFileName = FileExtensions.ebFileNameInDirectory(".ebignore", dir);
        if (FileExtensions.ebFileExists(ebIgnoreFileName))
            return false;

        return true;
    }

    private boolean isAllowedFile(Path path) {
        File file = path.toFile();
        String localFileName = path.getFileName().toString();
        if (file.isDirectory() || file.length()==0)
            return false;
        String fileName = path.toString();
        return !localFileName.startsWith(".") &&
                !fileName.contains("\\bin\\") && !fileName.contains("/bin/") &&
                !fileName.contains("\\obj\\") && !fileName.contains("/obj/");
    }
}

