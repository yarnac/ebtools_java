package com.eb.misc;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvertJsonToCode {

    // Record zur strukturierten Darstellung eines Eintrags im JSON
    public record CodeFile(String fileName, String type, String codeText) {}

    /**
     * Liest eine JSON-Datei ein, die eine Liste von Code-Datei-Definitionen enthält,
     * und schreibt die jeweiligen codeText-Inhalte in die angegebene Zielverzeichnisstruktur.
     *
     * @param fileName        Pfad zur JSON-Eingabedatei
     * @param targetDirectory Zielverzeichnis, in dem die Dateien erstellt werden sollen
     * @throws IOException Wenn Dateizugriff oder JSON-Parsing fehlschlägt
     */
    public void convertJsonToCode(String fileName, String targetDirectory) throws IOException {
        if (fileName == null || targetDirectory == null) {
            throw new IllegalArgumentException("Dateiname und Zielverzeichnis dürfen nicht null sein.");
        }

        ObjectMapper mapper = new ObjectMapper();
        Path targetPath = Paths.get(targetDirectory);
        Path inputPath = Paths.get(fileName);

        // Zielverzeichnis sicherstellen (und ggf. erstellen)
        Files.createDirectories(targetPath);

        // JSON einlesen
        List<CodeFile> codeFiles;
        try (var reader = Files.newBufferedReader(inputPath, StandardCharsets.UTF_8)) {
            codeFiles = mapper.readValue(reader, new TypeReference<List<CodeFile>>() {});
        }

        // Dateien in Zielverzeichnis schreiben
        for (var file : codeFiles) {
            String safeFileName = sanitizeFileName(file.fileName());
            Path outPath = targetPath.resolve(safeFileName);

            // Unterverzeichnisse ggf. erstellen (falls fileName einen Pfad enthält)
            Path parentDir = outPath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }

            Files.writeString(outPath, file.codeText(), StandardCharsets.UTF_8);
        }
    }

    // Hilfsmethode zur Bereinigung des Dateinamens (Verhindert path traversal)
    private String sanitizeFileName(String fileName) {
        // Entferne Zeichen, die auf Windows/unix problematisch sein könnten
        // (dient hier als einfacher Schutz)
        String clean = fileName.replace('\\', '/'); // auf Unix-Style normalisieren
        String[] parts = clean.split("/");
        // Verhindere absolute Pfade und '../'-Traversierung
        if (clean.startsWith("/") || clean.contains("..")) {
            // Alternativ: Exception werfen – hier: nur die Dateinamen-Ende verwenden
            clean = parts[parts.length - 1]; // fallback
        }
        // Optional: Zeichenbeschränkung für extreme Fälle
        clean = clean.replaceAll("[^a-zA-Z0-9_\\-./]", "_"); // sehr vorsichtig

        // Falls leer: Standardnamen
        return clean.isEmpty() ? "unnamed.txt" : clean;
    }

    // Optional: Beispiel-Main-Methode
    public static void main(String[] args) {
        ConvertJsonToCode converter = new ConvertJsonToCode();
        try {
            // Beispielaufruf – passe Pfade an
            converter.convertJsonToCode("input.json", "code");
            System.out.println("Konvertierung abgeschlossen.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
