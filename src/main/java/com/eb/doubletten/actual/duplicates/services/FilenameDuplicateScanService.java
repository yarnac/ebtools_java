package com.eb.doubletten.actual.duplicates.services;

import com.eb.doubletten.actual.duplicates.api.IDuplikatContainer;
import com.eb.doubletten.actual.duplicates.api.IDuplicateScanService;
import com.eb.doubletten.actual.duplicates.domain.ListBasedDuplikatContainer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FilenameDuplicateScanService implements IDuplicateScanService {

    @Override
    public List<? extends IDuplikatContainer> scan(Path root) {

        Map<String, List<String>> files = new HashMap<>();

        try {
            Files.walk(root)
                    .filter(Files::isRegularFile)
                    .filter(this::isAllowedFile)
                    .forEach(p ->
                            {
                                if (isAllowedFile(p))
                                    files.computeIfAbsent(
                                            p.getFileName().toString(),
                                            k -> new ArrayList<>()
                                    ).add(p.toString());
                            });

        } catch (IOException e) {
            return List.of();
        }

        List<ListBasedDuplikatContainer> list = files.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .map(e ->
                        new ListBasedDuplikatContainer(
                                e.getKey(),
                                e.getValue()
                        )
                )
                .toList();
        return list;
    }

    private boolean isAllowedFile(Path path) {
        String fileName = path.toString();
        return (fileName.contains("\\bin\\") || fileName.contains("/bin/") ||
                fileName.contains("\\obj\\") || fileName.contains("/obj/"))
                ? false
                : true;
    }
}

