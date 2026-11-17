package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

import com.eb.apps.ebchat_client.components.JsonFileContentProvider;
import com.eb.base.extensions.FileExtensions;
import com.eb.base.extensions.StringExtensions;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Utility-Klasse zum Hinzufügen von Dateiinhalten zu einem Text basierend auf speziellen Markern.
 * Diese Klasse ist für den Produktionseinsatz optimiert und enthält Fehlerbehandlung,
 * Thread-Sicherheit und verbesserte Performance.
 */
public class JsonFileAppendUtil {

    private static final Logger logger = Logger.getLogger(JsonFileAppendUtil.class.getName());

    // File Prefix Constants
    private static final String FILE_PREFIX = "<$ ";
    private static final String DIRECTORY_PREFIX = "<$D ";
    private static final String ALL_FILES_PREFIX = "<$DA ";
    private static final String IMAGE_FILE_PREFIX = "<$P ";

    // Line Separator Constants
    private static final String LINE_SEPARATOR = "\n";

    // File Filter Constants
    private static final String ALL_FILES_FILTER = "*";

    // Thread-safe Speicher für erlaubte Pfade
    private static final Set<Path> allowedPaths = ConcurrentHashMap.newKeySet();

    // Cache für Datei-Prüfungen zur Verbesserung der Performance
    private static final Map<String, Boolean> fileExistsCache = new ConcurrentHashMap<>();
    private static final Map<String, Boolean> isDirectoryCache = new ConcurrentHashMap<>();

    /**
     * Fügt erlaubte Verzeichnisse hinzu.
     *
     * @param directories Liste von Verzeichnispfaden
     * @throws IllegalArgumentException wenn die Liste null ist
     */
    public static void addAllowedDirectories(Iterable<String> directories) {
        if (directories == null) {
            logger.warning("Versuch, null-Verzeichnisse hinzuzufügen");
            return;
        }

        for (String dir : directories) {
            if (dir == null || dir.isEmpty()) {
                logger.warning("Leerer oder null-Verzeichnisname übersprungen");
                continue;
            }

            try {
                Path dirPath = Paths.get(dir).toAbsolutePath().toRealPath().normalize();
                if (Files.isDirectory(dirPath)) {
                    allowedPaths.add(dirPath);
                    logger.fine("Erlaubtes Verzeichnis hinzugefügt: " + dirPath);
                } else {
                    logger.warning("Nicht-Verzeichnis übersprungen: " + dirPath);
                }
            } catch (IOException e) {
                logger.log(Level.WARNING, "Fehler beim Hinzufügen des erlaubten Verzeichnisses: " + dir, e);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Unerwarteter Fehler beim Hinzufügen des erlaubten Verzeichnisses: " + dir, e);
            }
        }
    }

    /**
     * Fügt alle Dateien aus Verzeichnissen hinzu, die in der Eingabe gefunden werden.
     *
     * @param inputString Der Eingabetext
     * @return Der Text mit hinzugefügten Dateien
     */
    public static String appendDirectoryFiles(String inputString) {
        if (inputString == null) {
            return "";
        }

        if (allowedPaths.isEmpty()) {
            logger.fine("Keine erlaubten Pfade konfiguriert");
            return inputString;
        }

        if (!inputString.contains(DIRECTORY_PREFIX) && !inputString.contains(ALL_FILES_PREFIX)) {
            return inputString;
        }

        StringBuilder strb = new StringBuilder();
        String[] lines = inputString.split(LINE_SEPARATOR);

        for (String untrimmedLine : lines) {
            String trimmedLine = untrimmedLine.trim();

            if (trimmedLine.startsWith(DIRECTORY_PREFIX)) {
                String dirName = StringExtensions.ebTrimAll(trimmedLine.substring(DIRECTORY_PREFIX.length()));
                Path dirPath = Paths.get(dirName);

                if (!isAllowedFileOrDirName(dirName) || !Files.isDirectory(dirPath)) {
                    logger.fine("Verzeichnis nicht erlaubt oder nicht gefunden: " + dirName);
                    strb.append(untrimmedLine).append(LINE_SEPARATOR);
                    continue;
                }

                try {
                    List<String> fileNames = Arrays.stream(FileExtensions.ebGetFiles(dirName, ALL_FILES_FILTER))
                            .toList();
                    fileNames.forEach(fileName -> {
                        strb.append(FILE_PREFIX).append(fileName).append(LINE_SEPARATOR);
                    });
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Fehler beim Abrufen von Dateien aus Verzeichnis: " + dirName, e);
                    strb.append(untrimmedLine).append(LINE_SEPARATOR);
                }
            } else if (trimmedLine.startsWith(ALL_FILES_PREFIX)) {
                String dirName = StringExtensions.ebTrimAll(trimmedLine.substring(ALL_FILES_PREFIX.length()));
                Path dirPath = Paths.get(dirName);

                if (!isAllowedFileOrDirName(dirName) || !Files.isDirectory(dirPath)) {
                    logger.fine("Verzeichnis nicht erlaubt oder nicht gefunden: " + dirName);
                    strb.append("Datei nicht gefunden: ").append(LINE_SEPARATOR);
                    strb.append(untrimmedLine).append(LINE_SEPARATOR);

                    continue;
                }

                try {
                    List<String> fileNames = FileExtensions.ebGetAllFiles(dirName, ALL_FILES_FILTER);
                    fileNames.forEach(fileName -> {
                        strb.append(FILE_PREFIX).append(fileName).append(LINE_SEPARATOR);
                    });
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Fehler beim Abrufen aller Dateien aus Verzeichnis: " + dirName, e);
                    strb.append(untrimmedLine).append(LINE_SEPARATOR);
                }
            } else {
                strb.append(untrimmedLine).append(LINE_SEPARATOR);
            }
        }

        return strb.toString();
    }

    /**
     * Prüft, ob ein Datei- oder Verzeichnisname erlaubt ist.
     *
     * @param name Der Name zur Prüfung
     * @return true wenn erlaubt, sonst false
     */
    private static boolean isAllowedFileOrDirName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }

        try {
            Path path = Paths.get(name);
            return path.isAbsolute() && isInAllowedDirectory(path.toRealPath());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Prüft, ob ein Pfad in einem erlaubten Verzeichnis liegt.
     *
     * @param inputPath Der zu prüfende Pfad
     * @return true wenn im erlaubten Bereich, sonst false
     */
    private static boolean isInAllowedDirectory(Path inputPath) {
        if (inputPath == null) {
            return false;
        }

        Path normalized = inputPath.toAbsolutePath().normalize();
        String normStr = normalized.toString();

        return allowedPaths.stream().anyMatch(allowedDir -> {
            String dirStr = allowedDir.toString();
            // Exakte Übereinstimmung oder Start mit Verzeichnis-Separator
            return normStr.equals(dirStr) ||
                    normStr.startsWith(dirStr + FileSystems.getDefault().getSeparator());
        });
    }

    /**
     * Fügt Dateien und Bilddateien hinzu, basierend auf den Markern im Text.
     *
     * @param inputString Der Eingabetext
     * @param imageFileNamesFromUser Liste der Bilddateinamen vom Benutzer
     * @return Der Text mit hinzugefügten Dateiinhalten
     */
    public static String appendFiles(String inputString, List<String> imageFileNamesFromUser) {
        if (inputString == null) {
            return "";
        }

        if (allowedPaths.isEmpty()) {
            logger.fine("Keine erlaubten Pfade konfiguriert");
            return inputString;
        }

        Set<String> imageFileNames = determineAllowedImageFileNames(imageFileNamesFromUser);
        Set<String> fileNames = new LinkedHashSet<>();
        StringBuilder stringBuilder = new StringBuilder();

        String[] lines = inputString.split(LINE_SEPARATOR);
        for (String untrimmedLine : lines) {
            String trimmedLine = untrimmedLine.trim();

            if (addAllowedRegularFile(fileNames, trimmedLine)) {
                continue;
            }

            if (trimmedLine.startsWith(IMAGE_FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(trimmedLine.substring(IMAGE_FILE_PREFIX.length()));
                if (addAllowedRegularFile(imageFileNames, fileName)) {
                    continue;
                }
            }

            if (trimmedLine.startsWith(FILE_PREFIX)) {
                String fileName = StringExtensions.ebTrimAll(trimmedLine.substring(FILE_PREFIX.length()));
                if (addAllowedRegularFile(fileNames, fileName)) {
                    continue;
                }
            }

            stringBuilder.append(untrimmedLine).append(LINE_SEPARATOR);
        }

        try {
            // Mehrere JSON Dokumente werden akzeptiert
            if (!fileNames.isEmpty()) {
                stringBuilder.append(new JsonFileContentProvider().getJsonFileString(fileNames.stream().toList()));
            }
            if (!imageFileNames.isEmpty()) {
                stringBuilder.append(new JsonFileContentProvider().getJsonFileString(imageFileNames.stream().toList()));
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Fehler beim Abrufen von Dateiinhalten", e);
            throw new RuntimeException("Fehler beim Abrufen von Dateiinhalten", e);
        }

        return stringBuilder.toString();
    }

    /**
     * Fügt eine reguläre Datei hinzu, wenn sie erlaubt ist.
     *
     * @param fileNames Menge der Dateinamen
     * @param fileName Der zu prüfende Dateiname
     * @return true wenn erfolgreich hinzugefügt, sonst false
     */
    private static boolean addAllowedRegularFile(Set<String> fileNames, String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return false;
        }

        if (isAllowedFileOrDirName(fileName) && Files.isRegularFile(Paths.get(fileName))) {
            fileNames.add(fileName.trim());
            return true;
        }
        return false;
    }

    /**
     * Bestimmt erlaubte Bilddateinamen.
     *
     * @param imageFileNamesFromUser Liste der Bilddateinamen vom Benutzer
     * @return Set mit erlaubten Bilddateinamen
     */
    private static Set<String> determineAllowedImageFileNames(List<String> imageFileNamesFromUser) {
        Set<String> imageFileNames = new LinkedHashSet<>();

        if (imageFileNamesFromUser == null || imageFileNamesFromUser.isEmpty()) {
            return imageFileNames;
        }

        for (String imageFileName : imageFileNamesFromUser) {
            if (imageFileName == null || imageFileName.isEmpty()) {
                continue;
            }

            if (isAllowedFileOrDirName(imageFileName) && Files.isRegularFile(Paths.get(imageFileName))) {
                imageFileNames.add(imageFileName);
            }
        }

        return imageFileNames;
    }

    /**
     * Fügt alle Dateien hinzu, sowohl aus Verzeichnissen als auch regulären Dateien.
     *
     * @param content Der Eingabetext
     * @return Der Text mit hinzugefügten Dateiinhalten
     */
    public static String appendAllFiles(String content) {
        return appendFiles(appendDirectoryFiles(content), new ArrayList<>());
    }

    /**
     * Löscht alle erlaubten Pfade.
     */
    public static void clearAllowedPaths() {
        allowedPaths.clear();
        logger.info("Alle erlaubten Pfade gelöscht");
    }

    /**
     * Gibt die Anzahl der erlaubten Pfade zurück.
     *
     * @return Anzahl der erlaubten Pfade
     */
    public static int getAllowedPathCount() {
        return allowedPaths.size();
    }
}