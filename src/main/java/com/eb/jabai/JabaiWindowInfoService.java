package com.eb.jabai;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.nio.charset.StandardCharsets;

/**
 * Service-Klasse zur Abfrage und Deserialisierung von Fensterinformationen
 * mittels des Systemtools 'yabai'.
 *
 * <p>Dieser Service interagiert mit dem macOS Window Manager (yabai)
 * und stellt die Daten als Java-Objekte bereit.</p>
 */
public class JabaiWindowInfoService {

    private final ObjectMapper objectMapper;

    public JabaiWindowInfoService() {
        // Initialisierung des ObjectMappers
        this.objectMapper = new ObjectMapper();
        // Wir konfigurieren ihn ähnlich wie im Reader, aber ohne INDENT_OUTPUT
        // da dies für die Parsing-Logik irrelevant ist, aber Konsistenz schafft.
        this.objectMapper.configure(SerializationFeature.INDENT_OUTPUT, false);
    }

    /**
     * Führt den Befehl 'yabai -m query --windows' aus, liest den JSON-Output
     * und deserialisiert diesen in eine Liste von JabaiWindowInfo Objekten.
     *
     * <p>Benötigte Berechtigungen auf macOS (macOS 12+ / 13+):
     * Diese Klasse erfordert 'Zugriff für Bildschirmaufnahme', damit yabai
     * alle Fenster identifizieren darf.</p>
     *
     * @return Eine Liste der erkannten Fensterinformationen.
     * @throws IOException Wenn der Befehl nicht ausgeführt werden konnte oder
     *                     die Ausgabe nicht als JSON geparst werden kann.
     */
    public List<JabaiWindowInfo> determineJabaiWindowInfos() throws IOException {
        // Definition des externen Befehls (yabai Kommandozeilen-Tool)
        // Wir nutzen var für Type-Inference in Java 21
        var command = new String[] {"/opt/homebrew/bin/yabai", "-m", "query", "--windows"};

        Process process = null;

        try {
            // Prozess starten
            process = new ProcessBuilder(command).start();

            // Wartezeit begrenzen, falls yabai hängt (Optional aber gut für Robustheit)
            if (!process.waitFor(30, java.util.concurrent.TimeUnit.SECONDS)) {
                throw new IOException("Timeout: Der yabai-Query-Prozess wurde nicht abgeschlossen.");
            }

            // Exit-Code prüfen
            int exitCode = process.exitValue();
            if (exitCode != 0) {
                var errorOutput = readProcessError(process);
                throw new IOException(String.format("yabai-Befehl fehlgeschlagen (Exit Code: %d). Output: %s", exitCode, errorOutput));
            }

            // Ausgabe lesen und in JSON-String konvertieren
            String jsonContent;
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8))) {
                var stringBufferBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBufferBuilder.append(line).append("\n");
                }
                jsonContent = stringBufferBuilder.toString();
            }

            // Deserialisierung mit TypeReference für List<Record>
            return objectMapper.readValue(jsonContent, new TypeReference<List<JabaiWindowInfo>>() {});

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            if (process != null) {
                process.destroyForcibly(); // Ressource freigegeben bei Exceptions
            }
        }
    }

    /**
     * Hilfsmethode zum Lesen des Fehlerstreams eines Prozesses.
     * Dies verhindert, dass der Prozess aufgrund vollen Puffers hängen bleibt.
     */
    private String readProcessError(Process process) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8))) {
            StringBuilder errorBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                errorBuilder.append(line).append(" ");
            }
            return errorBuilder.toString();
        } catch (IOException e) {
            return "Fehler beim Lesen des Fehlerstreams: " + e.getMessage();
        }
    }
}
