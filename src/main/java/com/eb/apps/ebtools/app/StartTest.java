package com.eb.apps.ebtools.app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StartTest {

    public static void main(String[] args) throws IOException, InterruptedException {
        // 1. Pfade definieren (Java-Pfad und Ziel-Jar)
        String javaHome = System.getProperty("java.home");
        String javaExecutable = javaHome + "/bin/java";

        // Alternative: Nutze einfach "java", wenn JAVA_HOME im PATH des Systems gesetzt ist
        // String javaExecutable = "java";

        List<String> command = new ArrayList<>();
        command.add(javaExecutable);          // Schritt 1: Das Java-Executable

        // Befehlsargumente für den Ziel-Prozess
        for (String arg : args) {
            command.add(arg);
        }

        System.out.println("Starte Prozess mit Command: " + String.join(" ", command));

        ProcessBuilder processBuilder = new ProcessBuilder(command);

        // Option A: Ausgabe direkt auf die Konsole des Aufrufers leiten (bequem)
        // processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        // processBuilder.redirectErrorStream(true);

        // Option B: Streams separat lesen, um Deadlocks zu vermeiden (empfohlen für stabile Apps)
        processBuilder.redirectErrorStream(true);
        // Hinweis: Wenn Sie die Ausgabe programmatisch analysieren wollen, nutzen Sie Process.getInputStream()
        // in einem separaten Thread.

        Process process = processBuilder.start();

        // Prozess-Ausgabe abholen (um Pipe-Limits zu umgehen)
        new Thread(() -> {
            try {
                java.util.Scanner scanner = new java.util.Scanner(process.getInputStream());
                while (scanner.hasNextLine()) {
                    System.out.println("CHILD: " + scanner.nextLine());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        // Warten, bis der Prozess beendet ist
        int exitCode = process.waitFor();

        if (exitCode == 0) {
            System.out.println("Prozess erfolgreich gestartet und abgeschlossen.");
        } else {
            System.err.println("Prozess abgebrochen mit Exit-Code: " + exitCode);
            // Hier könnte man process.destroy() aufrufen, wenn nötig
        }

        process.destroy();
    }
}
