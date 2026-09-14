package com.eb.apps.ebchatclient.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LlmMessageDeprecated {

    private String role;
    private String content;
}
