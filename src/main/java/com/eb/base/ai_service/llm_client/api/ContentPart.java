package com.eb.base.ai_service.llm_client.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentPart {
    @JsonProperty("type")
    private String type;

    @JsonProperty("text")
    private String text;

    // Optional: Für Bild-URLs
    @JsonProperty("image_url")
    private ImageUrl imageDetails;

    public ContentPart(String type) {
        this.type = type;
    }
}