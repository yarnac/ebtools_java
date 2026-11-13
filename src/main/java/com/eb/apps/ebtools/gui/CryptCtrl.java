package com.eb.apps.ebtools.gui;

import com.eb.base.crypt.Crypt;
import com.eb.base.crypt.algorithmes.Twofish_Crypter;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;

public class CryptCtrl {

    private final CryptForm window;
    private final IniFile myIniFile;

    public static void main(String[] args) {
        CryptCtrl ctrl = new CryptCtrl();
    }

    public CryptCtrl() {
        window = new CryptForm();

        myIniFile = IniFileProvider.createIniFile("CryptForm.ini");
        GuiDecorator decorator = new GuiDecorator(window, myIniFile, "Einstellungen");
        decorator.addOnEnterOrReturnPressed(window.getEdPassword(), (Runnable) ()->{EncryptInput();});
        decorator.addOnEnterOrReturnPressed(window.getEdInput(), (Runnable) ()->{EncryptInput();});
        decorator.addOnEnterOrReturnPressed(window.getEdOutput(), (Runnable) ()->{DecryptInput();});


        SwingUtilities.invokeLater(() -> {window.setVisible(true);});
    }

    private void DecryptInput() {
        String str = window.getEdOutput().getText();
        String pwd = window.getEdPassword().getText();
        Twofish_Crypter crypter = new Twofish_Crypter();

        String encrypted = crypter.decryptString(str, pwd);
        window.getEdInput().setText(encrypted);
    }

    private void EncryptInput() {
        String str = window.getEdInput().getText();
        String pwd = window.getEdPassword().getText();
        Twofish_Crypter crypter = new Twofish_Crypter();

        String encrypted = crypter.encryptString(str, pwd);
        window.getEdOutput().setText(encrypted);

    }
}
