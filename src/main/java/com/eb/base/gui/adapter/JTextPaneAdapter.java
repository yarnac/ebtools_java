package com.eb.base.gui.adapter;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;

public class JTextPaneAdapter {

    private final JScrollPane scrollPane;
    private final JTextPane textPane;

    public JTextPaneAdapter(JScrollPane scrollPane, JTextPane textPane) {
        this.scrollPane = scrollPane;
        this.textPane = textPane;
    }

    /**
     * Gibt die Zeilennummer unter dem Cursor zurück (1-basiert).
     */
    public int getLineNrUnderCursor() {
        int caretPosition = textPane.getCaretPosition();
        String text = textPane.getText();

        return getLineNr(text, caretPosition);
    }

    /**
     * Gibt die Anzahl der sichtbaren Zeilen zurück.
     */
    public int getVisibleLinesCount() {
        FontMetrics fontMetrics =
                textPane.getFontMetrics(textPane.getFont());

        int lineHeight = fontMetrics.getHeight();
        int viewportHeight = scrollPane.getViewport().getHeight();

        return Math.max(1, viewportHeight / lineHeight);
    }

    /**
     * Gibt die erste sichtbare Zeilennummer zurück (1-basiert).
     */
    public int getFirstVisibleLineNr() {
        JViewport viewport = scrollPane.getViewport();
        Rectangle viewRect = viewport.getViewRect();

        try {
            Point point = new Point(0, viewRect.y);

            int offset = textPane.viewToModel2D(point);

            if (offset < 0) {
                return 1;
            }

            return getLineNrForTextPosition(offset);

        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * Gibt das Wort unter dem Cursor zurück.
     */
    public String getWordUnderCursor() {
        int caretPosition = textPane.getCaretPosition();
        String text = textPane.getText();

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
     * Gibt den Satz unter dem Cursor zurück.
     */
    public String getSentenceUnderCursor() {
        int caretPosition = textPane.getCaretPosition();
        String text = textPane.getText();

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
     * Gibt den Absatz unter dem Cursor zurück.
     */
    public String getParagraphUnderCursor() {
        int caretPosition = textPane.getCaretPosition();
        String text = textPane.getText();

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
     * Setzt die erste sichtbare Zeilennummer (1-basiert).
     */
    public void setFirstVisibleLine(int lineNr) {
        try {
            int offset = getTextPositionForLine(lineNr);

            Rectangle rect =
                    textPane.modelToView2D(offset).getBounds();

            JViewport viewport = scrollPane.getViewport();

            viewport.setViewPosition(
                    new Point(0, rect.y)
            );

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt die Zeilennummer für eine bestimmte Textposition zurück
     * (1-basiert).
     *
     * Eine Zeile beginnt ausschließlich nach einem '\n'.
     */
    public int getLineNrForTextPosition(int position) {
        String text = textPane.getText();

        if (position < 0 || position > text.length()) {
            return -1;
        }

        return getLineNr(text, position);
    }

    /**
     * Gibt die Textposition des Anfangs einer Zeile zurück.
     *
     * @param lineNr Zeilennummer (1-basiert)
     */
    private int getTextPositionForLine(int lineNr) {
        if (lineNr <= 1) {
            return 0;
        }

        String text = textPane.getText();

        int currentLine = 1;

        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                currentLine++;

                if (currentLine == lineNr) {
                    return i + 1;
                }
            }
        }

        // Falls die angeforderte Zeile nicht existiert:
        return text.length();
    }

    /**
     * Ermittelt die Zeilennummer einer Textposition.
     *
     * Identisch zur Logik von JTextArea:
     *
     *   Text ohne '\n'       -> Zeile 1
     *   "abc\n"              -> Zeile 1
     *   "abc\ndef"           -> Position 4 = Zeile 2
     */
    private int getLineNr(String text, int position) {
        int lineNr = 1;

        for (int i = 0; i < position; i++) {
            if (text.charAt(i) == '\n') {
                lineNr++;
            }
        }

        return lineNr;
    }

    /**
     * Prüft, ob ein Zeichen Teil eines Wortes ist.
     */
    private boolean isWordCharacter(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }
}