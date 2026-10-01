package com.eb.base.gui;


import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;

public class BrowserLaunch {
    public static void openBrowser(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                throw new RuntimeException("Fehler beim Öffnen des Browsers", e);
            }
        } else {
            System.err.println("Browsing wird auf diesem System nicht unterstützt.");
        }
    }

    public static void main(String[] args) {
        openBrowser("https://ollama.com");
    }
}
