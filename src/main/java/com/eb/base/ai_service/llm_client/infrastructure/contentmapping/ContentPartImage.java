package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

public record ContentPartImage(
        String mimeType,
        String base64
) implements ContentPart {
}

