package com.eb.apps.ebchat_client.domain.chat;

import com.eb.base.ai_service.llm_client.infrastructure.LlmClientFactory;
import com.eb.base.crypt.Crypt;
import com.eb.base.crypt.CryptDES;
import com.eb.base.crypt.algorithmes.Twofish_Crypter;
import com.eb.base.extensions.FileExtensions;

import java.nio.charset.StandardCharsets;

public class GlobaleEinstellungen {
    private static String AIPFAD;
    private static String JAVA_DATAPFAD;
    private static String USER_DATAPFAD;
    private static String SHARED_DATA;
    private static String simplePwd;

    public static String getSimplePassword()
    {
        if (simplePwd == null)
            simplePwd = FileExtensions.ebGetFileText(getDataPfadJUser("mykey_crypted.txt")).substring(10, 32);

        return simplePwd;
    }

    public static String getPfad(String localIniFilePath) {
        return FileExtensions.ebFullFileNameInDirectory(localIniFilePath, getAiPfad());
    }

    public static String getDataPfadJava() {

        if (JAVA_DATAPFAD == null)
        {
            String[] directories = new  String[]{
                    "d:\\Develop\\Java\\wsjava_ij\\Data",
                    "/Users/ekkart/Data/develop/wsjava_ij/Data",
                    "/home/ekkart/develop/java/shared_data",
                    "c:\\workspaces\\etw\\Visual Studio\\Shared\\Data",
                    "/Users/ekkart/Data/Develop/shared_data"};

            JAVA_DATAPFAD = findDirectory(directories);
        }
        return JAVA_DATAPFAD;
    }

    public static String getDataPfadJUser(String fileName) {

        String userHome = System.getProperty("user.home");
        return FileExtensions.ebFullFileNameInDirectory(fileName, userHome + "/EbToolsDaten");
    }

    public static String getAiPfad() {

        if (AIPFAD == null) {
            String[] directories = new  String[]{
                    "d:\\Develop\\Visual22\\Shared\\Data\\Ai",
                    "c:\\workspaces\\etw\\Visual Studio\\Shared\\Data\\Ai",
                    "/Users/ekkart/Data/develop/shared-projects/Data/Ai",
                    "/Users/ekkart/Data/Develop/shared-projects/Data/Ai",
                    "/home/ekkart/develop/java/shared-projects/Data/Ai"
            };

            AIPFAD = findDirectory(directories);
        }
        return AIPFAD;
    }

    private static String findDirectory(String[] directories) {
        for (String directory : directories) {
            if (FileExtensions.ebDirectoryExists(directory)) {
                return directory;
            }
        }
        throw new RuntimeException("Directory not found");
    }

    public static String getTempFileName(String fileName)
    {
        String tempDir = System.getProperty("java.io.tmpdir");
        return tempDir + fileName;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public static String getDataPfadShared() {
        if (SHARED_DATA == null) {
            String[] directories = new  String[]{
                    "d:\\Develop\\shared_data",
                    "/Users/ekkart/Data/develop/shared_data"};
            SHARED_DATA = findDirectory(directories);
        }
        return SHARED_DATA;
    }

    public static String getDataPfadShared(String fileName) {
        return FileExtensions.ebFullFileNameInDirectory(fileName, getDataPfadShared());
    }

    public static boolean isMacOs() {
        return getDataPfadJava().startsWith("/User");
    }
}
