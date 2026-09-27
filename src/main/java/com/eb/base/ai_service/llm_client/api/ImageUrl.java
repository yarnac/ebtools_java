package com.eb.base.ai_service.llm_client.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageUrl {
    @JsonProperty("url")
    private String url;

    // Optional Detail-Level (auto, low, high)
    @JsonProperty("detail")
    private String detail;

    public ImageUrl(String url) {
        this.url = url;
    }

    public ImageUrl(String url, String detail) {
        this.url = url;
        this.detail = detail;
    }
}