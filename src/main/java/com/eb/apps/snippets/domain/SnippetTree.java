package com.eb.apps.snippets.domain;

import com.eb.base.EbTextFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


public class SnippetTree {
    @Getter
    private final List<SnippetTree> children;
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String snippet;
    @Getter @Setter
    private String dataValue;

    public SnippetTree() {
        children = new ArrayList<SnippetTree>();
    }

    public boolean containsSearchMuster(String pattern)
    {
        if (isAllowedString(getSnippet(), pattern))
            return true;

        return !getChildren()
                .stream()
                .filter(child -> child.containsSearchMuster(pattern))
                .findAny()
                .isEmpty();
    }

    private boolean isAllowedString(String snippet, String pattern) {
        return EbTextFilter.testPosNeg(snippet, pattern, EbTextFilter.Mode.ODER, false, false);
    }
}
