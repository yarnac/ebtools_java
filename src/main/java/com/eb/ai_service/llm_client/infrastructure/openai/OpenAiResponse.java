package com.eb.ai_service.llm_client.infrastructure.openai;

import java.util.List;
import java.util.Map;

public class OpenAiResponse {

    private String id;
    private String object;
    private long created_at;
    private String status;
    private boolean background;
    private Billing billing;
    private Long completed_at;
    private String model;
    private List<Output> output;
    private boolean parallel_tool_calls;
    private double temperature;
    private Reasoning reasoning;
    private Usage usage;
    private Text text;
    private Map<String, Object> metadata;

    // Getter & Setter

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

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
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

    public Billing getBilling() {
        return billing;
    }

    public void setBilling(Billing billing) {
        this.billing = billing;
    }

    public Long getCompleted_at() {
        return completed_at;
    }

    public void setCompleted_at(Long completed_at) {
        this.completed_at = completed_at;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Output> getOutput() {
        return output;
    }

    public void setOutput(List<Output> output) {
        this.output = output;
    }

    public boolean isParallel_tool_calls() {
        return parallel_tool_calls;
    }

    public void setParallel_tool_calls(boolean parallel_tool_calls) {
        this.parallel_tool_calls = parallel_tool_calls;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public Reasoning getReasoning() {
        return reasoning;
    }

    public void setReasoning(Reasoning reasoning) {
        this.reasoning = reasoning;
    }

    public Usage getUsage() {
        return usage;
    }

    public void setUsage(Usage usage) {
        this.usage = usage;
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }


    public static class Billing {
        private String payer;

        public String getPayer() {
            return payer;
        }

        public void setPayer(String payer) {
            this.payer = payer;
        }
    }


    public static class Output {
        private String id;
        private String type;
        private String status;
        private String phase;
        private String role;
        private List<Content> content;

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

        public List<Content> getContent() {
            return content;
        }

        public void setContent(List<Content> content) {
            this.content = content;
        }
    }


    public static class Content {
        private String type;
        private List<Object> annotations;
        private List<Object> logprobs;
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<Object> getAnnotations() {
            return annotations;
        }

        public void setAnnotations(List<Object> annotations) {
            this.annotations = annotations;
        }

        public List<Object> getLogprobs() {
            return logprobs;
        }

        public void setLogprobs(List<Object> logprobs) {
            this.logprobs = logprobs;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }


    public static class Reasoning {
        private String context;
        private String effort;
        private String mode;
        private Object summary;

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

        public Object getSummary() {
            return summary;
        }

        public void setSummary(Object summary) {
            this.summary = summary;
        }
    }


    public static class Usage {
        private int input_tokens;
        private int output_tokens;
        private int total_tokens;
        private Map<String, Object> input_tokens_details;
        private Map<String, Object> output_tokens_details;

        public int getInput_tokens() {
            return input_tokens;
        }

        public void setInput_tokens(int input_tokens) {
            this.input_tokens = input_tokens;
        }

        public int getOutput_tokens() {
            return output_tokens;
        }

        public void setOutput_tokens(int output_tokens) {
            this.output_tokens = output_tokens;
        }

        public int getTotal_tokens() {
            return total_tokens;
        }

        public void setTotal_tokens(int total_tokens) {
            this.total_tokens = total_tokens;
        }

        public Map<String, Object> getInput_tokens_details() {
            return input_tokens_details;
        }

        public void setInput_tokens_details(Map<String, Object> input_tokens_details) {
            this.input_tokens_details = input_tokens_details;
        }

        public Map<String, Object> getOutput_tokens_details() {
            return output_tokens_details;
        }

        public void setOutput_tokens_details(Map<String, Object> output_tokens_details) {
            this.output_tokens_details = output_tokens_details;
        }
    }


    public static class Text {
        private Format format;

        public Format getFormat() {
            return format;
        }

        public void setFormat(Format format) {
            this.format = format;
        }

        public static class Format {
            private String type;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}