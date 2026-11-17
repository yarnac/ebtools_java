package com.eb.base.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonExtensions {

    private static final ObjectMapper mapper = new ObjectMapper();

    /**
     * Serialisiert String zu JSON
     */
    public static String jsonFromString(String str) {
        if (str == null) return "\"\"";
        try {
            return mapper.writeValueAsString(str);
        } catch (Exception e) {
            System.err.println("Error serializing string to JSON: " + e.getMessage());
            return "\"\"";
        }
    }

    /**
     * Deserialisieret JSON zu String
     */
    public static String jsonToString(String str) {
        if (StringExtensions.ebIsNilOrEmpty(str)) return "";
        try {
            return mapper.readValue(str, String.class);
        } catch (Exception e) {
            System.err.println("Error deserializing JSON to string: " + e.getMessage());
            return "";
        }
    }

    /**
     * Serialisiert List<String> zu JSON
     */
    public static String jsonFromStringList(List<String> str) {
        if (str == null) return "[]";
        try {
            return mapper.writeValueAsString(str);
        } catch (Exception e) {
            System.err.println("Error serializing list to JSON: " + e.getMessage());
            return "[]";
        }
    }

    /**
     * Deserialisieret JSON zu List<String>
     */
    public static List<String> jsonToStringList(String str) {
        if (StringExtensions.ebIsNilOrEmpty(str)) return new java.util.ArrayList<>();
        try {
            return mapper.readValue(str, mapper.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (Exception e) {
            System.err.println("Error deserializing JSON to string list: " + e.getMessage());
            return new java.util.ArrayList<>();
        }
    }
}
