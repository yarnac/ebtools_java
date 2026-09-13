package com.eb.chatclient.domain.chat;

import com.eb.base.extensions.FileExtensions;

public class GlobaleEinstellungen {
    public static String getPfad(String localIniFilePath) {
        return FileExtensions.ebFileNameInDirectory(localIniFilePath, getAiPfad());
    }

    public static String getAiPfad() {
        return "d:\\Develop\\Visual22\\Shared\\Data\\Ai";
    }
}
