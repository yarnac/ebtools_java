package com.eb.apps.ebchatclient.components;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.extensions.StringExtensions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonFileAppendUtil {

    public static String appendFiles(String inputString, List<String> imageFileNames) {
        boolean useStringBuilder = false;
        List<String> dateien = new ArrayList<String>();
        StringBuilder strb = new StringBuilder();

        boolean convertDirectories = inputString.contains("\n<$D");

        String[] lines = inputString.split("\n");
        for (String line : lines) {
            if (!convertDirectories && line.startsWith("<$P "))
            {
                useStringBuilder = true;
                String fileName = StringExtensions.ebTrimAll(line.substring(3));
                imageFileNames.add(fileName.trim());
                continue;
            }
            if (!convertDirectories && line.startsWith("<$ "))
            {
                useStringBuilder = true;
                String fileName = StringExtensions.ebTrimAll(line.substring(2));
                dateien.add(fileName.trim());
                continue;
            }
            if (line.startsWith("<$D "))
            {
                useStringBuilder = true;
                String dirName = StringExtensions.ebTrimAll(line.substring(3));
                List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, "*.java");
                fileNames.stream().forEach(fileName -> {strb.append("<$ " + fileName).append("\n");});
                continue;
            }
            if (line.startsWith("<$DA "))
            {
                useStringBuilder = true;
                String dirName = StringExtensions.ebTrimAll(line.substring(4));
                List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, "*.*");
                fileNames.stream().forEach(fileName -> {strb.append("<$ " + fileName).append("\n");});
                continue;
            }
            else
                strb.append(line);
            strb.append("\n");
        }
        if (convertDirectories) {
            return strb.toString();
        }
        try {
            strb.append(new JasonFileContentProvider().getJsonFileString(dateien));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (useStringBuilder)
            return strb.toString();
        else
            return inputString;
    }
}
