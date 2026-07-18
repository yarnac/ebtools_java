package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.llm_client.api.LlmMessage;
import com.eb.ai_service.llm_client.api.LlmRequest;

import java.util.ArrayList;
import java.util.List;

public class AbstrLlmClientTest {

    private  static List<LlmMessage> createTestMessages() {

        List<LlmMessage> messages = new ArrayList<>();
        messages.add(new LlmMessage("user", "Übersetze das türkische Wort yapmak nach Deutsch. Liefere kein Reasoning und keine MD Formatierung"));
                return messages;
    }

    public static LlmRequest createTestRequest(String model) {
        LlmRequest request = new LlmRequest(model, createTestMessages());
        return request;
    }
}
