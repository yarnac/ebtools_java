package com.eb.base.ai_service.llm_client.api;

import com.eb.apps.ebchat_client.app.AiPlaygroundCtrl;
import com.eb.base.extensions.StringExtensions;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonPropertyOrder({ "contentMemo", "model", "parameters", "messages" }) // Reihenfolge festlegen
@Getter
@Setter
public class LlmRequest {
    @JsonProperty("model")
    private String model;
    List<LlmMessage> messages;
    List<Parameter> parameters;

    String contentMemo;

    @Override
    public String toString() {
        if (StringExtensions.ebIsNilOrEmpty(contentMemo)) {
            return getMessages().getFirst().getMessageString();
        }
        return contentMemo;
    }

    public LlmRequest() {}

    @JsonIgnore()
    private LlmResponse lastResponse;

    @JsonIgnore()
    private Long requestId;


    public LlmRequest(String model, List<LlmMessage> messages) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(messages);
        this.model = model;
        this.messages = messages;
        this.parameters = new ArrayList<>();
    }

    public LlmRequest(String model, List<LlmMessage> messages, List<Parameter> parameters) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(messages);
        Objects.requireNonNull(parameters);
        this.model = model;
        this.messages = messages;
        this.parameters = parameters;
    }

    public  static LlmRequestBuilderSystemOrUserMsg builder()
    {
        return LlmRequestBuilder.create();
    }

    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }

    @JsonProperty
    public Parameter getParameter(String name) {
        for (Parameter parameter : parameters) {
            if (parameter.getName().equals(name)) {
                return parameter;
            }
        }
        return null;
    }

    public boolean hasParameter(String maxTokens) {
        return getParameter(maxTokens) != null;
    }

    public void addUserMsg(String inputStringWithFiles) {
        LlmMessage msg = new LlmMessage("user", inputStringWithFiles);
        getMessages().add(msg);
    }

    public void addResponse(LlmResponse response) {
        lastResponse = response;
        LlmMessage message = new LlmMessage("assistant", response.getAnswer());
        message.setMessageInfo(response.getMessageInfo());
        getMessages().add(message);
    }

    @JsonIgnore
    public String getAllMessageString() {
        StringBuilder sb = new StringBuilder();
        for(LlmMessage message : getMessages()) {
            sb.append(message.getMessageString()).append("\n");
        }
        return sb.toString();
    }


    public class LastLlmMessageInfo
    {
        public String system;
        public String user;
        public String assistant;

        public LastLlmMessageInfo() {
            system = "";
            user = "";
            assistant = "";
        }
    }

    public LastLlmMessageInfo getLastLlmMessageInfo() {

        LastLlmMessageInfo lastLlmMessageInfo = new LastLlmMessageInfo();
        int messageCount = getMessages().size();
        if (messageCount == 0) {
            return lastLlmMessageInfo;
        }

        LlmMessage llmMessage = getMessages().get(0);
        if (llmMessage.isSystem())
            lastLlmMessageInfo.system = llmMessage.getMessageString();

        int i=messageCount-1;
        llmMessage = getMessages().get(i);
        if (llmMessage.isAssistent()) {
            lastLlmMessageInfo.assistant = llmMessage.getMessageString();
            i--;
            if (i>=0 ) {
                llmMessage = getMessages().get(i);
                if (llmMessage.isUser()) {
                    lastLlmMessageInfo.user = getMessages().get(i).getMessageString();
                }
            }
        }
        else if (llmMessage.isUser())
            lastLlmMessageInfo.user = llmMessage.getMessageString();

        return lastLlmMessageInfo;
    }
}
