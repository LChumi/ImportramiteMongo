package com.cumpleanos.importramite.utils;

public class StringUtils {
    public static String trimWhitespace(String input) {
        return input == null ? null : input.trim();
    }

    public static String sanitizeForMongoDB(String input) {
        if (input == null) return null;
        return trimWhitespace(input).replaceAll("[^\\w\\s]", ""); // Remueve caracteres especiales

    }
}