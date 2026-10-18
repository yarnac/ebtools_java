package com.eb.base.gui;

import java.awt.*;
import java.util.Arrays;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FontUtil {

    // RegEx für typische Monospace-Font-Namen (case-insensitive, Wortgrenzen)
    private static final Pattern MONO_NAME_PATTERN = Pattern.compile(
            "(?i)\\b(mono|fixed|courier|consolas|terminus|inconsolata|roboto\\s*mono|monospaced)"
    );

    // Thread-sicheres Caching des System-Fallbacks
    private static volatile Font cachedFallbackMonospace;

    public static Font determineMonospaceFont(int fontSize)
    {
        return determineMonospaceFont(fontSize, "JetBrains Mono", "Consolas", "Monospaces");
    }

    public static void setMonospaceFont(int fontSize, Component... components)
    {
        Font font = determineMonospaceFont(fontSize);
        for(Component component : components)
            component.setFont(font);

    }

    public static Font determineMonospaceFont(int fontSize, String... fonts) {
        if (fonts == null || fonts.length == 0) {
            return getSystemFallbackMonospace(fontSize);
        }

        String[] availableFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        Set<String> availableSet = Arrays.stream(availableFamilies)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toUnmodifiableSet());

        // 1. Geforderte Fonts prüfen
        for (String requested : fonts) {
            if (requested == null || requested.trim().isEmpty()) continue;
            String cleanName = requested.trim();
            if (availableSet.contains(cleanName)) {
                return new Font(cleanName, Font.PLAIN, fontSize);
            }
        }

        // 2. Fallback: Systematisch nach Monospace-Font suchen
        return getSystemFallbackMonospace(fontSize);
    }

    /**
     * Findet den ersten verfügbaren Monospace-Font des Systems.
     * Nutzung von Double-Checked Locking für effizientes Caching.
     */
    private static synchronized Font getSystemFallbackMonospace(int fontSize) {
        Font result = cachedFallbackMonospace;
        if (result != null) return result;

        String[] allFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();

        // Versuche, einen Font anhand von Namensmustern zu finden
        for (String family : allFamilies) {
            if (family != null && MONO_NAME_PATTERN.matcher(family.trim()).find()) {
                cachedFallbackMonospace = new Font(family.trim(), Font.PLAIN, fontSize);
                return cachedFallbackMonospace;
            }
        }

        // Ultimate Fallback auf systemweite Standard-Aliase
        Set<String> set = Arrays.stream(allFamilies)
                .map(String::trim)
                .collect(Collectors.toUnmodifiableSet());

        for (String alias : new String[]{"Monospaced", "DefaultMonospaced", "Courier New", "DejaVu Sans Mono"}) {
            if (set.contains(alias)) {
                cachedFallbackMonospace = new Font(alias, Font.PLAIN, fontSize);
                return cachedFallbackMonospace;
            }
        }

        // Absicherung: Immer ein syntaktisch gültiges Font-Objekt zurückgeben
        cachedFallbackMonospace = new Font("MonoSpaced", Font.PLAIN, fontSize);
        return cachedFallbackMonospace;
    }

    public static void setFont(Font font, Component... components) {
        for (Component component : components) {
            component.setFont(font);
        }
    }
}