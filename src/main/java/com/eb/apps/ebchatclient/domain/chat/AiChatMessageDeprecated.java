package com.eb.apps.ebchatclient.domain.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.OffsetDateTime;

public class AiChatMessageDeprecated {

    @JsonProperty("SystemQuestion")
    private String systemQuestion;

    @JsonProperty("UserQuestion")
    private String userQuestion;

    @JsonProperty("Result")
    private String result;

    @JsonProperty("Model")
    private String model;

    @JsonProperty("Start")
    private OffsetDateTime start;

    @JsonProperty("Stop")
    private OffsetDateTime stop;

    @JsonProperty("StoredAnswer")
    private String storedAnswer;

    @JsonProperty("UsageInfo")
    private Usage usageInfo;

    public String getSystemQuestion() { return systemQuestion; }
    public void setSystemQuestion(String systemQuestion) { this.systemQuestion = systemQuestion; }

    public String getUserQuestion() { return userQuestion; }
    public void setUserQuestion(String userQuestion) { this.userQuestion = userQuestion; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public OffsetDateTime getStart() { return start; }
    public void setStart(OffsetDateTime start) { this.start = start; }

    public OffsetDateTime getStop() { return stop; }
    public void setStop(OffsetDateTime stop) { this.stop = stop; }

    public String getStoredAnswer() { return storedAnswer; }
    public void setStoredAnswer(String storedAnswer) { this.storedAnswer = storedAnswer; }

    public Usage getUsageInfo() { return usageInfo; }
    public void setUsageInfo(Usage usageInfo) { this.usageInfo = usageInfo; }

    public static class Usage {
        @JsonProperty("input_tokens")
        private int inputTokens;

        @JsonProperty("input_tokens_details")
        private TokenDetails inputTokensDetails;

        @JsonProperty("output_tokens")
        private int outputTokens;

        @JsonProperty("output_tokens_details")
        private OutputTokenDetails outputTokensDetails;

        @JsonProperty("total_tokens")
        private int totalTokens;

        public int getInputTokens() { return inputTokens; }
        public void setInputTokens(int inputTokens) { this.inputTokens = inputTokens; }

        public TokenDetails getInputTokensDetails() { return inputTokensDetails; }
        public void setInputTokensDetails(TokenDetails inputTokensDetails) { this.inputTokensDetails = inputTokensDetails; }

        public int getOutputTokens() { return outputTokens; }
        public void setOutputTokens(int outputTokens) { this.outputTokens = outputTokens; }

        public OutputTokenDetails getOutputTokensDetails() { return outputTokensDetails; }
        public void setOutputTokensDetails(OutputTokenDetails outputTokensDetails) { this.outputTokensDetails = outputTokensDetails; }

        public int getTotalTokens() { return totalTokens; }
        public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }

        public static class TokenDetails {
            @JsonProperty("CachedTokens")
            private int cachedTokens;

            public int getCachedTokens() { return cachedTokens; }
            public void setCachedTokens(int cachedTokens) { this.cachedTokens = cachedTokens; }
        }

        public static class OutputTokenDetails {
            @JsonProperty("ReasoningTokens")
            private int reasoningTokens;

            public int getReasoningTokens() { return reasoningTokens; }
            public void setReasoningTokens(int reasoningTokens) { this.reasoningTokens = reasoningTokens; }
        }
    }
}
