package com.eb.base;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SecurityFileHandler {

    private final Path basePath; // Verzeichnis, das sicher ist (z.B. Benutzerverzeichnis)
    private final List<Path> allowedPaths = new ArrayList<>();

    public SecurityFileHandler(String safeBaseDir) throws IOException {
        this.basePath = Paths.get(safeBaseDir).toRealPath();
    }

    public SecurityFileHandler(String safeBaseDir, List<String> newAllowedDirs) throws IOException {
        this.basePath = Paths.get(safeBaseDir).toRealPath();
        for (String path : newAllowedDirs) {
            this.allowedPaths.add(Paths.get(path).toRealPath());
        }
        if (!allowedPaths.contains(this.basePath)) {
            allowedPaths.add(0, this.basePath);
        }
    }

    /**
     * Öffnet eine Datei SICH, indem Path Traversal verhindert wird.
     */
    public Path getSafeFile(String requestedFileName) throws IOException {

        if (requestedFileName == null || requestedFileName.isEmpty()) {
            throw new SecurityException(
                    "Zugriffsverletzung: Versuch außerhalb der erlaubten Verzeichnisse");
        }

        // 1. Pfad auflösen relativ zum Basis-Pfad (oder absolut prüfen)
        Path requestedPath = basePath.resolve(requestedFileName).normalize().toRealPath();

        // 2. WICHTIG: Prüfen, ob der Pfad nach 'normalize' noch in erlaubte Verzeichnissen liegt

        boolean found = false;
        for (Path allowedPath : allowedPaths) {
            if (requestedPath.startsWith(allowedPath)) {
                found = true;
                break;
            }
        }

        if (!found) {
            throw new SecurityException(
                    "Zugriffsverletzung: Versuch außerhalb der erlaubten Verzeichnisse");
        }

        // 3. Existenz prüfen
        if (!Files.exists(requestedPath) || !Files.isReadable(requestedPath)) {
            throw new IOException("Datei existiert oder ist nicht lesbar.");
        }

        return requestedPath;
    }
}
