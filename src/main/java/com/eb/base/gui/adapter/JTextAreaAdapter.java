package com.eb.base.gui.adapter;

// Generiert von claude-haiku

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class JTextAreaAdapter {
    private final JScrollPane scrollPane;
    private final JTextArea textArea;

    public JTextAreaAdapter(JScrollPane scrollPane, JTextArea textArea) {
        this.scrollPane = scrollPane;
        this.textArea = textArea;
    }

    /**
     * Gibt die Zeilennummer unter dem Cursor zurück (1-basiert)
     */
    public int getLineNrUnderCursor() {
        int caretPosition = textArea.getCaretPosition();
        try {
            return textArea.getLineOfOffset(caretPosition) + 1;
        } catch (BadLocationException e) {
            return 1;
        }
    }

    /**
     * Gibt die Anzahl der sichtbaren Zeilen zurück
     */
    public int getVisibleLinesCount() {
        FontMetrics fontMetrics = textArea.getFontMetrics(textArea.getFont());
        int lineHeight = fontMetrics.getHeight();
        int viewportHeight = scrollPane.getViewport().getHeight();
        return Math.max(1, viewportHeight / lineHeight);
    }

    /**
     * Gibt die erste sichtbare Zeilennummer zurück (1-basiert)
     */
    public int getFirstVisibleLineNr() {
        JViewport viewport = scrollPane.getViewport();
        Rectangle viewRect = viewport.getViewRect();
        int offset = textArea.viewToModel2D(new Point(0, viewRect.y));
        
        try {
            return textArea.getLineOfOffset(offset) + 1;
        } catch (BadLocationException e) {
            return 1;
        }
    }

    /**
     * Gibt das Wort unter dem Cursor zurück
     */
    public String getWordUnderCursor() {
        int caretPosition = textArea.getCaretPosition();
        String text = textArea.getText();
        
        if (caretPosition >= text.length()) {
            return "";
        }
        
        int start = caretPosition;
        int end = caretPosition;
        
        // Rückwärts zum Anfang des Wortes
        while (start > 0 && isWordCharacter(text.charAt(start - 1))) {
            start--;
        }
        
        // Vorwärts zum Ende des Wortes
        while (end < text.length() && isWordCharacter(text.charAt(end))) {
            end++;
        }
        
        return text.substring(start, end);
    }

    /**
     * Gibt den Satz unter dem Cursor zurück
     */
    public String getSentenceUnderCursor() {
        int caretPosition = textArea.getCaretPosition();
        String text = textArea.getText();
        
        if (caretPosition >= text.length()) {
            return "";
        }
        
        int start = caretPosition;
        int end = caretPosition;
        
        // Rückwärts zum Anfang des Satzes
        while (start > 0) {
            char ch = text.charAt(start - 1);
            if (ch == '.' || ch == '!' || ch == '?') {
                break;
            }
            start--;
        }
        
        // Vorwärts zum Ende des Satzes
        while (end < text.length()) {
            char ch = text.charAt(end);
            if (ch == '.' || ch == '!' || ch == '?') {
                end++;
                break;
            }
            end++;
        }
        
        return text.substring(start, end).trim();
    }

    /**
     * Gibt den Absatz unter dem Cursor zurück
     */
    public String getParagraphUnderCursor() {
        int caretPosition = textArea.getCaretPosition();
        String text = textArea.getText();
        
        if (caretPosition >= text.length()) {
            return "";
        }
        
        int start = caretPosition;
        int end = caretPosition;
        
        // Rückwärts zum Anfang des Absatzes
        while (start > 0 && text.charAt(start - 1) != '\n') {
            start--;
        }
        
        // Vorwärts zum Ende des Absatzes
        while (end < text.length() && text.charAt(end) != '\n') {
            end++;
        }
        
        return text.substring(start, end).trim();
    }

    /**
     * Setzt die erste sichtbare Zeilennummer (1-basiert)
     */
    public void setFirstVisibleLine(int lineNr) {
        try {
            int offset = textArea.getLineStartOffset(Math.max(0, lineNr - 1));
            Rectangle rect = textArea.modelToView2D(offset).getBounds();
            JViewport viewport = scrollPane.getViewport();
            viewport.setViewPosition(new Point(0, rect.y));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt die Zeilennummer für eine bestimmte Textposition zurück (1-basiert)
     */
    public int getLineNrForTextPosition(int position) {
        if (position < 0 || position > textArea.getText().length()) {
            return -1;
        }
        
        try {
            return textArea.getLineOfOffset(position) + 1;
        } catch (BadLocationException e) {
            return -1;
        }
    }

    /**
     * Hilfsmethode: Prüft, ob ein Zeichen Teil eines Wortes ist
     */
    private boolean isWordCharacter(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }
}