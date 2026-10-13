package com.eb.apps.ebchat_client.domain.snippets;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Snippet {
    private String name;
    private String knoten;
    private String snippet;

    public Snippet() {
    }

    public Snippet(String name) {
        this.name = name;
    }

    public Snippet(String name, String knoten, String snippetText) {
        this.name = name;
        this.knoten = knoten;
        this.snippet = snippetText;
    }


    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
