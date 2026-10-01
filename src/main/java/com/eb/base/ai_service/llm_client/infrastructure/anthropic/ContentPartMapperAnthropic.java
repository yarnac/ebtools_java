package com.eb.base.ai_service.llm_client.infrastructure.anthropic;

import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPart;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartToMapMapper;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartImage;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartText;

import java.util.Map;

/**
 * Mapper zur Transformation von ContentParts in das Format der Anthropic API (v1).
 */
public class ContentPartMapperAnthropic implements ContentPartToMapMapper {

    @Override
    public Map<String, Object> map(ContentPart part) {
        return switch (part) {
            case ContentPartText text -> Map.of(
                    "type", "text",
                    "text", text.text()
            );

            case ContentPartImage image -> {
                // Anthropic erwartet das Bild im 'source'-Objekt, getrennt in Typ und Daten
                Map<String, Object> sourceMap = Map.of(
                        "type", "base64",
                        "media_type", image.mimeType(),
                        "data", image.base64()
                );

                yield Map.of(
                        "type", "image",
                        "source", sourceMap
                );
            }
        };
    }

    @Override
    public String getContentIdentifier() {
        return "messages";
    }

    @Override public boolean shouldSendMaxTokens() {return true;}

    @Override public boolean shouldSendSystemSeparately() {return true;}
}
