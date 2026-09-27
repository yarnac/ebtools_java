package com.eb.jabai;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Reader-Klasse zum Serialisieren (Schreiben) und Deserialisieren (Lesen)
 * von List<JavaiWindowInfo> in JSON-Dateien.
 */
public class JabaiWindowInfoReader {

    private final ObjectMapper objectMapper;

    public JabaiWindowInfoReader() {
        this.objectMapper = new ObjectMapper();
        // Schreibe JSON formatiert (Pretty Print) für bessere Lesbarkeit der Datei
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    /**
     * Schreibt die Liste der Fensterinformationen in eine JSON-Datei.
     *
     * @param fileName Der Dateiname inklusive Pfad (z.B. "window_infos.json")
     * @param windowInfos Die Liste an JavaiWindowInfo Objekten, die geschrieben werden sollen
     */
    public void writeWindowInfos(String fileName, List<JabaiWindowInfo> windowInfos) throws IOException {
        // Konvertiert das Objekt in einen JSON-String und schreibt es direkt in die Datei
        Files.writeString(Paths.get(fileName), objectMapper.writeValueAsString(windowInfos));
    }

    /**
     * Liest Fensterinformationen aus einer JSON-Datei.
     *
     * @param fileName Der Dateiname inklusive Pfad (z.B. "window_infos.json")
     * @return Die Liste der JavaiWindowInfo Objekte aus der Datei
     */
    public List<JabaiWindowInfo> readWindowInfos(String fileName) throws IOException {
        // Liest den kompletten JSON-String aus der Datei
        String jsonContent = Files.readString(Paths.get(fileName));

        // Deserialisiert unter Beachtung des Generics (Liste <JavaiWindowInfo>)
        return objectMapper.readValue(jsonContent, new TypeReference<List<JabaiWindowInfo>>() {});
    }
}
