package com.eb.chatclient.domain.chat;

import com.eb.base.extensions.FileExtensions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class EbFileHelpers {
    public static List<String> EbGetFiles(String aiLogPfad, String s) {
        return FileExtensions.ebGetFileList(aiLogPfad, false, s);
    }

    public static void ensureDirectory(String ordnerInAiPfad) {
        try {
            Files.createDirectories(Paths.get(ordnerInAiPfad));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
