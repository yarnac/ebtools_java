package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

import java.util.Map;

public interface ContentPartToMapMapper {

    Map<String, Object> map(ContentPart part);
    String getContentIdentifier();
    default boolean shouldSendMaxTokens() {return false;}
    default boolean shouldSendSystemSeparately() {return false;}
}

