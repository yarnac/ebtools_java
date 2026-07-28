package com.eb.base.extensions;

import java.util.*;
import java.util.stream.Collectors;

public class CollectionExtensions {

    /**
     * Prüft ob Liste null oder leer ist
     */
    public static <T> boolean ebIsNilOrEmpty(List<T> werte) {
        return werte == null || werte.isEmpty();
    }

    /**
     * Konvertiert String-Array zu List
     */
    public static List<String> ebToList(String[] strings) {
        if (strings == null) return new ArrayList<>();
        return Arrays.asList(strings);
    }

    /**
     * Konvertiert List zu Array
     */
    public static String[] ebToArray(List<String> strings) {
        if (strings == null) return new String[0];
        return strings.toArray(new String[0]);
    }

    /**
     * Konvertiert generisches Array zu Object-Array
     */
    public static <T> Object[] ebToObjArr(T[] objects) {
        if (objects == null) return new Object[0];
        Object[] res = new Object[objects.length];
        System.arraycopy(objects, 0, res, 0, objects.length);
        return res;
    }

    /**
     * Konvertiert Iterable zu Object-Array
     */
    public static <T> Object[] ebToObjArr(Iterable<T> objects) {
        if (objects == null) return new Object[0];
        List<T> list = new ArrayList<>();
        for (T t : objects) {
            if (t != null) list.add(t);
        }
        Object[] res = new Object[list.size()];
        int i = 0;
        for (T t : list) {
            res[i++] = t;
        }
        return res;
    }

    /**
     * Findet einen Index in String-Array
     */
    public static int ebIndexOf(String[] strs, String pattern) {
        if (strs == null) return -1;
        for (int i = 0; i < strs.length; i++) {
            if (strs[i] != null && strs[i].equals(pattern)) return i;
        }
        return -1;
    }

    /**
     * Gibt Index in String-Array oder Element zurück
     */
    public static String ebGet(String[] str, int nr) {
        if (str == null || str.length <= nr) return null;
        return str[nr];
    }

    /**
     * Gibt Index in List oder Element zurück
     */
    public static String ebGet(List<String> str, int nr) {
        if (str == null || str.size() <= nr) return null;
        return str.get(nr);
    }

    /**
     * Konvertiert String-List zu String mit Separator
     */
    public static String ebSeparatedString(Iterable<String> strings, String separator) {
        return ebSeparatedString(strings, separator, true);
    }

    public static String ebSeparatedString(Iterable<String> strings, String separator, boolean ignoreEmptyStrings) {
        if (strings == null) return "";
        StringBuilder strb = new StringBuilder();
        for (String s : strings) {
            if (ignoreEmptyStrings && (s == null || s.trim().isEmpty())) continue;
            if (strb.length() > 0) strb.append(separator);
            strb.append(s);
        }
        return strb.toString();
    }

    /**
     * Konvertiert String-List zu Zeilen
     */
    public static String ebAsLines(Iterable<String> strings) {
        return ebAsLines(strings, "\n");
    }

    public static String ebAsLines(Iterable<String> strings, String separator) {
        return ebSeparatedString(strings, separator);
    }

    /**
     * Konvertiert String-List zu Text aus Zeilen
     */
    public static String textFromLines(Iterable<String> strings) {
        return textFromLines(strings, "\n");
    }

    public static String textFromLines(Iterable<String> strings, String separator) {
        if (strings == null) return "";
        StringBuilder strb = new StringBuilder();
        for (String s : strings) {
            if (strb.length() > 0) strb.append(separator);
            strb.append(s.trim());
        }
        return strb.toString();
    }

    /**
     * Trimmed alle Objekte zu String-List
     */
    public static List<String> trimmedWerte(Object[] werte) {
        List<String> res = new ArrayList<>();
        if (werte == null) return res;
        for (Object o : werte) {
            if (o == null) continue;
            String trimmedWert = o.toString().trim();
            if (trimmedWert.length() > 0) res.add(trimmedWert);
        }
        return res;
    }

    /**
     * Teilt List in kleinere Lists auf
     */
    public static List<List<String>> ebSplit(List<String> lines, int size) {
        List<List<String>> res = new ArrayList<>();
        if (lines == null || lines.isEmpty()) return res;
        
        List<String> actList = new ArrayList<>();
        res.add(actList);

        int n = 0;
        for (String line : lines) {
            if (n++ == size) {
                actList = new ArrayList<>();
                res.add(actList);
                n = 0;
            }
            actList.add(line);
        }
        return res;
    }

    /**
     * Entfernt leere Strings aus List (in-place)
     */
    public static void ebRemoveEmptyStrings(List<String> orig) {
        if (orig == null) return;
        orig.removeIf(x -> x == null || x.length() == 0);
    }

    /**
     * Filtert Liste und gibt gefilterte Elemente zurück
     */
    public static List<String> ebFilterWhere(List<String> self, java.util.function.Predicate<String> test) {
        if (self == null) return new ArrayList<>();
        List<String> list = self.stream().filter(test).collect(Collectors.toList());
        self.removeIf(test);
        return list;
    }

    /**
     * Findet oder erstellt ein Element in einer Liste
     */
    public static <T> T ebFindOrCreate(List<T> self, java.util.function.Predicate<T> predicate, java.util.function.Supplier<T> creator) {
        if (self == null) self = new ArrayList<>();
        for (T item : self) {
            if (predicate.test(item)) return item;
        }
        T obj = creator.get();
        self.add(obj);
        return obj;
    }

}
