package com.eb.base.ai_service.llm_client.infrastructure.contentmapping;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import lombok.Getter;
import lombok.Setter;

public class LlmMessageMapper {

    @Getter
    @Setter
    private ContentPartToMapMapper contentPartToMapMapper;

    public LlmMessageMapper(ContentPartToMapMapper contentPartToMapMapper) {
        this.contentPartToMapMapper = contentPartToMapMapper;
    }

    public LlmMessageRaw getRawMessage(LlmMessage llmMessage) {
        LlmMessageRaw message = new LlmMessageRaw(llmMessage.getRole());
        for (ContentPart contentPart : llmMessage.getContentParts()) {
            message.addContentPartMap(contentPartToMapMapper.map(contentPart));
        }
        return message;
    }

}
