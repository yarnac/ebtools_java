package com.eb.ai_service.llm_client.infrastructure.anthropic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AnthropicResponse {

    private String model;
    private String id;
    private String type;
    private String role;
    private List<Content> content;

    @JsonProperty("stop_reason")
    private String stopReason;

    @JsonProperty("stop_sequence")
    private String stopSequence;

    @JsonProperty("stop_details")
    private Object stopDetails;

    private Usage usage;

    public AnthropicResponse() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getStopReason() {
        return stopReason;
    }

    public void setStopReason(String stopReason) {
        this.stopReason = stopReason;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.stopSequence = stopSequence;
    }

    public Object getStopDetails() {
        return stopDetails;
    }

    public void setStopDetails(Object stopDetails) {
        this.stopDetails = stopDetails;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Content {

        private String type;
        private String text;

        public Content() {
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {

        @JsonProperty("input_tokens")
        private int inputTokens;

        @JsonProperty("cache_creation_input_tokens")
        private int cacheCreationInputTokens;

        @JsonProperty("cache_read_input_tokens")
        private int cacheReadInputTokens;

        @JsonProperty("cache_creation")
        private CacheCreation cacheCreation;

        @JsonProperty("output_tokens")
        private int outputTokens;

        @JsonProperty("service_tier")
        private String serviceTier;

        @JsonProperty("inference_geo")
        private String inferenceGeo;

        public Usage() {
        }

        public int getInputTokens() {
            return inputTokens;
        }

        public void setInputTokens(int inputTokens) {
            this.inputTokens = inputTokens;
        }

        public int getCacheCreationInputTokens() {
            return cacheCreationInputTokens;
        }

        public void setCacheCreationInputTokens(int cacheCreationInputTokens) {
            this.cacheCreationInputTokens = cacheCreationInputTokens;
        }

        public int getCacheReadInputTokens() {
            return cacheReadInputTokens;
        }

        public void setCacheReadInputTokens(int cacheReadInputTokens) {
            this.cacheReadInputTokens = cacheReadInputTokens;
        }

        public CacheCreation getCacheCreation() {
            return cacheCreation;
        }

        public void setCacheCreation(CacheCreation cacheCreation) {
            this.cacheCreation = cacheCreation;
        }

        public int getOutputTokens() {
            return outputTokens;
        }

        public void setOutputTokens(int outputTokens) {
            this.outputTokens = outputTokens;
        }

        public String getServiceTier() {
            return serviceTier;
        }

        public void setServiceTier(String serviceTier) {
            this.serviceTier = serviceTier;
        }

        public String getInferenceGeo() {
            return inferenceGeo;
        }

        public void setInferenceGeo(String inferenceGeo) {
            this.inferenceGeo = inferenceGeo;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CacheCreation {

        @JsonProperty("ephemeral_5m_input_tokens")
        private int ephemeral5mInputTokens;

        @JsonProperty("ephemeral_1h_input_tokens")
        private int ephemeral1hInputTokens;

        public CacheCreation() {
        }

        public int getEphemeral5mInputTokens() {
            return ephemeral5mInputTokens;
        }

        public void setEphemeral5mInputTokens(int ephemeral5mInputTokens) {
            this.ephemeral5mInputTokens = ephemeral5mInputTokens;
        }

        public int getEphemeral1hInputTokens() {
            return ephemeral1hInputTokens;
        }

        public void setEphemeral1hInputTokens(int ephemeral1hInputTokens) {
            this.ephemeral1hInputTokens = ephemeral1hInputTokens;
        }
    }
}