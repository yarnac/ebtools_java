package com.eb.base.ai_service.llm_client.api;

import lombok.Getter;

@Getter
public class Parameter {
    private final String name;
    private final Object value;

    public Parameter(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
