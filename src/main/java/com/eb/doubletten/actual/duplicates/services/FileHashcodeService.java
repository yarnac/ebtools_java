package com.eb.doubletten.actual.duplicates.services;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

public class FileHashcodeService implements IFileHashcodeService {

    private final long bufferSize;

    public FileHashcodeService() {
        super();
        bufferSize = 64 * 1024L;

    }
    public FileHashcodeService(long newBufferSize) {
        bufferSize = newBufferSize;
    }

    public String getHashCode(String string)
    {
        return getHashCode(Path.of(string));
    }

    @Override
    public String getHashCode(Path filename) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            File file = new File(String.valueOf(filename));
            long length = file.length();



            try (InputStream is = Files.newInputStream(filename)) {

                byte[] bufferAnfang = new byte[(int) bufferSize];
                int bytesRead = is.read(bufferAnfang);
                digest.update(bufferAnfang, 0, bytesRead);

                if (length> 2 * bufferSize) {
                    long midPosition = length / 2;
                    long seek = midPosition - bytesRead;
                    is.skip(seek);

                    byte[] bufferMitte = new byte[(int) bufferSize];
                    bytesRead = is.read(bufferMitte);
                    digest.update(bufferMitte, 0, bytesRead);

                    long actPosition = bufferAnfang.length + seek + bytesRead;

                    long endPosition = length - bufferSize;
                    long seekToLastBlock = endPosition - actPosition;
                    is.skip(seekToLastBlock);

                    byte[] bufferEnde = new byte[(int) bufferSize];
                    bytesRead = is.read(bufferEnde);
                    digest.update(bufferEnde, 0, bytesRead);
                }
            }

            byte[] hashBytes = digest.digest();
            return FileHashcodeService.bytesToHex(hashBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<keine>";
    }



    public String getFullHashCode(Path filename) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            File file = new File(String.valueOf(filename));
            long length = file.length();

            try (InputStream is = Files.newInputStream(filename)) {
                byte[] buffer = new byte[8192];
                int bytesRead;

                while ((bytesRead = is.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }

            byte[] hashBytes = digest.digest();
            return FileHashcodeService.bytesToHex(hashBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "<keine>";
    }


    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
