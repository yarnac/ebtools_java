package com.eb.chatclient.app;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.extensions.StringExtensions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonFileAppenUtil {

    public static String appendFiles(String inputString) {
        boolean useStringBuilder = false;
        List<String> dateien = new ArrayList<String>();
        StringBuilder strb = new StringBuilder();
        String[] lines = inputString.split("\n");
        for (String line : lines) {
            if (line.startsWith("<$ "))
            {
                useStringBuilder = true;
                String fileName = StringExtensions.ebTrimAll(line.substring(2));
                dateien.add(fileName);
            }
            if (line.startsWith("<$D "))
            {
                useStringBuilder = true;
                String dirName = StringExtensions.ebTrimAll(line.substring(3));
                String[] files = FileExtensions.ebGetFiles(dirName, "*.*");
                List<String> fileNames = Arrays.asList(files);
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
