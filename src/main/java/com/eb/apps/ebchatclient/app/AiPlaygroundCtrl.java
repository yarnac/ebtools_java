package com.eb.apps.ebchatclient.app;

import com.eb.apps.ebchatclient.codegen.AiCodeGeneratorCtrl;
import com.eb.apps.ebchatclient.domain.chat.GlobaleEinstellungen;
import com.eb.apps.ebchatclient.domain.context.app.ContextEditDlg;
import com.eb.apps.ebchatclient.domain.context.domain.ContextManager;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llm_client.api.LlmRequestBuilder;
import com.eb.base.ai_service.llm_client.api.LlmRequestService;
import com.eb.base.ai_service.llm_client.api.LlmResponse;
import com.eb.base.ai_service.llm_client.infrastructure.LlmModel;
import com.eb.base.ai_service.llm_client.infrastructure.LlmModelProvider;
import com.eb.base.ai_service.llmreqstore.LlmRequestManager;
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
import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;
import com.eb.apps.ebchatclient.domain.chat.AiChatManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AiPlaygroundCtrl {

    private JComboBox<ContextWithFiles> cbContexts;
    private JComboBox<LlmModel> cbModelle;
    private JComboBox<AiChat> cbChats;
    AiPlaygroundWindow window;
    private JProgressBar progressBar;
    private GuiDecorator decorator;
    private ContextEditDlg contextEditDlg;
    private LlmRequest actLlmRequest;
    private LlmRequestManager llmRequestManager;

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);

    AiPlaygroundCtrl() {


        try {
            if (!GlobaleEinstellungen.isWindows()) {
                System.out.println(UIManager.getLookAndFeel());
                System.out.println(UIManager.getLookAndFeel().getClass().getName());
                UIManager.setLookAndFeel(
                        UIManager.getCrossPlatformLookAndFeelClassName()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        IniFile iniFile = IniFileProvider.createIniFile("JavaAiPlaygroundCtrl.ini");
        window = new AiPlaygroundWindow(iniFile);
        window.setVisible(true);
        decorateToolbarInput();

        String tbOutputName = window.getPanelWithToolBarOutput().getToolbar().getName();
        decorator.addToolbarButton(tbOutputName,"Generate Code", ICF.ArrowFlowVertical_Add, this::handleGenerateCode);

        
        IComponentPersister persister = ComponentPersisterFactory.createIniFilePersister(iniFile);
        persister.addComponentItem(cbChats,"CbChats");
        persister.addComponentItem(cbContexts,"CbContexts");
        persister.addComponentItem(cbModelle,"CbModelle");
        window.registerPersister(persister);
        persister.loadAndSetComponentItems();
        decorator.addCloseAction(persister::persistComponentItems);

        llmRequestManager = new LlmRequestManager();


        // ✅ Empfehlung der AI qwen3.5:9
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
        }));

    }

    private void handleGenerateCode(ActionEvent actionEvent) {
        AiCodeGeneratorCtrl generator = new AiCodeGeneratorCtrl();
        generator.setCodeText(window.getTextPaneOutput().getText());
    }

    private void decorateToolbarInput() {

        String tbName = window.getPanelWithToolBarInput().getToolbar().getName();
        decorator = window.getDecorator();

        decorator.setCurrentMenu("Datei");
        decorator.addMenuItem("RUN",()->sendRequest(false), KeyEvent.VK_F5,0);
        decorator.addMenuItem("Speichern",()->speichereRequest(), KeyEvent.VK_S,7);
        decorator.addOpenFileToolbarButton(tbName, "OpenAI Kosten", ICF.Monitor_Info, "https://platform.openai.com/home");
        decorator.addOpenFileToolbarButton(tbName, "Anthropic Kosten", ICF.Monitor_Properties, "https://platform.claude.com/dashboard");

        decorator.addToolbarButton(tbName,"Run", IC.PLAY, (s) -> sendRequest(false));
        decorator.addToolbarButton(tbName,"Run again", IC.MB_PLAY,  (s) -> sendRequest(true));
        decorator.addToolbarButton(tbName,"Open append file window", ICF.BulletList, this::actionPerformed);
        decorator.addToolbarButton(tbName, "Kontexte", ICF.LinePoints_Add, this::openContextEditor);
        decorator.addToolbarButton(tbName, "Dummy", ICF.MaleHardHat_Lock, (s)->{});

        AiChatManager chatManager = AiChatManager.getCurrent();
        ContextManager contextManager = chatManager.getContextManager();

        LlmModelProvider modelProvider = new LlmModelProvider();
        cbChats = decorator.addToolbarComboBox( tbName,"Chats", chatManager.getAvailableChats(), this::setChat);
        int height = cbChats.getHeight();
        cbContexts = decorator.addToolbarComboBox( tbName,"Kontext", contextManager.getContextList(), this::setContext);
        cbModelle = decorator.addToolbarComboBox( tbName,"Modell", modelProvider.getModels(), this::setModel);

        cbChats.setPreferredSize(new Dimension(20, height));
        cbModelle.setPreferredSize(new Dimension(20, height));
        cbContexts.setPreferredSize(new Dimension(20, height));

        progressBar = decorator.addToolbarProgressBar(tbName,"Huhu");
    }

    private void speichereRequest() {
        if (actLlmRequest == null) {
            return;
        }

        if (llmRequestManager==null) {
            llmRequestManager = new LlmRequestManager();
        }
        llmRequestManager.store(actLlmRequest);
    }

    private void openContextEditor(ActionEvent actionEvent) {

        contextEditDlg = WindowUtility.showOrCreateWindow(contextEditDlg, ContextEditDlg::ShowWindow);
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

    private void setContext(ContextWithFiles s) {

        window.setInputText(s.getUserString());
    }

    private void sendRequest(boolean append) {

        String inputString = window.getInputString();
        if (!validateInput(inputString))
            return;

        List<String> imageFileNames = new ArrayList<>();

        if (inputString.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Kein Prompt", "Fehler", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String inputStringWithDirectoriesAndFiles = JsonFileAppendUtil.appendDirectoryFiles(inputString);
        String inputStringWithFiles = JsonFileAppendUtil.appendFiles(inputStringWithDirectoriesAndFiles, imageFileNames);

        if (!append || actLlmRequest==null) {
            actLlmRequest = null;

            LlmRequestBuilder builder = new LlmRequestBuilder();
            actLlmRequest =
                    builder
                            .addRequestMsg(inputStringWithFiles, imageFileNames)
                            .setModel(((LlmModel) Objects.requireNonNull(cbModelle.getSelectedItem())).getModelName())
                            .build();
        }
        else
        {
            actLlmRequest.addUserMsg(inputStringWithFiles);
        }


        sendRequestAndHandleResponseWithNewTaskAndProgressBarAnimation(actLlmRequest);
    }

    private boolean validateInput(String inputString) {
        String fehlerMessage = null;
        if (inputString.isEmpty()) {
            fehlerMessage = "Kein Prompt";

        }
        else {
            int startSystem = inputString.indexOf("<<");;
            if (startSystem >= 0) {
                int endSystem = inputString.indexOf(">>");;
                if (endSystem < startSystem) {
                    fehlerMessage = "System Message nicht abgeschlossen. '>>' fehlt!";
                }
            }
        }
        if (fehlerMessage != null) {
            JOptionPane.showMessageDialog(window, fehlerMessage, "Kein gültiger Prompt", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void sendRequestAndHandleResponseWithNewTaskAndProgressBarAnimation(LlmRequest llmRequest) {
        // 1. UI-Status setzen (Wird vom EDT ausgeführt, blockiert also nicht durch die Logik)
        SwingUtilities.invokeLater(() -> {
            // progressBar.setUI(new BasicProgressBarUI());
            progressBar.setIndeterminate(true);
            window.getTextPaneOutput().setText("Waiting for request answer");

            progressBar.revalidate();
            progressBar.repaint();

            // Optional: Auch das Fenster erzwingen zu repainted, falls die Progressbar in einer komplexeren Struktur liegt
            window.revalidate();
            window.repaint();
        });

        // 2. Request im Hintergrund-Thread ausführen (Blockiert nicht die UI)
        executor.execute(() -> {
            try {
                LlmResponse result = LlmRequestService.sendRequest(llmRequest);

                // 3. Erfolg: UI-Ergebnis setzen (Wieder zurück auf den EDT via invokeLater)
                SwingUtilities.invokeLater(() -> {
                    window.setOutputText(result.getAnswerWithDetails());
                    if(window.getTextPaneAdapterOutput() != null) {
                        window.getTextPaneAdapterOutput().setFirstVisibleLine(0);
                    }
                    progressBar.setIndeterminate(false);
                    System.out.println("\nFertig!");
                });

            } catch (Exception e) {
                // 4. Fehler: UI-Fehlermeldung setzen (Wieder zurück auf den EDT via invokeLater)
                SwingUtilities.invokeLater(() -> {
                    window.setOutputText("Error: " + e.getMessage());
                    progressBar.setIndeterminate(false);
                    System.out.println("\nFertig!");
                });
            }
        });
    }




    private void withProgressbarAnimationDo(Runnable runnable) {
        progressBar.setIndeterminate(true);
        // window.setInOutEnabled(false);
        window.getTextPaneOutput().setText("Waiting for request answer");
        runnable.run();
        progressBar.setIndeterminate(false);
        // window.setInOutEnabled(true);
        System.out.println("\nFertig!");
    }

    private void actionPerformed(ActionEvent s) {
        openAppendFileWindow();
    }

    private void actionPerformed2(ActionEvent s) {
        progressBar.setIndeterminate(false);
    }
}
