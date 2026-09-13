package com.eb.chatclient.app;

import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmRequestBuilder;
import com.eb.ai_service.llm_client.api.LlmResponse;
import com.eb.ai_service.llm_client.infrastructure.LlmModel;
import com.eb.ai_service.llm_client.infrastructure.LlmModelProvider;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.gui.ICF;
import com.eb.chatclient.components.fileprovider.GuiFileNameProvider;
import com.eb.chatclient.domain.chat.AiChat;
import com.eb.chatclient.domain.chat.AiChatContext;
import com.eb.chatclient.domain.chat.AiChatManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AiPlaygroundCtrl {

    private JComboBox<AiChatContext> cbContexts;
    private JComboBox<LlmModel> cbModelle;
    private JComboBox<AiChat> cbChats;
    AiPlaygroundWindow window;
    private JProgressBar progressBar;
    private boolean ignoreChanges;

    AiPlaygroundCtrl() {
        window = new AiPlaygroundWindow();
        window.setVisible(true);

        decorateToolbarInput();
    }

    private void decorateToolbarInput() {

        String tbName = window.getPanelWithToolBarInput().getToolbar().getName();
        GuiDecorator decorator = window.getDecorator();

        decorator.addToolbarButton(tbName,"Run", IC.PLAY, (s) -> sendRequest());
        decorator.addToolbarButton(tbName,"Run", IC.MB_PLAY, (s) -> {progressBar.setIndeterminate(false);});
        decorator.addToolbarButton(tbName,"Open append file window", ICF.BulletList, (s) -> openAppendFileWindow());
        decorator.addToolbarButton(tbName, "Dummy", ICF.LinePoints_Add, (s)->{});
        decorator.addToolbarButton(tbName, "Dummy", ICF.MaleHardHat_Lock, (s)->{});

        AiChatManager manager = AiChatManager.getCurrent();

        LlmModelProvider modelProvider = new LlmModelProvider();
        cbChats = decorator.addToolbarComboBox( tbName,"Chats", manager.getAvailableChats(), (s) -> setChat(s));
        int height = cbChats.getHeight();
        cbContexts = decorator.addToolbarComboBox( tbName,"Kontext", manager.getAvailableContexts(), (s) -> setContext(s));
        cbModelle = decorator.addToolbarComboBox( tbName,"Modell", modelProvider.getModels(), (s) -> setModel(s));

        cbChats.setPreferredSize(new Dimension(20, height));
        cbModelle.setPreferredSize(new Dimension(20, height));
        cbContexts.setPreferredSize(new Dimension(20, height));

        progressBar = decorator.addToolbarProgressBar(tbName,"Huhu");
    }

    private void openAppendFileWindow() {
        GuiFileNameProvider provider = new GuiFileNameProvider();
        provider.setVisible(true);
        provider.addListConsumer(l -> appendFileNames(l));
    }

    private void appendFileNames(List<String> l) {
        StringBuilder sb = new StringBuilder();
        sb.append(window.getInputString() + "\n");
        for(String s : l) {
            sb.append("<$ " + s + "\n");
        }
        window.setInputText(sb.toString());
    }

    private void setModel(LlmModel s) {
        if (ignoreChanges)
            return;
    }

    private void setChat(AiChat s) {
        if (ignoreChanges)
            return;
    }

    private void setContext(AiChatContext s) {
        if (ignoreChanges)
            return;
        window.setInputText(s.getRequestMessage());
    }

    private void sendRequest() {
        LlmRequestBuilder builder = new LlmRequestBuilder();

        String inputString = window.getInputString();
        if (inputString.trim().length() == 0) {
            inputString = """
                        <<Du bist ein CSharp Programmierer unter DotNet 9 mit CSharp 10.>>
                        Schreibe einen HttpClient für Ollama.
                        """;
        }

        String inputStringWithFiles = JsonFileAppenUtil.appendFiles(inputString);
        if (!inputStringWithFiles.equals(inputString))
        {
            window.setInputText(inputStringWithFiles);
            return;
        }

        LlmRequest llmRequest =
                builder
                        .addRequestMsg(inputString)
                        .setModel(((LlmModel) cbModelle.getSelectedItem()).getModelName())
                        .build();

        sendRequestAndHandleResponseWithNewTaskAndProgressBarAnimation(llmRequest);
    }

    private void sendRequestAndHandleResponseWithNewTaskAndProgressBarAnimation(LlmRequest llmRequest) {

        Thread task = new Thread(() -> {
            withProgressbarAnimationDo(()->{
                LlmResponse result = LlmRequestService.sendRequest(llmRequest);
                window.setOutputText(result.getAnswer());
            });
        });
        task.start();
    }


    private void withProgressbarAnimationDo(Runnable runnable) {
        progressBar.setIndeterminate(true);
        window.setInOutEnabled(false);
        window.getTextPaneOutput().setText("Waiting for request answer");
        runnable.run();
        progressBar.setIndeterminate(false);
        window.setInOutEnabled(true);
        System.out.println("\nFertig!");
    }

    private void startRequest() {
    }

}
