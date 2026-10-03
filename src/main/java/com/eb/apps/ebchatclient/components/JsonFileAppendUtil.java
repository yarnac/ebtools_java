package com.eb.apps.ebchatclient.components;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.extensions.StringExtensions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonFileAppendUtil {

    public static String appendDirectoryFiles(String inputString) {
        if (!inputString.contains("\n<$D"))
            return inputString;
        StringBuilder strb = new StringBuilder();
        String[] lines = inputString.split("\n");
        for (String line : lines) {
            String trimmedLine = line.trim();

            if (trimmedLine.startsWith("<$D "))
            {
                String dirName = StringExtensions.ebTrimAll(line.substring(3));
                List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, "*.java");
                fileNames.forEach(fileName -> {strb.append("<$ ").append(fileName).append("\n");});
            }
            else if (trimmedLine.startsWith("<$DA "))
            {
                String dirName = StringExtensions.ebTrimAll(line.substring(4));
                List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, "*.*");
                fileNames.forEach(fileName -> {strb.append("<$ ").append(fileName).append("\n");});
            }
            else {
                strb.append(line);
                strb.append("\n");
            }
        }
        return strb.toString();
    }


    public static String appendFiles(String inputString, List<String> imageFileNames) {

        List<String> dateien = new ArrayList<String>();
        StringBuilder strb = new StringBuilder();

        if (!inputString.contains("\n<$"))
            return inputString;


        String[] lines = inputString.split("\n");
        for (String line : lines) {
            if (line.startsWith("<$P "))
            {
                String fileName = StringExtensions.ebTrimAll(line.substring(3));
                imageFileNames.add(fileName.trim());
                continue;
            }
            if (line.startsWith("<$ "))
            {
                String fileName = StringExtensions.ebTrimAll(line.substring(2));
                dateien.add(fileName.trim());
                continue;
            }

            strb.append(line);
            strb.append("\n");
        }

        try {
            strb.append(new JasonFileContentProvider().getJsonFileString(dateien));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return strb.toString();
    }
}
