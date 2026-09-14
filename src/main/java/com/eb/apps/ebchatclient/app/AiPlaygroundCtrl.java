package com.eb.apps.ebchatclient.app;

import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmRequestBuilder;
import com.eb.base.ai_service.llm_client.api.LlmRequestService;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import com.eb.base.ai_service.llm_client.infrastructure.LlmModel;
import com.eb.base.ai_service.llm_client.infrastructure.LlmModelProvider;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.gui.ICF;
import com.eb.base.gui.persist.ComponentPersisterFactory;
import com.eb.base.gui.persist.IComponentPersister;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.apps.ebchatclient.components.JsonFileAppendUtil;
import com.eb.apps.ebchatclient.components.fileprovider.GuiFileNameProvider;
import com.eb.apps.ebchatclient.domain.chat.AiChat;
import com.eb.apps.ebchatclient.domain.chat.AiChatContext;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Objects;

public class AiPlaygroundCtrl {

    private JComboBox<AiChatContext> cbContexts;
    private JComboBox<LlmModel> cbModelle;
    private JComboBox<AiChat> cbChats;
    AiPlaygroundWindow window;
    private JProgressBar progressBar;
    private GuiDecorator decorator;

    AiPlaygroundCtrl() {

        IniFile iniFile = IniFileProvider.createIniFile("JavaAiPlaygroundCtrl.ini");
        window = new AiPlaygroundWindow(iniFile);
        window.setVisible(true);
        decorateToolbarInput();
        
        IComponentPersister persister = ComponentPersisterFactory.createIniFilePersister(iniFile);
        persister.addComponentItem(cbChats,"CbChats");
        persister.addComponentItem(cbContexts,"CbContexts");
        persister.addComponentItem(cbModelle,"CbModelle");

        window.registerPersister(persister);


        persister.loadAndSetComponentItems();


        decorator.addCloseAction(persister::persistComponentItems);

    }

    private void decorateToolbarInput() {

        String tbName = window.getPanelWithToolBarInput().getToolbar().getName();
        decorator = window.getDecorator();

        decorator.addToolbarButton(tbName,"Run", IC.PLAY, (s) -> sendRequest());
        decorator.addToolbarButton(tbName,"Run", IC.MB_PLAY, this::actionPerformed2);
        decorator.addToolbarButton(tbName,"Open append file window", ICF.BulletList, this::actionPerformed);
        decorator.addToolbarButton(tbName, "Dummy", ICF.LinePoints_Add, (s)->{});
        decorator.addToolbarButton(tbName, "Dummy", ICF.MaleHardHat_Lock, (s)->{});

        AiChatManager manager = AiChatManager.getCurrent();

        LlmModelProvider modelProvider = new LlmModelProvider();
        cbChats = decorator.addToolbarComboBox( tbName,"Chats", manager.getAvailableChats(), this::setChat);
        int height = cbChats.getHeight();
        cbContexts = decorator.addToolbarComboBox( tbName,"Kontext", manager.getAvailableContexts(), this::setContext);
        cbModelle = decorator.addToolbarComboBox( tbName,"Modell", modelProvider.getModels(), this::setModel);

        cbChats.setPreferredSize(new Dimension(20, height));
        cbModelle.setPreferredSize(new Dimension(20, height));
        cbContexts.setPreferredSize(new Dimension(20, height));

        progressBar = decorator.addToolbarProgressBar(tbName,"Huhu");
    }

    private void openAppendFileWindow() {
        GuiFileNameProvider provider = new GuiFileNameProvider();
        provider.setVisible(true);
        provider.addListConsumer(this::appendFileNames);
    }

    private void appendFileNames(List<String> l) {
        StringBuilder sb = new StringBuilder();
        sb.append(window.getInputString());
        sb.append("\n");
        for(String s : l) {
            sb.append("<$ %s\n".formatted(s));
        }
        window.setInputText(sb.toString());
    }

    private void setModel(LlmModel s) {

    }

    private void setChat(AiChat s) {

    }

    private void setContext(AiChatContext s) {

        window.setInputText(s.getRequestMessage());
    }

    private void sendRequest() {
        LlmRequestBuilder builder = new LlmRequestBuilder();

        String inputString = window.getInputString();
        if (inputString.trim().isEmpty()) {
            inputString = """
                        <<Du bist ein CSharp Programmierer unter DotNet 9 mit CSharp 10.>>
                        Schreibe einen HttpClient für Ollama.
                        """;
        }

        String inputStringWithFiles = JsonFileAppendUtil.appendFiles(inputString);
        if (!inputStringWithFiles.equals(inputString))
        {
            window.setInputText(inputStringWithFiles);
            return;
        }

        LlmRequest llmRequest =
                builder
                        .addRequestMsg(inputString)
                        .setModel(((LlmModel) Objects.requireNonNull(cbModelle.getSelectedItem())).getModelName())
                        .build();

        sendRequestAndHandleResponseWithNewTaskAndProgressBarAnimation(llmRequest);
    }

    private void sendRequestAndHandleResponseWithNewTaskAndProgressBarAnimation(LlmRequest llmRequest) {

        Thread task = new Thread(() -> withProgressbarAnimationDo(()->{
            LlmResponse result = LlmRequestService.sendRequest(llmRequest);
            window.setOutputText(result.getAnswer());
        }));
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

    private void actionPerformed(ActionEvent s) {
        openAppendFileWindow();
    }

    private void actionPerformed2(ActionEvent s) {
        progressBar.setIndeterminate(false);
    }
}
