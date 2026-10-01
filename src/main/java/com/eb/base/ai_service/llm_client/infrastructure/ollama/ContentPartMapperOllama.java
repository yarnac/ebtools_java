package com.eb.base.ai_service.llm_client.infrastructure.ollama;

import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPart;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartToMapMapper;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartImage;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartText;

import java.util.Map;

public class ContentPartMapperOllama
        implements ContentPartToMapMapper {

    @Override
    public Map<String, Object> map(ContentPart part) {
        return switch (part) {
            case ContentPartText text -> Map.of(
                    "type", "text",
                    "text", text.text()
            );

            case ContentPartImage image -> Map.of(
                    "type", "image_url",
                    "image_url", Map.of(
                            "url", "data:" + image.mimeType()
                                    + ";base64," + image.base64()
                    )
            );
        };
    }

    @Override
    public String getContentIdentifier() {
        return "messages";
    }
}
