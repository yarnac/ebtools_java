package com.eb.apps.ebtools.tobj;

import com.eb.base.extensions.FileExtensions;
import com.eb.base.io.FileUtil;

import java.io.File;
import java.io.IOException;

public class Util {

    public static void startProcess(String fileName, String arguments, boolean waitUntilProcessTerminate) {
        // Erstelle eine ProcessBuilder Instanz mit dem Dateinamen und den Argumenten
        ProcessBuilder processBuilder = new ProcessBuilder(fileName, arguments);
        processBuilder.directory(new File(FileExtensions.ebFileDirectory(fileName)));

        try {
            // Starte den Prozess
            Process process = processBuilder.start();

            if (waitUntilProcessTerminate) {
                // Warten bis der Prozess beendet ist
                process.waitFor();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            FileUtil.WriteText("error.log", e.getMessage());
        }
    }
}
