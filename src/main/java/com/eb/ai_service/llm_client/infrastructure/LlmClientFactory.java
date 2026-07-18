package com.eb.ai_service.llm_client.infrastructure;

import com.eb.ai_service.AiServiceConfig;
import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.infrastructure.anthropic.AnthropicClient;
import com.eb.ai_service.llm_client.infrastructure.ollama.OllamaClient;
import com.eb.ai_service.llm_client.infrastructure.openai.OpenAiClient;
import com.eb.base.crypt.algorithmes.Twofish_Crypter;

import java.io.IOException;
import java.net.http.HttpClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class LlmClientFactory {

    private static String openAiKey;
    private static String anthropicKey;

    private static void loadKeys()
    {
        try {
            String fileName = AiServiceConfig.current().getGeheimnisFileName();
            List<String> lines = Files.readAllLines(Path.of((fileName)));
            String line0 = lines.get(0);
            String decrypted0 = new Twofish_Crypter().decryptString(line0, "ekkartist33924840");
            openAiKey = decrypted0.substring(decrypted0.indexOf("=")+1);
            String line1 = lines.get(1);
            String decrypted1 = new Twofish_Crypter().decryptString(line1, "ekkartist33924840");
            anthropicKey = decrypted1.substring(decrypted1.indexOf("=")+1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public LlmClientFactory() {
        modelProvider = new ModelProvider();

        List<String> lines = null;
    }

    private ModelProvider modelProvider;

    public static String getAnthropicApiKey() {
        if (anthropicKey==null)
            loadKeys();

        return anthropicKey;
    }

    public static String getOpenAiKey() {
        if (openAiKey==null)
            loadKeys();
        return openAiKey;
    }


    public ILlmClient getLlmClient(LlmRequest request) {

        String modelName = request.getModel();

        LlmModel model = modelProvider.getModel(modelName);

        if (model == null) {
            throw new IllegalArgumentException("Model not found: " + modelName);
        }

        if (model.isAnthropic())
            return new AnthropicClient(HttpClient.newHttpClient(), getAnthropicApiKey());
        if (model.isOpenAi())
            return new OpenAiClient(HttpClient.newHttpClient(), getOpenAiKey());
        if (model.isOllama())
            return new OllamaClient(HttpClient.newHttpClient());

        throw new IllegalArgumentException("Provider not found: " + modelName);
    }
}
