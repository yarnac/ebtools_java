package com.eb.chatclient.domain.chat;

import com.eb.base.extensions.FileExtensions;

public class GlobaleEinstellungen {
    private static String AIPFAD;
    private static String JAVA_DATAPFAD;
    private static String USER_DATAPFAD;
    private static String SHARED_DATA;


    public static String getPfad(String localIniFilePath) {
        return FileExtensions.ebFullFileNameInDirectory(localIniFilePath, getAiPfad());
    }

    public static String getDataPfadJava() {

        if (JAVA_DATAPFAD == null)
        {
            String[] directories = new  String[]{
                    "d:\\Develop\\Java\\wsjava_ij\\Data",
                    "/Users/ekkart/Data/develop/wsjava_ij/Data"};

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
                    "/Users/ekkart/Data/develop/shared-projects/Data/Ai"};
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
}
