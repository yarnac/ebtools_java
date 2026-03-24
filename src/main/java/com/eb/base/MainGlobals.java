package com.eb.base;

import java.nio.file.Path;

public class MainGlobals {

    public static String getUserFileName(String fileName)
    {
        String userHome = System.getProperty("user.home");
        return userHome + "/" + fileName;
    }

    public static String getEbToolsFileName(String fileName)
    {
        String userHome = System.getProperty("user.home");
        return userHome + "/EbToolsDaten/" + fileName;
    }

    public static String getTempFileName(String fileName)
    {
        String tempDir = System.getProperty("java.io.tmpdir");
        return tempDir + fileName;
    }

    public static String getDataFileName()
    {
        if (isWindows())
            return "d:/Develop/Java/wsjava_ij/Data";
        else
            return "/Users/ekkart/Data/Develop/Java/wsjava_ij/Data";
    }

    public static String getDataFileName(String fileName)
    {
        if (fileName.contains("/")||fileName.contains("\\"))
            return fileName;
        return getDataFileName() + "/" + fileName;
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }
}
