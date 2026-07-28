package com.eb.ebookreader.gui;

public class TextFinder {

    /**
     * Sucht die erste Position einer Wortfolge im originaltext.
     * 
     * @param originaltext Der Text, in dem gesucht werden soll
     * @param suchmuster Die Wortfolge, die gesucht wird
     * @return Die Position des ersten Zeichens der gefundenen Wortfolge,
     *         oder -1, wenn die Wortfolge nicht gefunden wird
     */
    public int searchPositionOfWortfolge(String originaltext, String suchmuster) {
        // Eingabevalidierung
        if (originaltext == null || suchmuster == null) {
            return -1;
        }
        
        // Texte in Kleinbuchstaben umwandeln
        String textLower = originaltext.toLowerCase().replace(","," ").replace("."," ").replace(","," ").replace(":"," ").replace(";"," ");;
        String musterLower = suchmuster.toLowerCase().replace(","," ").replace("."," ").replace(","," ").replace(":"," ").replace(";"," ");;
        
        // Suchmuster in Wörter aufteilen
        String[] suchWorter = musterLower.trim().split("\\s+");
        
        if (suchWorter.length == 0) {
            return -1;
        }
        
        // Originaltext in Wörter aufteilen
        String[] textWorter = textLower.split("\\s+");
        
        // Nach der Wortfolge suchen
        for (int i = 0; i <= textWorter.length - suchWorter.length; i++) {
            // Prüfen, ob ab Position i die gesuchte Wortfolge vorhanden ist
            if (istWortfolgeAb(textWorter, i, suchWorter)) {
                // Position des ersten Zeichens dieser Wortfolge im originaltext finden
                return findeCharPosition(textLower, textWorter, i);
            }
        }
        
        return -1;
    }
    
    /**
     * Prüft, ob ab der angegebenen Position im Word-Array die gesuchte Wortfolge vorkommt.
     * 
     * @param textWorter Array der Wörter aus dem Originaltext
     * @param startIndex Startindex im Word-Array
     * @param suchWorter Array der gesuchten Wörter
     * @return true, wenn die Wortfolge ab startIndex vorhanden ist
     */
    private boolean istWortfolgeAb(String[] textWorter, int startIndex, String[] suchWorter) {
        for (int i = 0; i < suchWorter.length; i++) {
            String w1 = textWorter[startIndex + i];
            String w2 = suchWorter[i];
            if (!w1.equals(w2)) {
                return false;
            }
            else
            {
                this.toString();
            }
        }
        return true;
    }
    
    /**
     * Findet die Character-Position eines Wortes im originaltext.
     * 
     * @param text Der Originaltext in Kleinbuchstaben
     * @param textWorter Array der Wörter
     * @param wordIndex Index des Wortes im Array
     * @return Die Character-Position des Wortes im Text
     */
    private int findeCharPosition(String text, String[] textWorter, int wordIndex) {
        int charPos = 0;
        int wordCount = 0;
        boolean inWord = false;
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            boolean isWhitespace = Character.isWhitespace(c);
            
            if (!isWhitespace && !inWord) {
                // Anfang eines neuen Wortes
                if (wordCount == wordIndex) {
                    return i;
                }
                inWord = true;
                wordCount++;
            } else if (isWhitespace && inWord) {
                // Ende eines Wortes
                inWord = false;
            }
        }
        
        return -1;
    }
}
