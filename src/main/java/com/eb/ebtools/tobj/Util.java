package com.eb.ebtools.tobj;

import com.eb.base.io.FileUtil;

import java.io.IOException;

public class Util {

    public static void startProcess(String fileName, String arguments, boolean waitUntilProcessTerminate) {
        // Erstelle eine ProcessBuilder Instanz mit dem Dateinamen und den Argumenten
        ProcessBuilder processBuilder = new ProcessBuilder(fileName, arguments);

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
