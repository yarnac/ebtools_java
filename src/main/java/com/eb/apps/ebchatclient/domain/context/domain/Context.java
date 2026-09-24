package com.eb.apps.ebchatclient.domain.context.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Context {
    private String name;
    private String knoten;
    private String userString;

    public Context() {
    }

    public Context(String name) {
        this.name = name;
    }

    public Context(String name, String knoten, String userString) {
        this.name = name;
        this.knoten = knoten;
        this.userString = userString;
    }

    public String getContextString()
    {
        return userString;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
