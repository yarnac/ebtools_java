package com.eb.base.extensions;

import java.io.IOException;

public class ProcessExtensions {

    /**
     * Startet einen Process
     */
    public static void ebStartProcess(String self) {
        if (StringExtensions.ebIsNilOrEmpty(self)) return;
        try {
            new ProcessBuilder(self).start();
        } catch (IOException e) {
            System.err.println("Error starting process: " + e.getMessage());
        }
    }

    /**
     * Startet einen Process mit Argumenten
     */
    public static void ebStartProcess(String self, String arguments) {
        if (StringExtensions.ebIsNilOrEmpty(self)) return;
        try {
            ProcessBuilder pb = new ProcessBuilder(self);
            if (!StringExtensions.ebIsNilOrEmpty(arguments)) {
                pb.command().add(arguments);
            }
            pb.inheritIO();
            pb.start();
        } catch (IOException e) {
            System.err.println("Error starting process: " + e.getMessage());
        }
    }

}
