package com.eb.apps.ebchatclient.domain;

import java.io.IOException;

public interface ILlmClient {
    LlmResponseDeprecated sendRequest(LlmRequestDeprecated request) throws IOException, InterruptedException;
}
