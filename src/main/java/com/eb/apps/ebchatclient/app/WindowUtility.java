package com.eb.apps.ebchatclient.app;

import javax.swing.SwingUtilities;
import java.awt.Frame;
import java.awt.Window;
import java.util.function.Supplier;

public final class WindowUtility {

    private WindowUtility() {
        // Utility-Klasse
    }

    public static <T extends Window> T showOrCreateWindow(
            T window,
            Supplier<T> windowFactory) {

        // Fenster existiert nicht oder wurde bereits disposed
        if (window == null || !window.isDisplayable()) {
            window = windowFactory.get();
        }

        // Falls minimiert: wiederherstellen
        if (window instanceof Frame frame
                && (frame.getExtendedState() & Frame.ICONIFIED) != 0) {
            frame.setExtendedState(Frame.NORMAL);
        }

        // Anzeigen und in den Vordergrund holen
        if (!window.isVisible()) {
            window.setVisible(true);
        }

        window.toFront();

        // Fokus muss auf dem EDT angefordert werden
        SwingUtilities.invokeLater(window::requestFocusInWindow);

        return window;
    }
}