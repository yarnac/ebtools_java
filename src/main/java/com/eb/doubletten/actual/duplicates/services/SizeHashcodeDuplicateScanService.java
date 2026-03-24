package com.eb.doubletten.actual.duplicates.services;

import com.eb.doubletten.actual.duplicates.api.IDuplikatContainer;
import com.eb.doubletten.actual.duplicates.api.IDuplicateScanService;
import com.eb.doubletten.actual.duplicates.domain.SizeHashcodeDuplicateContainer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

        try {
            Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(this::isAllowedFile)
                    .forEach(p ->
                            {
                                if (isAllowedFile(p)) {
                                    String key = fileHashcodeService.getHashCode(p);
                                    files.computeIfAbsent(
                                            key,
                                            k -> new ArrayList<>()
                                    ).add(p.toString());
                                }
                            });

        } catch (IOException e) {
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

    private boolean isAllowedFile(Path path) {
        File file = path.toFile();
        if (file.isDirectory() || file.length()==0)
            return false;
        String fileName = path.toString();
        return (fileName.contains(".ebindex") ||
                fileName .contains("\\bin\\") || fileName.contains("/bin/") ||
                fileName.contains("\\obj\\") || fileName.contains("/obj/"))
                ? false
                : true;
    }
}

