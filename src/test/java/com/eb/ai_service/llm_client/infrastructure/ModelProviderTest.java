package com.eb.ai_service.llm_client.infrastructure;

import com.eb.base.ai_service.llm_client.infrastructure.LlmModelProvider;
import org.junit.jupiter.api.Test;

class ModelProviderTest {

    @Test
    public void test() {
        LlmModelProvider mp = new LlmModelProvider();
        mp.getAllModels();
    }

}