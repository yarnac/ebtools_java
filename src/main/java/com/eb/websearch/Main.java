package com.eb.websearch;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Ollama URL (Standard: http://localhost:11434)
        String ollamaUrl = "http://localhost:11434";

        // Optional: API Key für externe Suchmaschine (z. B. Tavily)
        // Alternativ: Setze externalApiKey = null und nutze nur WebsearchTool.searchWeb()
        String tavilyApiKey = System.getenv("TAVILY_API_KEY"); // oder ein festgelegter Key

        try {
            WebsearchTool tool = new WebsearchTool(ollamaUrl);

            // 1. Einfache Webrecherche ohne externes API (nur Prompt-Injektion)
            String simpleResult = tool.searchWeb("Welche neuen Technologien gibt es im Bereich KI im Jahr 2025?");
            System.out.println("=== Einfache Webrecherche ===");
            System.out.println(simpleResult);

            // 2. Erweiterte Recherche mit externem Search-Service
            if (tavilyApiKey != null && !tavilyApiKey.isEmpty()) {
                String enhancedResult = tool.searchWebWithExternalAPI(
                    "Was ist der aktuelle Bitcoin-Kurs und warum entwickelt er sich so?",
                    tavilyApiKey,
                    "https://api.tavily.com/search"
                );
                System.out.println("\n=== Erweiterte Webrecherche ===");
                System.out.println(enhancedResult);
            } else {
                System.out.println("\nTavily API Key nicht gesetzt — überspringe Erweiterung.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            System.err.println("Fehler bei der Webrecherche: " + e.getMessage());
        }
    }
}