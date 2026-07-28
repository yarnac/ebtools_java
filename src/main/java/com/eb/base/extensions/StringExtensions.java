package com.eb.base.extensions;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Pattern;

public class StringExtensions {

    /**
     * Öffnet eine Datei mit der Standard-Anwendung
     */
    public static void ebOpenFile(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return;
        }
        try {
            String os = System.getProperty("os.name").toLowerCase();
            ProcessBuilder pb;
            if (os.contains("win")) {
                pb = new ProcessBuilder("cmd", "/c", "start", fileName);
            } else if (os.contains("mac")) {
                pb = new ProcessBuilder("open", fileName);
            } else {
                pb = new ProcessBuilder("xdg-open", fileName);
            }
            pb.start();
        } catch (Exception e) {
            System.err.println("Error opening file: " + e.getMessage());
        }
    }

    /**
     * Entfernt mehrfach hintereinander folgende Zeilenumbrüche
     */
    public static String ebRemoveLineEndDoubles(String str) {
        if (str == null) return "";
        String output = str;
        int outputLen = output.length();
        while (true) {
            output = output.replace("\r\n\r\n\r\n", "\r\n\r\n");
            output = output.replace("\n\n\n", "\n\n");
            if (output.length() == outputLen) break;
            outputLen = output.length();
        }
        return output;
    }

    /**
     * Entfernt XML-Tags aus einem String
     */
    public static String ebRemoveXmlTags(String text) {
        if (text == null) return "";
        int startIndex = 0;
        while (startIndex <= text.length() && (startIndex = text.indexOf("<", startIndex)) != -1) {
            int openBracketIndex = text.indexOf("<", startIndex);
            int closeBracketIndex = text.indexOf(">", startIndex);
            if (closeBracketIndex > openBracketIndex) {
                int len = closeBracketIndex - openBracketIndex + 1;
                text = text.substring(0, startIndex) + text.substring(startIndex + len);
            } else {
                break;
            }
        }
        return text;
    }

    /**
     * Entfernt Satztrennzeichen (Punkt) und alles nach dem ersten Punkt
     */
    public static String ebRemoveSatztrennzeichen(String text) {
        return ebSubstringBeforeFirst(text, ".");
    }

    /**
     * Teilt Text in Sätze auf
     */
    public static String ebSplitTextBySentences(String eingabeText, int sentenceCount) {
        if (eingabeText == null) return "";
        boolean isInAnfuehrungszeichen = false;
        StringBuilder strb = new StringBuilder();
        int currentAnzahl = 0;

        for (int i = 0; i < eingabeText.length(); i++) {
            char ch = eingabeText.charAt(i);
            strb.append(ch);

            if (ebIsAnfuehrungszeichen(ch))
                isInAnfuehrungszeichen = !isInAnfuehrungszeichen;

            if (!isInAnfuehrungszeichen && ebIsSatztrennzeichen(ch)) {
                if (i < eingabeText.length() - 3 && ch == '.' && 
                    eingabeText.charAt(i + 1) == '.' && eingabeText.charAt(i + 2) == '.') {
                    strb.append("...");
                    i += 2;
                    continue;
                }

                if (i < eingabeText.length() - 1 && eingabeText.charAt(i + 1) == ')') {
                    i++;
                    strb.append(')');
                }

                if (i < eingabeText.length() - 2 && eingabeText.charAt(i + 2) == ')') {
                    i += 2;
                    strb.append(')');
                }

                currentAnzahl++;
                if (currentAnzahl == sentenceCount) {
                    currentAnzahl = 0;
                    strb.append("\n\n");
                    if (i < eingabeText.length() - 1 && Character.isWhitespace(eingabeText.charAt(i + 1)))
                        i++;
                }
            }
        }
        return strb.toString();
    }

    /**
     * Prüft ob ein Zeichen ein Satztrennzeichen ist
     */
    public static boolean ebIsSatztrennzeichen(char ch) {
        return ".?!".indexOf(ch) >= 0;
    }

    /**
     * Prüft ob ein Zeichen ein Anführungszeichen ist
     */
    public static boolean ebIsAnfuehrungszeichen(char ch) {
        return "\"\"„".indexOf(ch) >= 0;
    }

    /**
     * Substring nach letztem Vorkommen eines Musters
     */
    public static String ebSubstringAfterLast(String str, String pattern) {
        return ebSubstringAfterLast(str, pattern, false, true);
    }

    public static String ebSubstringAfterLast(String str, String pattern, boolean includePattern, boolean toLower) {
        return ebSubstringAfter(str, pattern, includePattern, toLower, String::lastIndexOf);
    }

    /**
     * Substring nach erstem Vorkommen eines Musters
     */
    public static String ebSubstringAfterFirst(String str, String pattern) {
        return ebSubstringAfterFirst(str, pattern, false, true);
    }

    public static String ebSubstringAfterFirst(String str, String pattern, boolean includePattern, boolean toLower) {
        return ebSubstringAfter(str, pattern, includePattern, toLower, String::indexOf);
    }

    /**
     * Generische Substring-After Methode
     */
    private static String ebSubstringAfter(String str, String pattern, boolean includePattern, 
                                          boolean toLower, java.util.function.BiFunction<String, String, Integer> indexFunc) {
        if (str == null) return "";
        String searchStr = toLower ? str.toLowerCase() : str;
        String searchPattern = toLower ? pattern.toLowerCase() : pattern;
        int index = indexFunc.apply(searchStr, searchPattern);
        if (index < 0) return str;
        if (!includePattern) index += pattern.length();
        return str.substring(index);
    }

    /**
     * Substring vor letztem Vorkommen eines Musters
     */
    public static String ebSubstringBeforeLast(String str, String pattern) {
        return ebSubstringBeforeLast(str, pattern, false, true);
    }

    public static String ebSubstringBeforeLast(String str, String pattern, boolean includePattern, boolean ignoreCase) {
        return ebSubstringBefore(str, pattern, includePattern, ignoreCase, String::lastIndexOf);
    }

    /**
     * Substring vor erstem Vorkommen eines Musters
     */
    public static String ebSubstringBeforeFirst(String str, String pattern) {
        return ebSubstringBeforeFirst(str, pattern, false, true);
    }

    public static String ebSubstringBeforeFirst(String str, String pattern, boolean includePattern, boolean ignoreCase) {
        return ebSubstringBefore(str, pattern, includePattern, ignoreCase, String::indexOf);
    }

    /**
     * Generische Substring-Before Methode
     */
    private static String ebSubstringBefore(String str, String pattern, boolean includePattern, boolean toLower,
                                           java.util.function.BiFunction<String, String, Integer> indexFunc) {
        if (str == null) return "";
        String searchStr = toLower ? str.toLowerCase() : str;
        String searchPattern = toLower ? pattern.toLowerCase() : pattern;
        int index = indexFunc.apply(searchStr, searchPattern);
        if (index < 0) return str;
        if (includePattern) index += pattern.length();
        return str.substring(0, index);
    }

    /**
     * Ersetzt letztes Vorkommen eines Musters
     */
    public static String ebReplaceLast(String str, String pattern, String replacement) {
        String vorher = ebSubstringBeforeLast(str, pattern);
        String nachher = ebSubstringAfterLast(str, pattern);
        if (vorher.equals(nachher)) return str;
        return vorher + replacement + nachher;
    }

    /**
     * Ersetzt erstes Vorkommen eines Musters
     */
    public static String ebReplaceFirst(String str, String pattern, String replacement) {
        if (ebEqualsIgnoreCase(str, pattern)) return replacement;
        String vorher = ebSubstringBeforeFirst(str, pattern);
        String nachher = ebSubstringAfterFirst(str, pattern);
        if (vorher.equals(nachher)) return str;
        return vorher + replacement + nachher;
    }

    /**
     * Teilt einen String nach einem Muster
     */
    public static List<String> ebSplit(String str, String pattern) {
        return ebSplit(str, pattern, false);
    }

    public static List<String> ebSplit(String str, String pattern, boolean deleteEmptyLines) {
        if (str == null) return new ArrayList<>();
        List<String> res = new ArrayList<>();
        int index = str.indexOf(pattern);
        if (index < 0) {
            res.add(str);
            return res;
        }

        int lastIndex = 0;
        while (index >= 0) {
            String newPart = str.substring(lastIndex, index);
            if (!deleteEmptyLines || newPart.trim().length() > 0)
                res.add(newPart);
            lastIndex = index + pattern.length();
            index = str.indexOf(pattern, lastIndex);
        }
        String lastPart = str.substring(lastIndex);
        if (!deleteEmptyLines || lastPart.trim().length() > 0)
            res.add(lastPart);

        return res;
    }

    /**
     * Teilt Text nach Anführungszeichen und Zeichen
     */
    public static List<String> ebNewSplit(String s, char ch) {
        List<String> result = new ArrayList<>();
        String[] strings = s.split("\"");
        for (int i = 0; i < strings.length; i++) {
            String part = strings[i].trim();
            if (i % 2 == 0 && part.length() > 0) {
                for (String subPart : strings[i].split(String.valueOf(ch))) {
                    result.add(subPart.trim());
                }
            } else {
                result.add(strings[i]);
            }
        }
        return result.stream()
                .map(String::trim)
                .filter(x -> x.length() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Zählt Zeilen in einem String
     */
    public static int ebCountLines(String str) {
        if (str == null || str.isEmpty()) return 0;
        return ebSplit(str, "\n").size();
    }

    /**
     * Ersetzt Unicode-Spaces
     */
    public static String ebReplaceUnicodeSpaces(String str, boolean useTabs) {
        if (ebIsNilOrEmpty(str)) return "";
        String[] unicodeSpaces = {
                "\u2000", "\u2001", "\u2002", "\u2003", "\u2004", "\u2005", 
                "\u2006", "\u2007", "\u2008", "\u2009", "\u200A", "\u00A0", 
                "\u202F", "\u205F", "\u3000"
        };
        String replaceWith = useTabs ? "\t" : "    ";
        for (String space : unicodeSpaces) {
            str = str.replace(space, replaceWith);
        }
        return str;
    }

    /**
     * Ersetzt Text mit Case-insensitive Vergleich
     */
    public static String ebReplaceIgnoreCase(String self, String pattern, String other) {
        if (self == null) return "";
        String current = self.toLowerCase();
        int aktpos = 0;
        StringBuilder strb = new StringBuilder();
        int index = current.indexOf(pattern.toLowerCase(), aktpos);
        while (index > -1) {
            strb.append(self, aktpos, index);
            strb.append(other);
            aktpos = index + pattern.length();
            index = current.indexOf(pattern.toLowerCase(), aktpos);
        }
        strb.append(self.substring(aktpos));
        return strb.toString();
    }

    /**
     * Formatiert einen String (wie String.format)
     */
    public static String ebFormat(String str, Object... args) {
        if (str == null) return "";
        return String.format(str, args);
    }

    /**
     * Prüft ob String mit einem der Argumente beginnt
     */
    public static boolean ebStartsWithAny(String str, Object... args) {
        if (str == null) return false;
        for (Object o : args) {
            if (str.startsWith(o.toString())) return true;
        }
        return false;
    }

    /**
     * Prüft ob String mit einem der Argumente endet
     */
    public static boolean ebEndsWithAny(String str, Object... args) {
        if (str == null) return false;
        for (Object o : args) {
            if (str.endsWith(o.toString())) return true;
        }
        return false;
    }

    /**
     * Prüft ob String einer Reihe von Strings gleicht (case-insensitive)
     */
    public static boolean ebIsOneOfStrings(String pattern, String... strings) {
        if (pattern == null) return false;
        for (String s : strings) {
            if (pattern.toLowerCase().equals(s.toLowerCase())) return true;
        }
        return false;
    }

    /**
     * Ersetzt Umlaute
     */
    public static String ebReplaceUmlaute(String self) {
        if (self == null) return "";
        return self.replace("ä", "ae")
                .replace("ö", "oe")
                .replace("ü", "ue")
                .replace("Ä", "Ae")
                .replace("Ö", "Oe")
                .replace("Ü", "Ue")
                .replace("ß", "ss");
    }

    /**
     * Entfernt mehrere Strings aus einem String
     */
    public static String ebRemove(String pattern, String... args) {
        String res = pattern;
        for (String s : args) {
            res = res.replace(s, "");
        }
        return res;
    }

    /**
     * Konvertiert Datums-String zum reversed Format
     */
    public static String ebDateRev(String pattern) {
        if (pattern == null || pattern.isEmpty() || !Character.isDigit(pattern.charAt(0)))
            return pattern;
        try {
            String dateStr = pattern.substring(0, Math.min(10, pattern.length()));
            String[] parts = dateStr.split("[.-]");
            if (parts.length < 3) return pattern;
            int day = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            int year = Integer.parseInt(parts[2]);
            return String.format("%04d%02d%02d", year, month, day);
        } catch (Exception e) {
            return pattern;
        }
    }

    /**
     * Wrapped Text auf einer bestimmten Länge
     */
    public static List<String> ebWrapText(String input, int len) {
        List<String> result = new ArrayList<>();
        if (input == null || input.isEmpty()) return result;

        int actPos = 0;
        while (actPos < input.length()) {
            int n = input.length() - actPos;
            if (n < len) {
                result.add(input.substring(actPos));
                return result;
            }
            String str = input.substring(actPos, actPos + len);
            int i1 = Math.max(str.lastIndexOf(" "), 0);
            i1 = Math.max(i1, str.lastIndexOf("\t"));
            i1 = Math.max(i1, str.lastIndexOf("."));
            i1 = Math.max(i1, str.lastIndexOf("-"));

            if (i1 <= 0) i1 = len - 1;

            str = str.substring(0, i1 + 1);
            result.add(str);
            actPos += i1 + 1;
        }
        return result;
    }

    /**
     * Konvertiert Double zu String mit Komma
     */
    public static String ebToString(double str) {
        return String.format("%.2f", str).replace(".", ",");
    }

    /**
     * Prüft ob Double fast null ist
     */
    public static boolean ebIsFastNull(double self) {
        return Math.abs(self) < 0.000001;
    }

    /**
     * Prüft ob zwei Doubles fast gleich sind
     */
    public static boolean ebIsFastGleich(double self, double other) {
        return ebIsFastNull(self - other);
    }

    /**
     * Prüft ob Double Nullbetrag ist
     */
    public static boolean ebIsNullbetrag(double self) {
        return Math.abs(self) < 0.01;
    }

    /**
     * Konvertiert String zu Double
     */
    public static double ebToDouble(String self) {
        if (ebIsNilOrEmpty(self)) return -1;
        try {
            return Double.parseDouble(self);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Konvertiert String zu Integer
     */
    public static int ebToInt(String self) {
        if (ebIsNilOrEmpty(self)) return -1;
        try {
            return Integer.parseInt(self.replace(".0", ""));
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Konvertiert String zu Long
     */
    public static long ebToLong(String self) {
        if (ebIsNilOrEmpty(self)) return -1;
        try {
            return Long.parseLong(self);
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Extrahiert Wörter aus String
     */
    public static List<String> ebWoerter(String str) {
        if (str == null) return new ArrayList<>();
        StringBuilder strb = new StringBuilder();
        boolean spaceAdded = false;
        for (char ch : str.toCharArray()) {
            if (Character.isLetterOrDigit(ch)) {
                spaceAdded = false;
                strb.append(ch);
            } else if (!spaceAdded) {
                spaceAdded = true;
                strb.append(' ');
            }
        }
        return Arrays.asList(strb.toString().split(" "));
    }

    /**
     * Konvertiert Wörter zu String
     */
    public static String ebWoerterString(String str) {
        if (str == null) return "";
        StringBuilder strb = new StringBuilder();
        for (String w : ebWoerter(str)) {
            strb.append(w).append(" ");
        }
        return strb.toString();
    }

    /**
     * Füllt String mit Spaces auf bestimmte Länge
     */
    public static String ebFilled(String str, int n) {
        if (str == null) str = "";
        if (str.length() >= n) return str;
        StringBuilder strb = new StringBuilder(str);
        while (strb.length() < n) strb.append(" ");
        return strb.toString();
    }

    public static String ebFilled(String str, int n, String separator) {
        if (str == null) str = "";
        if (str.length() > n) return str + separator;
        StringBuilder strb = new StringBuilder(str).append(separator);
        while (strb.length() < n) strb.append(" ");
        return strb.toString();
    }

    /**
     * Kodiert String als JSON-String
     */
    public static String ebCodeString(String str) {
        return "\"" + str + "\"";
    }

    /**
     * Kodiert Boolean als Code-String
     */
    public static String ebCodeString(boolean b) {
        return b ? "true" : "false";
    }

    /**
     * Teilt String als CSV
     */
    public static List<String> ebCsvSplit(String str, char separator) {
        List<String> result = new ArrayList<>();
        if (str == null) return result;
        if (!str.contains("\"")) {
            return Arrays.asList(str.split(Pattern.quote(String.valueOf(separator))));
        }

        StringBuilder strb = new StringBuilder();
        boolean isInKlammern = false;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == separator) {
                if (isInKlammern) {
                    strb.append(c);
                } else {
                    result.add(strb.toString());
                    strb = new StringBuilder();
                }
            } else if (c == '"') {
                if (i + 1 < str.length() && str.charAt(i + 1) == '"') {
                    strb.append('"');
                    i++;
                } else {
                    isInKlammern = !isInKlammern;
                }
            } else {
                strb.append(c);
            }
        }
        result.add(strb.toString());
        return result;
    }

    /**
     * Extrahiert OKU Nummern
     */
    public static String ebOkuNr(String str) {
        if (str == null) return "";
        int index = str.toLowerCase().indexOf("oku-");
        int len = 4;
        if (index < 0) {
            index = str.toLowerCase().indexOf("supp-");
            len = 5;
        }
        if (index < 0) {
            index = str.toLowerCase().indexOf("inf-");
            len = 4;
        }
        if (index < 0) return "";

        int endPos = index + len;
        while (endPos < str.length() && Character.isDigit(str.charAt(endPos)))
            endPos++;
        return str.substring(index, endPos);
    }

    /**
     * Extrahiert JIRA Nummern
     */
    public static List<String> ebGetAllJiraNrs(String self) {
        List<String> list = new ArrayList<>();
        String rest = self;
        while (true) {
            String found = ebGetFirstJiraNr(rest);
            if (found == null) return list;
            list.add(found);
            rest = ebSubstringAfterFirst(rest, found);
        }
    }

    /**
     * Extrahiert erste JIRA Nummer
     */
    public static String ebGetFirstJiraNr(String bemerkungen) {
        if (bemerkungen == null) return null;
        String upperBem = bemerkungen.toUpperCase();
        int index = ebFirstIndexOf(upperBem, "OKU-", "INF-");
        if (index < 0) return null;

        String startsWithJiraNr = upperBem.substring(index);
        int i = startsWithJiraNr.indexOf("-") + 1;
        while (index + i < bemerkungen.length() && Character.isDigit(upperBem.charAt(index + i)))
            i++;
        return bemerkungen.substring(index, index + i);
    }

    /**
     * Findet erstes Index of mehreren Mustern
     */
    public static int ebFirstIndexOf(String self, String... praefixes) {
        int minIndex = Integer.MAX_VALUE;
        for (String str : praefixes) {
            int index = self.indexOf(str);
            if (index >= 0) minIndex = Math.min(minIndex, index);
        }
        return minIndex == Integer.MAX_VALUE ? -1 : minIndex;
    }

    /**
     * Extrahiert Wörter aus String
     */
    public static List<String> ebGetWords(String self) {
        List<String> res = new ArrayList<>();
        if (self == null) return res;

        StringBuilder strb = null;
        for (int i = 0; i < self.length(); i++) {
            char c = self.charAt(i);
            if (Character.isLetter(c) || (strb != null && Character.isDigit(c))) {
                if (strb == null) strb = new StringBuilder();
                strb.append(c);
            } else {
                if (strb != null) {
                    res.add(strb.toString());
                    strb = null;
                }
            }
        }
        if (strb != null) res.add(strb.toString());
        return res;
    }

    /**
     * Ersetzt ein Wort (whole word matching)
     */
    public static String ebReplaceWord(String self, String word, String replacement) {
        if (self == null) return "";
        StringBuilder strb = new StringBuilder();
        int actI = indexOfWord(self, word, 0);
        if (actI < 0) return self;

        if (actI + word.length() < self.length())
            return self.substring(0, actI) + replacement + 
                   ebReplaceWord(self.substring(actI + word.length()), word, replacement);
        else
            return self.substring(0, actI) + replacement;
    }

    /**
     * Findet Index eines Wortes
     */
    private static int indexOfWord(String self, String other, int startPosition) {
        int actI = self.indexOf(other, startPosition);
        if (actI < 0) return actI;
        if (isWord(self, other, actI)) return actI;
        return indexOfWord(self, other, startPosition + 1);
    }

    /**
     * Prüft ob bei Index ein ganzes Wort beginnt
     */
    private static boolean isWord(String self, String other, int actI) {
        if (actI > 0) {
            char ch = self.charAt(actI - 1);
            if (Character.isLetterOrDigit(ch)) return false;
        }

        String subString = self.substring(actI);
        if (subString.equals(other)) return true;
        if (subString.length() <= other.length()) return false;

        char followingChar = subString.charAt(other.length());
        return !Character.isLetterOrDigit(followingChar);
    }

    /**
     * Prüft ob String null oder leer ist
     */
    public static boolean ebIsNilOrEmpty(String wert) {
        return wert == null || wert.isEmpty();
    }

    /**
     * Trimmed einen String oder gibt null zurück
     */
    public static String ebTrim(String wert) {
        return wert == null ? null : wert.trim();
    }

    /**
     * Prüft ob ein Object in einer Liste vorkommt
     */
    public static boolean ebIsOneOf(Object self, Object... args) {
        for (Object arg : args) {
            if (self.equals(arg)) return true;
        }
        return false;
    }

    /**
     * Prüft ob String gleich ist (case-insensitive)
     */
    public static boolean ebEqualsIgnoreCase(String str, String other) {
        if (other == null) return false;
        return str.toLowerCase().equals(other.toLowerCase());
    }

    /**
     * Erstellt Substring mit bestimmter Länge vom Ende
     */
    public static String ebGetShorterString(String self, int n) {
        if (self == null || self.length() <= n) return "";
        return self.substring(0, self.length() - n);
    }

    /**
     * Konvertiert Datums-String von Filename-Format zu deutschem Format
     */
    public static String ebDateString(String localName) {
        if (localName == null || localName.length() < 8) return localName;
        return localName.substring(6, 8) + "." + localName.substring(4, 6) + "." + localName.substring(0, 4);
    }

    /**
     * Trimmed alle Whitespaces vom Anfang
     */
    public static String ebTrimAll(String str) {
        if (str == null) return "";
        int i;
        for (i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c != '\n' && c != '\r' && c != ' ' && !Character.isWhitespace(c)) {
                break;
            }
        }
        return str.substring(i);
    }

    /**
     * Konvertiert Zeit-String von reverser zu normaler Formatierung
     */
    public static String ebTimeStringInverse(String self) {
        if (self == null || self.length() < 13) return "";
        return self.substring(6, 8) + "." + self.substring(4, 6) + "." + self.substring(0, 4) + " " +
               self.substring(9, 11) + " " + self.substring(11, 13);
    }

    /**
     * Konvertiert Command Line Arguments String
     */
    public static List<String> ebCommandLineArguments(String argString) {
        List<String> argumente = new ArrayList<>();
        if (argString == null || argString.isEmpty()) return argumente;

        boolean isInArgument = false;
        boolean isInHochkommata = false;
        StringBuilder strb = new StringBuilder();

        for (int index = 0; index < argString.length(); index++) {
            char ch = argString.charAt(index);

            if (ch == ' ') {
                if (isInHochkommata) {
                    strb.append(ch);
                    continue;
                }
                if (isInArgument) {
                    argumente.add(strb.toString());
                    isInArgument = false;
                    strb = new StringBuilder();
                }
            } else if (ch == '"') {
                if (isInHochkommata) {
                    strb.append(ch);
                    if (strb.length() > 1)
                        argumente.add(strb.toString());
                    isInHochkommata = false;
                    strb = new StringBuilder();
                } else {
                    strb.append(ch);
                    isInHochkommata = true;
                }
            } else {
                strb.append(ch);
                if (isInArgument || isInHochkommata) {
                    continue;
                }
                isInArgument = true;
            }
        }
        if (strb.length() > 0)
            argumente.add(strb.toString());
        return argumente;
    }

    /**
     * Prüft ob String in mehreren Patterns vorkommt
     */
    public static boolean ebContainsOneOf(String str, Iterable<String> patterns, boolean toLower) {
        if (str == null) return false;
        String searchWord = toLower ? str.toLowerCase() : str;
        for (String pattern : patterns) {
            String searchPattern = toLower ? pattern.toLowerCase() : pattern;
            if (searchWord.contains(searchPattern.trim()))
                return true;
        }
        return false;
    }

    /**
     * Prüft ob String mit einem Pattern endet
     */
    public static boolean ebEndsWithOneOf(String text, String spaceSeparatedPatterns) {
        if (text == null || spaceSeparatedPatterns == null) return false;
        for (String pattern : spaceSeparatedPatterns.split(" ")) {
            if (text.endsWith(pattern)) return true;
        }
        return false;
    }

    /**
     * Prüft ob String mit einem Pattern beginnt
     */
    public static boolean ebStartsWithOneOf(String text, String spaceSeparatedPatterns) {
        if (text == null || spaceSeparatedPatterns == null) return false;
        for (String pattern : spaceSeparatedPatterns.split(" ")) {
            if (text.startsWith(pattern)) return true;
        }
        return false;
    }

}
