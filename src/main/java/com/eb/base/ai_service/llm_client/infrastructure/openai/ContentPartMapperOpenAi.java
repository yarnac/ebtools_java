package com.eb.base.ai_service.llm_client.infrastructure.openai;

import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPart;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartToMapMapper;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartImage;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.ContentPartText;

import java.util.Map;

public class ContentPartMapperOpenAi
        implements ContentPartToMapMapper {

    @Override
    public Map<String, Object> map(ContentPart part) {
        return switch (part) {
            case ContentPartText text -> Map.of(
                    "type", "input_text",
                    "text", text.text()
            );

            case ContentPartImage image -> Map.of(
                    "type", "input_image",
                    "image_url",
                    "data:" + image.mimeType()
                            + ";base64," + image.base64()
            );
        };
    }

    @Override
    public String getContentIdentifier() {
        return "input";
    }
}
