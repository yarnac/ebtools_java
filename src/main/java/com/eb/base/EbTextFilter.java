package com.eb.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class EbTextFilter {

    public enum Mode {
        UND, ODER
    }

    private boolean caseSensitive = true;

    // Standard Java Bean Pattern für Getter/Setter
    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    public void setCaseSensitive(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    public static boolean testPosNeg(String textToCheck, String words, Mode mode,
                                     boolean caseSensitive, boolean wrapWords) {
        if (words == null || words.trim().isEmpty()) {
            return true;
        }

        List<String> positiveWords = Arrays.stream(words.split(" "))
                .filter(x -> !x.startsWith("-"))
                .toList();

        List<String> negativeWords = Arrays.stream(words.split(" "))
                .filter(x -> x.startsWith("-"))
                .map(x -> x.substring(1))
                .toList();

        return test(textToCheck, positiveWords, negativeWords, mode, caseSensitive, wrapWords);
    }

    public static boolean test(String textToCheck, String wordsToCheckPositive, String wordsToCheckNegative,
                               Mode mode, boolean caseSensitive, boolean wrapWords) {
        List<String> positiveWords = Arrays.stream(wordsToCheckPositive.split(" "))
                .filter(x -> !x.isEmpty())
                .toList();
        List<String> negativeWords = Arrays.stream(wordsToCheckNegative.split(" "))
                .filter(x -> !x.isEmpty())
                .toList();

        return test(textToCheck, positiveWords, negativeWords, mode, mode, caseSensitive, wrapWords);
    }

    public static boolean test(String textToCheck, Iterable<String> positiveWords, Iterable<String> negativeWords,
                               Mode mode, boolean caseSensitive, boolean wrapWords) {
        List<String> posList = asList(positiveWords);
        List<String> negList = asList(negativeWords);

        return test(textToCheck, posList, negList, mode, mode, caseSensitive, wrapWords);
    }

    public static boolean testUnd(String textToCheck, Iterable<String> positiveWords, Iterable<String> negativeWords,
                                  boolean caseSensitive, boolean wrapWords) {
        List<String> posList = asList(positiveWords);
        List<String> negList = asList(negativeWords);
        return test(textToCheck, posList, negList, Mode.UND, Mode.UND, caseSensitive, wrapWords);
    }

    public static boolean testOder(String textToCheck, Iterable<String> positiveWords, Iterable<String> negativeWords,
                                   boolean caseSensitive, boolean wrapWords) {
        List<String> posList = asList(positiveWords);
        List<String> negList = asList(negativeWords);
        return test(textToCheck, posList, negList, Mode.ODER, Mode.ODER, caseSensitive, wrapWords);
    }

    public static boolean testUndOder(String textToCheck, Iterable<String> positiveWords, Iterable<String> negativeWords,
                                      boolean caseSensitive, boolean wrapWords) {
        List<String> posList = asList(positiveWords);
        List<String> negList = asList(negativeWords);
        return test(textToCheck, posList, negList, Mode.UND, Mode.ODER, caseSensitive, wrapWords);
    }

    public static boolean testOderUnd(String textToCheck, Iterable<String> positiveWords, Iterable<String> negativeWords,
                                      boolean caseSensitive, boolean wrapWords) {
        List<String> posList = asList(positiveWords);
        List<String> negList = asList(negativeWords);
        return test(textToCheck, posList, negList, Mode.ODER, Mode.UND, caseSensitive, wrapWords);
    }

    public static boolean test(String textToCheck, Collection<String> positiveWords, Collection<String> negativeWords,
                               Mode modePos, Mode modeNeg, boolean caseSensitive, boolean wrapWords) {

        if (!testPositive(textToCheck, positiveWords, modePos, caseSensitive, wrapWords)) {
            return false;
        }

        if (testNegative(textToCheck, negativeWords, modeNeg, caseSensitive, wrapWords)) {
            return false;
        }

        return true;
    }

    public static boolean testPositive(String str, Iterable<String> words, Mode mode,
                                       boolean caseSensitive, boolean wrapWords) {
        List<String> reducedList = new ArrayList<>();
        for (String word : words) {
            String w = word.trim();
            if (!w.isEmpty()) {
                reducedList.add(w);
            }
        }

        if (reducedList.isEmpty()) {
            return true;
        }

        return innerTest(str, mode, caseSensitive, reducedList);
    }

    public static boolean testNegative(String str, Iterable<String> words, Mode mode,
                                       boolean caseSensitive, boolean wrapWords) {
        List<String> reducedList = new ArrayList<>();
        for (String word : words) {
            String w = word.trim();
            if (!w.isEmpty()) {
                reducedList.add(w);
            }
        }

        if (reducedList.isEmpty()) {
            return false;
        }

        return innerTest(str, mode, caseSensitive, reducedList);
    }

    // Hilfsfunktion zur Konvertierung beliebiger Iterables in eine List
    private static List<String> asList(Iterable<String> iterable) {
        List<String> list = new ArrayList<>();
        for (String s : iterable) {
            list.add(s);
        }
        return list;
    }

    private static boolean innerTest(String textToTest, Mode mode, boolean caseSensitive, Iterable<String> reducedList) {
        // Caching der Lowercase-Version für性能 bei Case-Insensitivity
        String lowerText = null;
        if (!caseSensitive) {
            lowerText = textToTest.toLowerCase(Locale.ROOT);
        }

        for (String wordToTestWith : reducedList) {
            if (wordToTestWith.startsWith("!")) {
                String target = wordToTestWith.substring(1);
                boolean matchesStart;
                if (caseSensitive) {
                    matchesStart = textToTest.startsWith(target);
                } else {
                    matchesStart = lowerText.indexOf(target.toLowerCase(Locale.ROOT)) == 0;
                }

                if (matchesStart) {
                    if (mode == Mode.ODER) return true;
                    continue;
                }
            }

            boolean matchesContain;
            if (caseSensitive) {
                matchesContain = textToTest.contains(wordToTestWith);
            } else {
                matchesContain = lowerText.indexOf(wordToTestWith.toLowerCase(Locale.ROOT)) != -1;
            }

            if (matchesContain) {
                if (mode == Mode.ODER) return true;
            } else if (mode == Mode.UND) {
                return false;
            }
        }

        // Äquivalent zur ursprünglichen Logik:
        // if (mode == Mode.ODER) return false; return true;
        return mode == Mode.UND;
    }
}
