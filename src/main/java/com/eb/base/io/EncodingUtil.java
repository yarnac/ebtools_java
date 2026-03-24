package com.eb.base.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.*;
import java.nio.file.*;


class EncodingUtil {

    public static String guessEncoding(String fileName) {

        Path path = Path.of(fileName);

        // 1️⃣ BOM prüfen (sehr schnell)
        try (InputStream is = Files.newInputStream(path)) {

            byte[] bom = is.readNBytes(4);

            if (bom.length >= 3 &&
                    bom[0] == (byte)0xEF &&
                    bom[1] == (byte)0xBB &&
                    bom[2] == (byte)0xBF) {
                return "UTF-8";
            }

            if (bom.length >= 2) {
                if (bom[0] == (byte)0xFF && bom[1] == (byte)0xFE) {
                    return "UTF-16LE";
                }
                if (bom[0] == (byte)0xFE && bom[1] == (byte)0xFF) {
                    return "UTF-16BE";
                }
            }

        } catch (IOException e) {
            return Charset.defaultCharset().name();
        }

        // 2️⃣ UTF-8 Validierung (liest nur einen Teil der Datei)
        if (looksLikeUtf8(path)) {
            return "UTF-8";
        }

        // 3️⃣ Fallback (historisch korrekt für viele Textdateien)
        return "windows-1252";
    }

    private static boolean looksLikeUtf8(Path path) {

        CharsetDecoder decoder = StandardCharsets.UTF_8
                .newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);

        try (InputStream is = Files.newInputStream(path);
             BufferedInputStream bis = new BufferedInputStream(is)) {

            byte[] buffer = new byte[4096];
            int read = bis.read(buffer);

            if (read <= 0) {
                return true; // leere Datei → UTF-8
            }

            decoder.decode(ByteBuffer.wrap(buffer, 0, read));
            return true;

        } catch (IOException e) {
            return false;
        }
    }


}
