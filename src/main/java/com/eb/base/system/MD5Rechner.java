package com.eb.base.system;

import com.eb.base.extensions.FileExtensions;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class MD5Rechner {

    private final MessageDigest md5;

    public MD5Rechner() {
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 wird nicht unterstützt", e);
        }
    }

    public String calculateMD5Line(String filename, long maxSize) throws IOException {
        Path path = Path.of(filename);
        long fileSize = Files.size(path);
        String hash = calculateMD5(filename, maxSize);
        return hash + "\t" + FileExtensions.ebLocalFileName(filename) + '\t' + fileSize;
    }

    public String calculateMD5(String filename, long maxSize)
            throws IOException {

        Path path = Path.of(filename);

        try (InputStream stream = Files.newInputStream(path)) {

            long n = Files.size(path);

            if (n > maxSize && maxSize > 0) {

                long offset = (n - maxSize) / 2;

                byte[] buffer = new byte[Math.toIntExact(maxSize)];

                try (var channel = Files.newByteChannel(path)) {
                    channel.position(offset);

                    java.nio.ByteBuffer byteBuffer =
                            java.nio.ByteBuffer.wrap(buffer);

                    while (byteBuffer.hasRemaining()) {
                        if (channel.read(byteBuffer) < 0) {
                            break;
                        }
                    }
                }

                return HexFormat.of().formatHex(md5.digest(buffer));
            }

            return HexFormat.of().formatHex(md5.digest(stream.readAllBytes()));
        }
    }
}
