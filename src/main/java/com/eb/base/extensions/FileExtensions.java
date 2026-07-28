package com.eb.base.extensions;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileExtensions {

    private static final String FILE_SEPARATOR = File.separator;

    /**
     * Gibt den Verzeichnisnamen ohne abschließenden Separator zurück
     */
    public static String ebDirectoryName(String self) {
        if (self == null) return "";
        if (self.endsWith(FILE_SEPARATOR)) {
            return self.substring(0, self.length() - FILE_SEPARATOR.length());
        }
        return self;
    }

    /**
     * Gibt den lokalen Dateinamen (ohne Pfad) zurück
     */
    public static String ebLocalFileName(String str) {
        if (str == null || str.isEmpty()) return "";
        if (str.endsWith(FILE_SEPARATOR)) {
            return ebLocalFileName(str.substring(0, str.length() - FILE_SEPARATOR.length()));
        }
        int lastIndex = str.lastIndexOf(FILE_SEPARATOR);
        return lastIndex >= 0 ? str.substring(lastIndex + FILE_SEPARATOR.length()) : str;
    }

    /**
     * Gibt die Dateiendung zurück
     */
    public static String ebFileExtension(String str) {
        if (str == null) return "";
        int lastDotIndex = str.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == 0) return "";
        return str.substring(lastDotIndex + 1);
    }

    /**
     * Gibt den Dateinamen ohne Endung zurück
     */
    public static String ebFileRoot(String str) {
        if (str == null) return "";
        return StringExtensions.ebSubstringBeforeLast(str, ".");
    }

    /**
     * Gibt den lokalen Dateinamen ohne Endung zurück
     */
    public static String ebLocalFileRoot(String str) {
        if (str == null) return "";
        return ebFileRoot(ebLocalFileName(str));
    }

    /**
     * Gibt das Verzeichnis einer Datei zurück
     */
    public static String ebFileDirectory(String self) {
        if (self == null) return "";
        if (self.endsWith(FILE_SEPARATOR)) {
            return ebFileDirectory(self.substring(0, self.length() - FILE_SEPARATOR.length()));
        }
        int lastIndex = self.lastIndexOf(FILE_SEPARATOR);
        if (lastIndex < 0) return ".";
        return self.substring(0, lastIndex);
    }

    /**
     * Gibt das übergeordnete Verzeichnis zurück
     */
    public static String ebFileParentDirectory(String self) {
        String parentDir = ebFileDirectory(ebFileDirectory(self));
        return ".".equals(parentDir) ? ".." : parentDir;
    }

    /**
     * Prüft ob Datei existiert
     */
    public static boolean ebFileExists(String str) {
        if (str == null) return false;
        return Files.exists(Paths.get(str));
    }

    /**
     * Prüft ob Verzeichnis existiert
     */
    public static boolean ebDirectoryExists(String str) {
        if (str == null) return false;
        return Files.exists(Paths.get(str)) && Files.isDirectory(Paths.get(str));
    }

    /**
     * Erstellt einen Verzeichnispfad
     */
    public static void ebCreateDirectory(String self) {
        if (self == null || self.isEmpty()) return;
        try {
            Files.createDirectories(Paths.get(self));
        } catch (IOException e) {
            System.err.println("Error creating directory: " + e.getMessage());
        }
    }

    /**
     * Erstellt einen Dateinamen im Verzeichnis
     */
    public static String ebFileNameInDirectory(String str, String dirName) {
        if (dirName == null || dirName.isEmpty()) return ebLocalFileName(str);
        return dirName + FILE_SEPARATOR + ebLocalFileName(str);
    }

    /**
     * Erstellt einen Dateinamen mit neuer Endung im Verzeichnis
     */
    public static String ebFileNameInDirectory(String str, String dirName, String extension) {
        String fileName = ebLocalFileRoot(str) + "." + extension;
        return ebFileNameInDirectory(fileName, dirName);
    }

    /**
     * Findet einen eindeutigen Dateinamen im Verzeichnis
     */
    public static String ebUniqueFileNameInDirectory(String str, String dirName) {
        String newName = ebFileNameInDirectory(str, dirName);
        if (!Files.exists(Paths.get(newName))) return newName;

        String basisName = ebFileRoot(newName);
        int i = 1;
        while (Files.exists(Paths.get(newName)) && Files.isDirectory(Paths.get(newName))) {
            newName = ebFilenameWithExtension(basisName + "_" + i++, ebFileExtension(newName));
        }
        while (Files.exists(Paths.get(newName))) {
            String newRoot = (basisName + "_" + i++);
            String extension = ebFileExtension(newName);
            newName = ebFilenameWithExtension(newRoot, extension);
        }
        return newName;
    }

    /**
     * Gibt den Dateinamen mit Zeitstempel zurück
     */
    public static String ebFilenameWithDateTime(String self) {
        return ebFilenameWithDateTime(self, java.time.LocalDateTime.now());
    }

    public static String ebFilenameWithDateTime(String self, java.time.LocalDateTime time) {
        StringBuilder strb = new StringBuilder();
        strb.append("_");
        strb.append(time.getYear());
        strb.append(String.format("%02d", time.getMonthValue()));
        strb.append(String.format("%02d", time.getDayOfMonth()));
        strb.append("_");
        strb.append(String.format("%02d", time.getHour()));
        strb.append(String.format("%02d", time.getMinute()));
        strb.append("_");
        strb.append(String.format("%02d", time.getSecond()));

        return ebFileRoot(self) + strb + "." + ebFileExtension(self);
    }

    /**
     * Gibt den Dateinamen mit neuer Endung zurück
     */
    public static String ebFilenameWithExtension(String self, String extension) {
        if (ebLocalFileName(self).contains(".")) {
            return ebFileRoot(self) + "." + extension;
        } else {
            return self + "." + extension;
        }
    }

    /**
     * Fügt ein Suffix vor der Endung ein
     */
    public static String ebFileNameWithSuffix(String fileName, String suffix) {
        return ebFileRoot(fileName) + suffix + "." + ebFileExtension(fileName);
    }

    /**
     * Fügt ein Präfix vor dem Dateinamen ein
     */
    public static String ebFileNameWithPraefix(String fileName, String praefix) {
        return ebFileNameInDirectory(praefix + ebLocalFileName(fileName), ebFileDirectory(fileName));
    }

    /**
     * Ersetzt Dateinamen
     */
    public static String ebReplaceFileNameWith(String str, String fileName) {
        return ebFileNameInDirectory(fileName, ebFileDirectory(str));
    }

    /**
     * Liest Dateiinhalte als String (UTF-8)
     */
    public static String ebGetFileText(String self) {
        if (!ebFileExists(self)) return "";
        return ebGetFileTextUtf8(self);
    }

    /**
     * Liest Dateiinhalte als String (UTF-8)
     */
    public static String ebGetFileTextUtf8(String self) {
        if (!ebFileExists(self)) return "";
        try {
            return new String(Files.readAllBytes(Paths.get(self)), StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return "";
        }
    }

    /**
     * Liest Dateiinhalte als String (Windows Encoding)
     */
    public static String ebFileContentsWindows(String self) {
        if (!ebFileExists(self)) return "";
        try {
            return new String(Files.readAllBytes(Paths.get(self)), java.nio.charset.Charset.forName("Cp1252"));
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Liest Dateiinhalte als String (Mac Encoding)
     */
    public static String ebFileContentsMac(String self) {
        if (!ebFileExists(self)) return "";
        try {
            return new String(Files.readAllBytes(Paths.get(self)), java.nio.charset.Charset.forName("MacRoman"));
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * Liest Dateiinhalte zeilenweise (UTF-8)
     */
    public static String[] ebGetFileTextLines(String self) {
        return ebGetFileTextLines(self, false, StandardCharsets.UTF_8);
    }

    public static String[] ebGetFileTextLines(String self, boolean deleteEmptyLines) {
        return ebGetFileTextLines(self, deleteEmptyLines, StandardCharsets.UTF_8);
    }

    public static String[] ebGetFileTextLines(String self, boolean deleteEmptyLines, java.nio.charset.Charset encoding) {
        if (!ebFileExists(self)) return new String[0];
        try {
            String[] lines = new String(Files.readAllBytes(Paths.get(self)), encoding)
                    .replace("\r", "").split("\n");
            if (!deleteEmptyLines) return lines;
            List<String> res = new ArrayList<>();
            for (String line : lines) {
                if (line.trim().length() > 0) res.add(line);
            }
            return res.toArray(new String[0]);
        } catch (IOException e) {
            return new String[0];
        }
    }

    /**
     * Schreibt Text in Datei (UTF-8)
     */
    public static void ebWriteTextToFileUtf8(String self, String fileName) {
        if (self == null || self.isEmpty()) return;
        try {
            if (Files.exists(Paths.get(fileName))) {
                Files.delete(Paths.get(fileName));
            }
            Files.write(Paths.get(fileName), self.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    /**
     * Schreibt Text in Datei (Windows Encoding)
     */
    public static void ebWriteTextToFile(String self, String fileName) {
        if (self == null || self.isEmpty()) return;
        try {
            if (Files.exists(Paths.get(fileName))) {
                Files.delete(Paths.get(fileName));
            }
            Files.write(Paths.get(fileName), self.getBytes(java.nio.charset.Charset.forName("Cp1252")));
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    /**
     * Schreibt Zeilen in Datei (UTF-8)
     */
    public static void ebWriteFileLinesUTF8(String self, Iterable<String> lines) {
        if (lines == null) return;
        try {
            List<String> result = new ArrayList<>();
            for (String line : lines) {
                result.add(line);
            }
            // Entferne leere Zeilen am Ende
            for (int i = result.size() - 1; i > 0; i--) {
                if (result.get(i).trim().length() == 0) {
                    result.remove(i);
                } else {
                    break;
                }
            }
            Files.write(Paths.get(self), result, StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    /**
     * Schreibt Zeilen in Datei (Windows Encoding)
     */
    public static void ebWriteFileLinesWindows(String self, Iterable<String> lines) {
        if (lines == null) return;
        try {
            List<String> result = new ArrayList<>();
            for (String line : lines) {
                result.add(line);
            }
            Files.write(Paths.get(self), result, java.nio.charset.Charset.forName("Cp1252"));
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }
    }

    /**
     * Kopiert eine Datei
     */
    public static void ebCopy(String str, String target) {
        try {
            if (Files.exists(Paths.get(target))) {
                Files.delete(Paths.get(target));
            }
            if (Files.exists(Paths.get(str))) {
                Files.copy(Paths.get(str), Paths.get(target));
            }
        } catch (IOException e) {
            System.err.println("Error copying file: " + e.getMessage());
        }
    }

    /**
     * Verschiebt eine Datei
     */
    public static void ebMove(String str, String target) {
        try {
            if (Files.exists(Paths.get(target))) {
                Files.delete(Paths.get(target));
            }
            Files.move(Paths.get(str), Paths.get(target));
        } catch (IOException e) {
            System.err.println("Error moving file: " + e.getMessage());
        }
    }

    /**
     * Gibt die Dateien eines Verzeichnisses zurück
     */
    public static String[] ebGetFiles(String self) {
        return ebGetFiles(self, "*.*");
    }

    public static String[] ebGetFiles(String self, String pattern) {
        if (!ebDirectoryExists(self)) return new String[0];
        try {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher(
                    "glob:" + pattern.replace(".", "\\.").replace("*", ".*"));
            return Files.list(Paths.get(self))
                    .filter(Files::isRegularFile)
                    .filter(path -> matcher.matches(path.getFileName()))
                    .map(Path::toString)
                    .toArray(String[]::new);
        } catch (IOException e) {
            return new String[0];
        }
    }

    /**
     * Gibt die Verzeichnisse eines Verzeichnisses zurück
     */
    public static String[] ebGetDirectories(String self, String... pattern) {
        return ebGetDirectoryList(self, pattern).toArray(new String[0]);
    }

    /**
     * Gibt die Verzeichnisse eines Verzeichnisses als Liste zurück
     */
    public static List<String> ebGetDirectoryList(String self, String... pattern) {
        if (!ebDirectoryExists(self)) return new ArrayList<>();
        if (pattern.length == 0) {
            return ebGetDirectoryList(self, "*");
        }
        List<String> res = new ArrayList<>();
        try {
            for (String p : pattern) {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher(
                        "glob:" + p.replace(".", "\\.").replace("*", ".*"));
                Files.list(Paths.get(self))
                        .filter(Files::isDirectory)
                        .filter(path -> matcher.matches(path.getFileName()))
                        .map(Path::toString)
                        .forEach(res::add);
            }
        } catch (IOException e) {
            return new ArrayList<>();
        }
        return res;
    }

    /**
     * Gibt alle Dateien rekursiv zurück
     */
    public static List<String> ebGetAllFiles(String self) {
        return ebGetAllFiles(self, "*.*");
    }

    public static List<String> ebGetAllFiles(String self, String pattern) {
        List<String> result = new ArrayList<>();
        if (!ebDirectoryExists(self)) return result;
        addAllFiles(result, self, pattern);
        return result;
    }

    /**
     * Gibt alle Dateien mit bestimmtem Namen rekursiv zurück
     */
    public static List<String> ebGetAllFilesInDirs(String self, String dirPattern, String pattern) {
        List<String> result = new ArrayList<>();
        List<String> dirs = Arrays.asList(ebGetDirectories(self, dirPattern));
        for (String dir : dirs) {
            result.addAll(ebGetAllFiles(dir, pattern));
        }
        return result;
    }

    /**
     * Gibt alle Verzeichnisse rekursiv zurück
     */
    public static List<String> ebGetAllDirectories(String self) {
        return ebGetAllDirectories(self, "*");
    }

    public static List<String> ebGetAllDirectories(String self, String pattern) {
        List<String> result = new ArrayList<>();
        if (!ebDirectoryExists(self)) return result;
        try {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher(
                    "glob:" + pattern.replace(".", "\\.").replace("*", ".*"));
            Files.walk(Paths.get(self))
                    .filter(Files::isDirectory)
                    .filter(path -> matcher.matches(path.getFileName()))
                    .map(Path::toString)
                    .filter(p -> !p.equals(self))
                    .forEach(result::add);
        } catch (IOException e) {
            return new ArrayList<>();
        }
        return result;
    }

    /**
     * Gibt die neueste Datei eines Verzeichnisses zurück
     */
    public static String ebGetLatestFile(String self) {
        return ebGetLatestFile(self, "*.*");
    }

    public static String ebGetLatestFile(String self, String pattern) {
        List<Path> files = new ArrayList<>();
        if (!ebDirectoryExists(self)) return null;
        try {
            PathMatcher matcher = FileSystems.getDefault().getPathMatcher(
                    "glob:" + pattern.replace(".", "\\.").replace("*", ".*"));
            files = Files.walk(Paths.get(self), 1)
                    .filter(Files::isRegularFile)
                    .filter(path -> matcher.matches(path.getFileName()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return null;
        }

        if (files.isEmpty()) return null;
        files.sort((p1, p2) -> {
            try {
                return Files.getLastModifiedTime(p2).compareTo(Files.getLastModifiedTime(p1));
            } catch (IOException e) {
                return 0;
            }
        });
        return files.get(0).toString();
    }

    /**
     * Prüft ob Verzeichnis leer ist
     */
    public static boolean ebIsEmptyDirectory(String self) {
        List<String> files = Arrays.asList(ebGetFiles(self));
        List<String> dirs = Arrays.asList(ebGetDirectories(self));
        return files.isEmpty() && dirs.isEmpty();
    }

    /**
     * Prüft ob Verzeichnis keine Dateien enthält (aber Unterverzeichnisse)
     */
    public static boolean ebIsDirectoryWithoutFiles(String self) {
        return ebGetAllFiles(self).isEmpty();
    }

    /**
     * Löscht leere Verzeichnisse
     */
    public static void ebDeleteEmptyDirs(String self) {
        List<String> dbstore = ebGetAllFiles(self, ".DS_Store");
        for (String item : dbstore) {
            try {
                Files.delete(Paths.get(item));
            } catch (IOException e) {
                System.err.println("Error deleting file: " + e.getMessage());
            }
        }
        deleteEmptyDirectoriesRecursive(Paths.get(self));
    }

    /**
     * Wird für ebDeleteEmptyDirs verwendet
     */
    private static void deleteEmptyDirectoriesRecursive(Path path) {
        if (!Files.exists(path)) return;
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (ebIsEmptyDirectory(dir.toString())) {
                        Files.delete(dir);
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error deleting directories: " + e.getMessage());
        }
    }

    /**
     * Gibt die Liste der Dateien mit bestimmtem Pattern zurück
     */
    public static List<String> ebGetFileList(String self, boolean recursive, String... patterns) {
        List<String> result = new ArrayList<>();
        if (!ebDirectoryExists(self)) return result;
        if (patterns.length == 0) {
            return new ArrayList<>(Arrays.asList(ebGetFiles(self)));
        }
        for (String pattern : patterns) {
            result.addAll(recursive ? ebGetAllFiles(self, pattern) : Arrays.asList(ebGetFiles(self, pattern)));
        }
        return result;
    }

    /**
     * Findet das erste gemeinsame Verzeichnis zweier Pfade
     */
    public static String ebSameRootDirectory(String file1, String file2) {
        if (file1 == null || file2 == null) return "";
        String[] parts1 = file1.toLowerCase().split(FILE_SEPARATOR);
        String[] parts2 = file2.toLowerCase().split(FILE_SEPARATOR);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts1.length && i < parts2.length; i++) {
            if (!parts1[i].toLowerCase().equals(parts2[i].toLowerCase())) break;
            if (sb.length() > 0) sb.append(FILE_SEPARATOR);
            sb.append(parts1[i]);
        }
        return sb.toString();
    }

    /**
     * Prüft ob es ein Git Repository ist
     */
    public static boolean ebIsGitRepository(String self) {
        return ebDirectoryExists(self + FILE_SEPARATOR + ".git");
    }

    /**
     * Prüft ob erste existierende Parent Verzeichnis
     */
    public static String ebFirstExistingParentDir(String str) {
        String dir = str;
        while (!ebDirectoryExists(dir)) {
            dir = ebFileDirectory(dir);
        }
        return dir;
    }

    /**
     * Führt eine Aktion auf alle Verzeichnisse aus (optimiert)
     */
    public static void ebDoWithAllDirs(String dir, java.util.function.Consumer<String> action) {
        if (!ebDirectoryExists(dir)) return;
        try {
            Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult postVisitDirectory(Path directory, IOException exc) throws IOException {
                    if (!directory.toString().equals(dir)) {
                        action.accept(directory.toString());
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Error walking file tree: " + e.getMessage());
        }
    }

    /**
     * Hilfsmethode zum rekursiven Hinzufügen von Dateien
     */
    private static void addAllFiles(List<String> list, String dirName, String pattern) {
        if (!ebDirectoryExists(dirName)) return;
        list.addAll(Arrays.asList(ebGetFiles(dirName, pattern)));
        for (String dir : ebGetDirectories(dirName)) {
            addAllFiles(list, dir, pattern);
        }
    }

    /**
     * Startet eine Datei als Process
     */
    public static boolean ebOpenAsProcess(String self, String arguments) {
        if (!ebFileExists(self)) return false;
        try {
            ProcessBuilder pb = new ProcessBuilder(self);
            if (arguments != null && !arguments.isEmpty()) {
                pb.command().add(arguments);
            }
            pb.start();
            return true;
        } catch (IOException e) {
            System.err.println("Error starting process: " + e.getMessage());
            return false;
        }
    }

    /**
     * Startet eine Datei als Process und wartet auf Ende
     */
    public static boolean ebOpenAsProcessWaitForExit(String self, String arguments) {
        if (!ebFileExists(self)) return false;
        try {
            ProcessBuilder pb = new ProcessBuilder(self);
            if (arguments != null && !arguments.isEmpty()) {
                pb.command().add(arguments);
            }
            Process p = pb.start();
            p.waitFor();
            return true;
        } catch (IOException | InterruptedException e) {
            System.err.println("Error starting process: " + e.getMessage());
            return false;
        }
    }

}
