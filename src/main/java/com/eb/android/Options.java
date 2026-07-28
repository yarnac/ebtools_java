package com.eb.android;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Options {
    private String title;
    private String message;
    public enum Type {
        Title,
        Message,
    };
    private Type type;
}
