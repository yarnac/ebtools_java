package com.eb.base.system;

import com.eb.base.extensions.FileExtensions;

import java.io.IOException;

public class MD5Test {
    static MD5Rechner rechner = new MD5Rechner();
    public static void main(String[] args) {
        String[] files = FileExtensions.ebGetFiles("d:\\Medien\\Bilder\\Tumblr");
        StringBuilder strb = new StringBuilder();
        for (String file : files) {
            try {
                strb.append(rechner.calculateMD5Line(file, 4096));
                strb.append("\n");
                System.out.println(file + " " + rechner.calculateMD5(file, 4096) );
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        FileExtensions.ebWriteTextToFileUtf8(strb.toString(), "d:\\Medien\\Bilder\\Tumblr\\.ebindex2");
    }
}
