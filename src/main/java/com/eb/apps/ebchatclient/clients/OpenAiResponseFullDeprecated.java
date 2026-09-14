package com.eb.apps.ebchatclient.clients;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenAiResponseFullDeprecated {

    @JsonProperty("id")
    private String id;

    @JsonProperty("object")
    private String object;

    @JsonProperty("created_at")
    private long createdAt;

    @JsonProperty("status")
    private String status;

    @JsonProperty("background")
    private boolean background;

    @JsonProperty("billing")
    private Billing billingInfo;

    @JsonProperty("completed_at")
    private long completedAt;

    @JsonProperty("error")
    private JsonNode error;

    @JsonProperty("frequency_penalty")
    private double frequencyPenalty;

    @JsonProperty("incomplete_details")
    private JsonNode incompleteDetails;

    @JsonProperty("instructions")
    private String instructions;

    @JsonProperty("max_output_tokens")
    private Integer maxOutputTokens;

    @JsonProperty("max_tool_calls")
    private Integer maxToolCalls;

    @JsonProperty("model")
    private String model;

    @JsonProperty("moderation")
    private JsonNode moderation;

    @JsonProperty("output")
    private List<OutputMessage> output;

    @JsonProperty("parallel_tool_calls")
    private boolean parallelToolCalls;

    @JsonProperty("presence_penalty")
    private double presencePenalty;

    @JsonProperty("previous_response_id")
    private String previousResponseId;

    @JsonProperty("prompt_cache_key")
    private String promptCacheKey;

    @JsonProperty("prompt_cache_retention")
    private String promptCacheRetention;

    @JsonProperty("reasoning")
    private Reasoning reasoningInfo;

    @JsonProperty("safety_identifier")
    private String safetyIdentifier;

    @JsonProperty("service_tier")
    private String serviceTier;

    @JsonProperty("store")
    private boolean store;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("text")
    private TextConfig text;

    @JsonProperty("tool_choice")
    private String toolChoice;

    @JsonProperty("tool_usage")
    private ToolUsage toolUsageInfo;

    @JsonProperty("tools")
    private List<JsonNode> tools;

    @JsonProperty("top_logprobs")
    private int topLogprobs;

    @JsonProperty("top_p")
    private double topP;

    @JsonProperty("truncation")
    private String truncation;

    @JsonProperty("usage")
    private Usage usageInfo;

    @JsonProperty("user")
    private String user;

    @JsonProperty("metadata")
    private Map<String, JsonNode> metadata;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isBackground() {
        return background;
    }

    public void setBackground(boolean background) {
        this.background = background;
    }

    public Billing getBillingInfo() {
        return billingInfo;
    }

    public void setBillingInfo(Billing billingInfo) {
        this.billingInfo = billingInfo;
    }

    public long getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public JsonNode getError() {
        return error;
    }

    public void setError(JsonNode error) {
        this.error = error;
    }

    public double getFrequencyPenalty() {
        return frequencyPenalty;
    }

    public void setFrequencyPenalty(double frequencyPenalty) {
        this.frequencyPenalty = frequencyPenalty;
    }

    public JsonNode getIncompleteDetails() {
        return incompleteDetails;
    }

    public void setIncompleteDetails(JsonNode incompleteDetails) {
        this.incompleteDetails = incompleteDetails;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getMaxOutputTokens() {
        return maxOutputTokens;
    }

    public void setMaxOutputTokens(Integer maxOutputTokens) {
        this.maxOutputTokens = maxOutputTokens;
    }

    public Integer getMaxToolCalls() {
        return maxToolCalls;
    }

    public void setMaxToolCalls(Integer maxToolCalls) {
        this.maxToolCalls = maxToolCalls;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public JsonNode getModeration() {
        return moderation;
    }

    public void setModeration(JsonNode moderation) {
        this.moderation = moderation;
    }

    public List<OutputMessage> getOutput() {
        return output;
    }

    public void setOutput(List<OutputMessage> output) {
        this.output = output;
    }

    public boolean isParallelToolCalls() {
        return parallelToolCalls;
    }

    public void setParallelToolCalls(boolean parallelToolCalls) {
        this.parallelToolCalls = parallelToolCalls;
    }

    public double getPresencePenalty() {
        return presencePenalty;
    }

    public void setPresencePenalty(double presencePenalty) {
        this.presencePenalty = presencePenalty;
    }

    public String getPreviousResponseId() {
        return previousResponseId;
    }

    public void setPreviousResponseId(String previousResponseId) {
        this.previousResponseId = previousResponseId;
    }

    public String getPromptCacheKey() {
        return promptCacheKey;
    }

    public void setPromptCacheKey(String promptCacheKey) {
        this.promptCacheKey = promptCacheKey;
    }

    public String getPromptCacheRetention() {
        return promptCacheRetention;
    }

    public void setPromptCacheRetention(String promptCacheRetention) {
        this.promptCacheRetention = promptCacheRetention;
    }

    public Reasoning getReasoningInfo() {
        return reasoningInfo;
    }

    public void setReasoningInfo(Reasoning reasoningInfo) {
        this.reasoningInfo = reasoningInfo;
    }

    public String getSafetyIdentifier() {
        return safetyIdentifier;
    }

    public void setSafetyIdentifier(String safetyIdentifier) {
        this.safetyIdentifier = safetyIdentifier;
    }

    public String getServiceTier() {
        return serviceTier;
    }

    public void setServiceTier(String serviceTier) {
        this.serviceTier = serviceTier;
    }

    public boolean isStore() {
        return store;
    }

    public void setStore(boolean store) {
        this.store = store;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public TextConfig getText() {
        return text;
    }

    public void setText(TextConfig text) {
        this.text = text;
    }

    public String getToolChoice() {
        return toolChoice;
    }

    public void setToolChoice(String toolChoice) {
        this.toolChoice = toolChoice;
    }

    public ToolUsage getToolUsageInfo() {
        return toolUsageInfo;
    }

    public void setToolUsageInfo(ToolUsage toolUsageInfo) {
        this.toolUsageInfo = toolUsageInfo;
    }

    public List<JsonNode> getTools() {
        return tools;
    }

    public void setTools(List<JsonNode> tools) {
        this.tools = tools;
    }

    public int getTopLogprobs() {
        return topLogprobs;
    }

    public void setTopLogprobs(int topLogprobs) {
        this.topLogprobs = topLogprobs;
    }

    public double getTopP() {
        return topP;
    }

    public void setTopP(double topP) {
        this.topP = topP;
    }

    public String getTruncation() {
        return truncation;
    }

    public void setTruncation(String truncation) {
        this.truncation = truncation;
    }

    public Usage getUsageInfo() {
        return usageInfo;
    }

    public void setUsageInfo(Usage usageInfo) {
        this.usageInfo = usageInfo;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Map<String, JsonNode> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, JsonNode> metadata) {
        this.metadata = metadata;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Billing {

        @JsonProperty("payer")
        private String payer;

        public String getPayer() {
            return payer;
        }

        public void setPayer(String payer) {
            this.payer = payer;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutputMessage {

        @JsonProperty("id")
        private String id;

        @JsonProperty("type")
        private String type;

        @JsonProperty("status")
        private String status;

        @JsonProperty("content")
        private List<ContentItem> content;

        @JsonProperty("phase")
        private String phase;

        @JsonProperty("role")
        private String role;

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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public List<ContentItem> getContent() {
            return content;
        }

        public void setContent(List<ContentItem> content) {
            this.content = content;
        }

        public String getPhase() {
            return phase;
        }

        public void setPhase(String phase) {
            this.phase = phase;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContentItem {

        @JsonProperty("type")
        private String type;

        @JsonProperty("annotations")
        private List<JsonNode> annotations;

        @JsonProperty("logprobs")
        private List<JsonNode> logprobs;

        @JsonProperty("text")
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<JsonNode> getAnnotations() {
            return annotations;
        }

        public void setAnnotations(List<JsonNode> annotations) {
            this.annotations = annotations;
        }

        public List<JsonNode> getLogprobs() {
            return logprobs;
        }

        public void setLogprobs(List<JsonNode> logprobs) {
            this.logprobs = logprobs;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Reasoning {

        @JsonProperty("context")
        private String context;

        @JsonProperty("effort")
        private String effort;

        @JsonProperty("mode")
        private String mode;

        @JsonProperty("summary")
        private String summary;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getEffort() {
            return effort;
        }

        public void setEffort(String effort) {
            this.effort = effort;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextConfig {

        @JsonProperty("format")
        private TextFormat format;

        @JsonProperty("verbosity")
        private String verbosity;

        public TextFormat getFormat() {
            return format;
        }

        public void setFormat(TextFormat format) {
            this.format = format;
        }

        public String getVerbosity() {
            return verbosity;
        }

        public void setVerbosity(String verbosity) {
            this.verbosity = verbosity;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TextFormat {

        @JsonProperty("type")
        private String type;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ToolUsage {

        @JsonProperty("image_gen")
        private ImageGenUsage imageGen;

        @JsonProperty("web_search")
        private WebSearchUsage webSearch;

        public ImageGenUsage getImageGen() {
            return imageGen;
        }

        public void setImageGen(ImageGenUsage imageGen) {
            this.imageGen = imageGen;
        }

        public WebSearchUsage getWebSearch() {
            return webSearch;
        }

        public void setWebSearch(WebSearchUsage webSearch) {
            this.webSearch = webSearch;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ImageGenUsage {

        @JsonProperty("input_tokens")
        private int inputTokens;

        @JsonProperty("input_tokens_details")
        private TokenDetails inputTokensDetails;

        @JsonProperty("output_tokens")
        private int outputTokens;

        @JsonProperty("output_tokens_details")
        private TokenDetails outputTokensDetails;

        @JsonProperty("total_tokens")
        private int totalTokens;

        public int getInputTokens() {
            return inputTokens;
        }

        public void setInputTokens(int inputTokens) {
            this.inputTokens = inputTokens;
        }

        public TokenDetails getInputTokensDetails() {
            return inputTokensDetails;
        }

        public void setInputTokensDetails(TokenDetails inputTokensDetails) {
            this.inputTokensDetails = inputTokensDetails;
        }

        public int getOutputTokens() {
            return outputTokens;
        }

        public void setOutputTokens(int outputTokens) {
            this.outputTokens = outputTokens;
        }

        public TokenDetails getOutputTokensDetails() {
            return outputTokensDetails;
        }

        public void setOutputTokensDetails(TokenDetails outputTokensDetails) {
            this.outputTokensDetails = outputTokensDetails;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TokenDetails {

        @JsonProperty("image_tokens")
        private int imageTokens;

        @JsonProperty("text_tokens")
        private int textTokens;

        public int getImageTokens() {
            return imageTokens;
        }

        public void setImageTokens(int imageTokens) {
            this.imageTokens = imageTokens;
        }

        public int getTextTokens() {
            return textTokens;
        }

        public void setTextTokens(int textTokens) {
            this.textTokens = textTokens;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class WebSearchUsage {

        @JsonProperty("num_requests")
        private int numRequests;

        public int getNumRequests() {
            return numRequests;
        }

        public void setNumRequests(int numRequests) {
            this.numRequests = numRequests;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Usage {

        @JsonProperty("input_tokens")
        private int inputTokens;

        @JsonProperty("input_tokens_details")
        private InputTokenDetails inputTokensDetails;

        @JsonProperty("output_tokens")
        private int outputTokens;

        @JsonProperty("output_tokens_details")
        private OutputTokenDetails outputTokensDetails;

        @JsonProperty("total_tokens")
        private int totalTokens;

        public int getInputTokens() {
            return inputTokens;
        }

        public void setInputTokens(int inputTokens) {
            this.inputTokens = inputTokens;
        }

        public InputTokenDetails getInputTokensDetails() {
            return inputTokensDetails;
        }

        public void setInputTokensDetails(InputTokenDetails inputTokensDetails) {
            this.inputTokensDetails = inputTokensDetails;
        }

        public int getOutputTokens() {
            return outputTokens;
        }

        public void setOutputTokens(int outputTokens) {
            this.outputTokens = outputTokens;
        }

        public OutputTokenDetails getOutputTokensDetails() {
            return outputTokensDetails;
        }

        public void setOutputTokensDetails(OutputTokenDetails outputTokensDetails) {
            this.outputTokensDetails = outputTokensDetails;
        }

        public int getTotalTokens() {
            return totalTokens;
        }

        public void setTotalTokens(int totalTokens) {
            this.totalTokens = totalTokens;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InputTokenDetails {

        @JsonProperty("cached_tokens")
        private int cachedTokens;

        public int getCachedTokens() {
            return cachedTokens;
        }

        public void setCachedTokens(int cachedTokens) {
            this.cachedTokens = cachedTokens;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OutputTokenDetails {

        @JsonProperty("reasoning_tokens")
        private int reasoningTokens;

        public int getReasoningTokens() {
            return reasoningTokens;
        }

        public void setReasoningTokens(int reasoningTokens) {
            this.reasoningTokens = reasoningTokens;
        }
    }
}
