package com.eb.apps.ebchat_client.components;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeBlockReplacer {

    private static final Pattern CODE_BLOCK =
            Pattern.compile("(?s)```[^\\r\\n]*\\R?(.*?)```");

    private static final Pattern FILE_NAME =
            Pattern.compile("\"fileName\"\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");

    public static String replaceCodeBlocks(String input) {

        if (input == null) {
            return null;
        }

        Matcher blocks = CODE_BLOCK.matcher(input);
        StringBuffer result = new StringBuffer();

        while (blocks.find()) {
            Matcher fileNames = FILE_NAME.matcher(blocks.group(1));
            List<String> replacements = new ArrayList<>();

            while (fileNames.find()) {
                // Der Pfad bleibt wie im JSON escaped, z. B. C:\\workspaces\\...
                replacements.add("<$ " + fileNames.group(1));
            }

            // Blöcke ohne fileName bleiben unverändert.
            String replacement = replacements.isEmpty()
                    ? blocks.group()
                    : String.join(System.lineSeparator(), replacements);

            blocks.appendReplacement(result, Matcher.quoteReplacement(replacement));
        }

        blocks.appendTail(result);
        return result.toString();
    }
}