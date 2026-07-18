package com.eb.ai_service.llm_client.infrastructure;

import org.junit.jupiter.api.Test;

class ModelProviderTest {

    @Test
    public void test() {
        ModelProvider mp = new ModelProvider();
        mp.getAllModels();
    }

}