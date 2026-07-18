package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmResponse;

import java.io.IOException;

public interface ILlmClient {

    LlmResponse sendRequest(LlmRequest request) throws IOException, InterruptedException;
}
