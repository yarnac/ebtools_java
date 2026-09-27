package com.eb.websearch;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Utility-Klasse zum Aufteilen eines Verzeichnisses basierend auf der Anzahl der Dateien.
 * Die Dateien werden rekursiv gefunden und auf mehrere Teilverzeichnisse (Parts) verteilt.
 */
public class DirectorySplitter {

    private static final Logger LOGGER = Logger.getLogger(DirectorySplitter.class.getName());

    /**
     * Verschiebt alle Dateien aus dem Verzeichnis 'sourceDirectory' in Teile,
     * die im Verzeichnis 'targetDirectory' erzeugt werden.
     *
     * @param sourceDirectory   Der Pfad zum Quellverzeichnis.
     * @param targetDirectory   Der Pfad zum Zielverzeichnis, in das die Part-Ordner erstellt werden.
     * @param maxCountOfFiles   Die maximale Anzahl von Dateien pro Teil-Ordner (Part).
     * @throws IllegalArgumentException Falls Parameter ungültig sind.
     * @throws IOException            Falls beim Lesen oder Verschieben der Dateien ein Fehler auftritt.
     */
    public static void splitDirectory(String sourceDirectory, String targetDirectory, int maxCountOfFiles)
            throws IOException {

        validateParameters(sourceDirectory, targetDirectory, maxCountOfFiles);

        Path sourcePath = Paths.get(sourceDirectory);
        Path targetPath = Paths.get(targetDirectory);

        // Quellverzeichnis muss existieren und ein Verzeichnis sein
        if (!Files.isDirectory(sourcePath)) {
            throw new IllegalArgumentException("Quellpfad ist kein gültiges Verzeichnis: " + sourceDirectory);
        }

        // Zielverzeichnis anlegen, falls nicht vorhanden
        if (Files.exists(targetPath) && !Files.isDirectory(targetPath)) {
            throw new IllegalArgumentException("Zielpfad existiert bereits und ist keine Ordner: " + targetDirectory);
        } else if (!Files.exists(targetPath)) {
            Files.createDirectories(targetPath);
        }

        // 1. Alle Dateien rekursiv sammeln (nur regular files)
        var fileStreams = List.of(Files.walk(sourcePath)); // Try-with-resources im nächsten Schritt

        // Wir nutzen einen Stream innerhalb eines try-Blocks, um sicherzustellen, dass er geschlossen wird.
        var allFiles = new ArrayList<Path>();
        try (var walkStream = Files.walk(sourcePath)) {
            // Filtern: Nur reguläre Dateien, nicht Verzeichnisse, und nicht das Wurzelverzeichnis selbst
            var filePaths = walkStream.filter(path -> {
                        boolean isDir;
                        try {
                            isDir = Files.isDirectory(path);
                        } catch (Exception e) {
                            LOGGER.log(Level.WARNING, "Fehler beim Zugriff auf Pfad: " + path, e);
                            return false;
                        }
                        return !isDir && !path.equals(sourcePath);
                    })
                    .collect(Collectors.toList());

            allFiles.addAll(filePaths);
        }

        if (allFiles.isEmpty()) {
            LOGGER.info("Keine Dateien zum Verschieben gefunden.");
            return;
        }

        // 2. Berechnen, wie viele Parts benötigt werden
        int totalParts = (int) Math.ceil(allFiles.size() / (double) maxCountOfFiles);

        if (totalParts < 1) {
            throw new IllegalArgumentException("maxCountOfFiles muss >= 1 sein.");
        }

        LOGGER.info(String.format("Starte Splitting: %d Dateien in %d Parts.", allFiles.size(), totalParts));

        // 3. Ordner für die Teile anlegen und verschieben
        for (int i = 0; i < allFiles.size(); i++) {
            Path sourceFile = allFiles.get(i);

            // Berechnung des Part-Index (basierend auf maxCountOfFiles)
            int partIndex = (i / maxCountOfFiles) + 1; // Startet bei 1

            String partFolderName = "Part_" + partIndex;
            Path targetPartDir = targetPath.resolve(partFolderName);

            // Sicherstellen, dass der Ziel-Ordner existiert
            if (!Files.exists(targetPartDir)) {
                Files.createDirectories(targetPartDir);
            }

            // 4. Zielpfad konstruieren (Relative Pfadstruktur erhalten, um Kollisionen zu vermeiden)
            Path relativePath = sourcePath.relativize(sourceFile);
            Path targetFile = targetPartDir.resolve(relativePath);

            try {
                Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Split " + sourceFile + " in " + targetFile);
                // Optional: Leere Verzeichnisse im Source nach dem Move aufräumen
                // deleteEmptyDirectoriesRecursively(sourcePath);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, String.format("Fehler beim Verschieben von Datei %s", sourceFile), e);
                throw new IOException("Verschieben fehlgeschlagen. Prüfung der Dateirechte oder Speicherplatz nötig.", e);
            }
        }

        // Letzter Cleanup: Leere Verzeichnisse im Quellordner löschen (Optional, aber gut für Ordnung)
        deleteEmptyDirectoriesRecursively(sourcePath);

        LOGGER.info(String.format("Splitting abgeschlossen. %d Dateien in Parts verteilt.", allFiles.size()));
    }

    /**
     * Hilft beim Aufräumen: Löscht leer gelassene Verzeichnisse rekursiv abwärts vom Zielort.
     */
    private static void deleteEmptyDirectoriesRecursively(Path startPath) throws IOException {
        // Wir gehen rückwärts durch das Verzeichnis (reverse order).
        // In Java 21 könnte man Optional verwenden, aber ein iterativer Ansatz ist performant genug und klar.
        try (var files = Files.walk(startPath)) {
            files
                    .filter(Files::isDirectory)
                    .sorted(java.util.Comparator.reverseOrder())
                    .forEach(path -> {
                        try {
                            // Nur löschen, wenn das Verzeichnis leer ist
                            if (Files.list(path).findAny().isEmpty()) {
                                Files.deleteIfExists(path);
                            }
                        } catch (IOException e) {
                            LOGGER.log(Level.WARNING, "Konnte leeres Verzeichnis nicht löschen: " + path, e);
                        }
                    });
        }
    }

    private static void validateParameters(String sourceDirectory, String targetDirectory, int maxCountOfFiles) {
        if (sourceDirectory == null || sourceDirectory.isBlank()) {
            throw new IllegalArgumentException("Quellverzeichnis darf nicht leer sein.");
        }
        if (targetDirectory == null || targetDirectory.isBlank()) {
            throw new IllegalArgumentException("Zielverzeichnis darf nicht leer sein.");
        }
        if (maxCountOfFiles <= 0) {
            throw new IllegalArgumentException("maxCountOfFiles muss positiv sein (> 0).");
        }
    }

    // Beispielhafter Aufruf im Main-Methode für Testing-Zwecke
    public static void main(String[] args) {
        try {
            String source = "/Volumes/Extreme SSD/Medien/Tumblr";      // Zum Testen anpassen
            String target = "/Volumes/Extreme SSD/Medien/Tumblr2";      // Zum Testen anpassen
            int limit = 1000;

            System.out.println("Starte Splitting Operation...");
            splitDirectory(source, target, limit);
        } catch (Exception e) {
            System.err.println("Fehler bei der Ausführung: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
