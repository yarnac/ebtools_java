package com.eb.apps.ebvlc.app;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileLocker {

    /**
     * Prüft, ob auf der angegebenen Datei ein Lock besteht.
     * Verwendet tryLock() für eine nicht-blockierende Prüfung.
     */
    public static boolean isFileLocked(Path path) {
        FileChannel channel = null;
        try {

            channel = FileChannel.open(path,  StandardOpenOption.READ,
                    StandardOpenOption.WRITE);
            // Versuchen, ein exklusives Lock auf Byte 0 mit Länge 1 zu erhalten
            return channel.tryLock(0, Long.MAX_VALUE, false) == null;
        } catch (Exception e) {
            System.err.println("Fehler beim Prüfen des Datei-Locks class: '" + e.getClass().getName() + "' Fehler: " + e.getMessage());
            return false;
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (Exception e) {
                    // Ignorieren bei Schließfehler
                }
            }
        }
    }

    /**
     * Prüft, ob auf der angegebenen Datei ein Lock besteht (über String-Pfad).
     */
    public static boolean isFileLocked(String filePath) {
        Path path = Paths.get(filePath);
        return isFileLocked(path);
    }

}
