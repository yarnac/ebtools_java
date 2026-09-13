package com.eb.chatclient.domain;

import java.io.IOException;

public interface ILlmClient {
    LlmResponseDeprecated sendRequest(LlmRequestDeprecated request) throws IOException, InterruptedException;
}
