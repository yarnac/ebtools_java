/* * Copyright parcIT GmbH
 * 
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdr�cklicher Erlaubnis der parcIT GmbH gestattet.
 * 
 * Created by: eb
 */
package com.eb.base.crypt;


import java.io.IOException;

import com.eb.base.Logger;
import com.eb.base.crypt.algorithmes.Twofish_Crypter;


public class Crypt {

    public static boolean IsValidCryptedFile(String filename, String passwort)
    {
        try {
            new Twofish_Crypter().loadEncryptedFile(filename, passwort);
            return true;
        }
        catch (Exception e) {
            Logger.logError(e);

        }
        return false;
    }

    public static void saveEncryptedFile(String fileName, String passwort, String text) {
        // TODO Ekkart 5.5 2015 Auto-generated method stub
        new Twofish_Crypter().saveEncryptedFile(fileName, passwort, text);

    }

    public static String loadEncryptedFile(String passFile, String pw) throws IOException {
        // TODO Ekkart 5.5 2015 Auto-generated method stub
        Twofish_Crypter crypter = new Twofish_Crypter();
        return crypter.loadEncryptedFile(passFile, pw);

    }

}
