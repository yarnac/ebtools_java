package com.eb.apps.ebchatclient.domain.context.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class ContextWithFiles extends Context {

    private List<String> fileNames = new ArrayList<>();

    public ContextWithFiles() {
        super();
    }

    public ContextWithFiles(String name) {
        super(name);
    }

    public ContextWithFiles(String name, String knoten, String userString) {
        super(name, knoten, userString);
    }

    public String getContextString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getUserString()).append("\n");
        for (String fileName : fileNames)
        {
            sb
                    .append("<$ ")
                    .append(fileName)
                    .append("\n");
        }
        return sb.toString();
    }
}
