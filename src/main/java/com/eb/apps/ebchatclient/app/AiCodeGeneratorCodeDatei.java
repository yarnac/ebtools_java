package com.eb.apps.ebchatclient.app;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AiCodeGeneratorCodeDatei {
    @JsonProperty("fileName") String name;
    @JsonProperty("type") String type;
    @JsonProperty("content") String content;
}
