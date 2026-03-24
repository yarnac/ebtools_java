package misctest;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AppDataExample {
    public static void main(String[] args) {
        // AppData mit System.getenv() abrufen
        String appData = System.getProperty("user.home");




        // Pfad zu AppData erstellen
        Path appDataPath = Paths.get(appData);

        // Ausgabe des Pfads
        System.out.println("AppData Verzeichnis: " + appDataPath);
    }
}
