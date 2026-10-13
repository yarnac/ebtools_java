package com.eb.apps.snippets.domain;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class SnippetTreeRepository {
    String fileName;
    ObjectMapper objectMapper = new ObjectMapper();

    public SnippetTreeRepository(String fileName) {
        this.fileName = fileName;
    }

    public SnippetTree loadSnippetTree() {
        try {
            return objectMapper.readValue(new File(fileName), SnippetTree.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveSnippetTree(SnippetTree snippetTree) {
        try {
            objectMapper.writeValue(new File(fileName), snippetTree);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
