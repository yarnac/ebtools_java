package com.eb.apps.ebchat_client.app;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.undo.UndoManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Fügt einer JTextArea Undo/Redo-Unterstützung hinzu.
 *
 * Undo: Ctrl+Z (bzw. Cmd+Z unter macOS)
 * Redo: Ctrl+Y oder Ctrl+Shift+Z
 */
public final class UndoDecorator {

    private UndoDecorator() {
        // Utility-Klasse, daher keine Instanzen
    }

    public static void registerForTextArea(JTextPane textArea) {
        if (textArea == null) {
            throw new IllegalArgumentException("JTextArea darf nicht null sein.");
        }

        UndoManager undoManager = new UndoManager();

        // Änderungen am Text für Undo/Redo speichern
        textArea.getDocument().addUndoableEditListener(
                event -> undoManager.addEdit(event.getEdit())
        );

        int shortcutMask = Toolkit.getDefaultToolkit()
                .getMenuShortcutKeyMaskEx();

        InputMap inputMap = textArea.getInputMap(
                JComponent.WHEN_FOCUSED
        );

        ActionMap actionMap = textArea.getActionMap();

        // Ctrl+Z / Cmd+Z: Undo
        inputMap.put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcutMask),
                "custom-undo"
        );

        actionMap.put("custom-undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canUndo()) {
                    undoManager.undo();
                }
            }
        });

        // Ctrl+Y / Cmd+Y: Redo
        inputMap.put(
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, shortcutMask),
                "custom-redo"
        );

        // Ctrl+Shift+Z / Cmd+Shift+Z: ebenfalls Redo
        inputMap.put(
                KeyStroke.getKeyStroke(
                        KeyEvent.VK_Z,
                        shortcutMask | InputEvent.SHIFT_DOWN_MASK
                ),
                "custom-redo"
        );

        actionMap.put("custom-redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (undoManager.canRedo()) {
                    undoManager.redo();
                }
            }
        });
    }
}