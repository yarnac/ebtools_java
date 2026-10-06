package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;



import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ContentPartText.class, name = "text"),
        @JsonSubTypes.Type(value = ContentPartImage.class, name = "image")
})

public sealed interface ContentPart
        permits ContentPartText, ContentPartImage {
}


