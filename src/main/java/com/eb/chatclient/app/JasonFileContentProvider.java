package com.eb.chatclient.app;


import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JasonFileContentProvider {

    private static final ObjectMapper mapper = new ObjectMapper();

    /*Liest eine Datei und gibt ihren Inhalt als JSON-String zurück,
     * der deserialisierbar ist in List<Codedatei>
     *
             * @param fileName Der Name der zu lesenden Datei
     * @return JSON-String mit der Datei als Codedatei-Objekt in einer Liste
     * @throws IOException Falls die Datei nicht gelesen werden kann
     */
    public String getJsonFileString(String fileName) throws IOException {
        // Dateiinhalt lesen
        String fileContent = Files.readString(Paths.get(fileName));

        // Dateiendung extrahieren
        String fileType = extractFileType(fileName);

        // Codedatei-Objekt erstellen
        Codedatei codedatei = new Codedatei();
        codedatei.fileName = fileName;
        codedatei.type = fileType;
        codedatei.codeText = fileContent;

        // In Liste verpacken (weil die Deserialisierung erwartet List<Codedatei>)
        List<Codedatei> codedateien = new ArrayList<>();
        codedateien.add(codedatei);

        StringBuilder strb = new StringBuilder();
        strb.append("```json\n");
        // Als JSON-String serialisieren
        strb.append(mapper.writeValueAsString(codedateien)).append("\n");
        strb.append("```\n");
        return strb.toString();
    }

    public String getJsonFileString(List<String> fileNames) throws IOException {
        // Dateiinhalt lesen
        List<Codedatei> codedateien = new ArrayList<>();
        for(String fn : fileNames) {
            String fileContent = Files.readString(Paths.get(fn));

            // Dateiendung extrahieren
            String fileType = extractFileType(fn);

            // Codedatei-Objekt erstellen
            Codedatei codedatei = new Codedatei();
            codedatei.fileName = fn;
            codedatei.type = fileType;
            codedatei.codeText = fileContent;

            // In Liste verpacken (weil die Deserialisierung erwartet List<Codedatei>)

            codedateien.add(codedatei);
        }

        StringBuilder strb = new StringBuilder();
        strb.append("```json\n");
        // Als JSON-String serialisieren
        strb.append(mapper.writeValueAsString(codedateien)).append("\n");
        strb.append("```\n");
        return strb.toString();
    }

    /* Extrahiert die Dateityp-Endung aus dem Dateinamen
     *
             * @param fileName Der Dateiname
     * @return Die Dateityp-Endung (z.B. "java", "json", "txt")
     */
    private String extractFileType(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(lastDotIndex + 1).toLowerCase();
        }
        return "unknown";
    }

    // Inner-Klasse für Serialisierung/Deserialisierung
    public static class Codedatei {
        public String fileName;
        public String type;
        public String codeText;
    }
}
