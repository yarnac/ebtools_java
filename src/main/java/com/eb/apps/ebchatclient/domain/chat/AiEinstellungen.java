package com.eb.apps.ebchatclient.domain.chat;

import com.eb.base.extensions.FileExtensions;

import java.util.List;

public class AiEinstellungen {
    public static String AiPfad = GlobaleEinstellungen.getAiPfad();

    public static String AILogPfad = AiPfad + "\\chats\\requests";

    public static List<String> AiLogFiles() {
        return EbFileHelpers.EbGetFiles(AILogPfad, "log*.txt");
    }

    public static String AIKey;
    public static String AIApiKey;

    public static String getFilePath(String dateiName, String ordner) {
        String ordnerInAiPfad = (ordner != null)
                ? AiPfad + "\\" + ordner
                : AiPfad;
        EbFileHelpers.ensureDirectory(ordnerInAiPfad);
        return FileExtensions.ebFileNameInDirectory(dateiName, ordnerInAiPfad);
    }

    public static String GetIniFilePathLocal(String aiplaygroundIni) {
        String path = GlobaleEinstellungen.getPfad("LocalIniFilePath") + "\\" + aiplaygroundIni;
        EbFileHelpers.ensureDirectory(FileExtensions.ebFileDirectory(aiplaygroundIni));
        return path;
    }

    public static String getFilePath(String s) {
        return  FileExtensions.ebFullFileNameInDirectory(s, AiPfad);
    }
}
