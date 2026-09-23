package com.eb.apps.ebchatclient.domain.chat;

import com.eb.apps.ebookreader.tobj.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter

public class AiChatContext {
    private String name;
    private String knoten;
    private String userString;
    private String systemPrompt;
    private String userPrompt;
    private List<String> fileNames = new ArrayList<>();

    public AiChatContext() {
    }

    public AiChatContext(String name) {
        this.name = name;
    }

    public AiChatContext(String name, String knoten, String systemPrompt, String userPrompt) {
        this.name = name;
        this.knoten = knoten;
        this.systemPrompt = systemPrompt;
        this.userPrompt = userPrompt;
    }

    public void prepareStore()
    {
        if (userString!=null)
            return;

        String systemPart = StringUtil.isNullOrEmpty(systemPrompt)
                ? ""
                : "<<" + systemPrompt.trim() + ">>\n";

        String userPart = StringUtil.isNullOrEmpty(userPrompt)
                ? ""
                : userPrompt;

        userString = systemPart + userPart;
    }


    public String getRequestMessage() {
        if (userString==null)
            prepareStore();

        return userString;
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
