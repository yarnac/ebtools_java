package com.eb.apps.ebchat_client.app;

import com.eb.apps.ebchat_client.domain.edrequest.EdRequestCtrl;
import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.apps.ebchat_client.domain.context.app.ContextEditDlg;
import com.eb.base.ai_service.llm_client.api.*;
import com.eb.base.ai_service.llm_client.infrastructure.contentmapping.JsonFileAppendUtil;
import com.eb.base.ai_service.llmreqstore.LlmRequestManager;
import com.eb.base.gui.decorator.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.gui.ICF;
import com.eb.base.gui.decorator.GuiDecoratorToolBars;
import com.eb.base.gui.persist.ComponentPersisterFactory;
import com.eb.base.gui.persist.IComponentPersister;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.apps.ebchat_client.domain.chat.AiChatManager;
import com.eb.base.io.FileUtil;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AiPlaygroundCtrl {

    private final LlmRequestManager llmRequestManager;
    private final AiInputOutputPanelCtrl aiInputOutputPanelCtrl;
    private final AiInputOutputPanel aiInputOutputPanel;
    AiPlaygroundWindow window;
    private GuiDecorator decorator;

    private static final ExecutorService executor = Executors.newFixedThreadPool(4);
    private JComboBox<LlmRequest> cbRequests;
    private AiChatManager chatManager;


    AiPlaygroundCtrl() {
        setMyIcon();
        IniFile iniFile = IniFileProvider.createIniFile("JavaAiPlaygroundCtrl.ini");
        JsonFileAppendUtil.addAllowedDirectories(iniFile.getSectionValues("AllowedPaths"));

        window = new AiPlaygroundWindow(iniFile);

        aiInputOutputPanelCtrl = window.getAiInputOutputPanelCtrl();
        aiInputOutputPanel = window.getAiInputOutputPanel();


        decorator = window.getDecorator();
        chatManager = AiChatManager.getCurrent();
        llmRequestManager = LlmRequestManager.current();

        decorateMainToolbar();
        decorateMenuBar();
        decorateToolbarTree();
        
        IComponentPersister persister = ComponentPersisterFactory.createIniFilePersister(iniFile);
        window.registerPersister(persister);
        persister.addComponentItem(aiInputOutputPanelCtrl.getCbChats(),"CbChats");

        persister.addComponentItem(aiInputOutputPanelCtrl.getCbModelle(),"CbModelle");
        persister.addComponentItem(window.getAiInputOutputPanel().getSplitPanelMessages().getSplitPane(),"SplitMessages");
        persister.addComponentItem(window.getAiInputOutputPanel().getSplitPanelInput().getSplitPane(),"SplitInput");

        window.registerPersister(persister);
        decorator.addCloseAction(persister::persistComponentItems);
        persister.loadAndSetComponentItems();

        window.getContextEditorCtrl().setPromptConsumer((String s)-> window.setInput(s));
        window.getContextEditorCtrl().setPromptOutputConsumer((String s)-> window.setInput(s));

        SwingUtilities.invokeLater(() -> {window.setVisible(true);});

        // ✅ Empfehlung der AI qwen3.5:9
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdown();
        }));

    }

    private void setMyIcon() {
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
    }

    private void decorateMainToolbar() {
        GuiDecoratorToolBars toolBarDecorator = decorator.toolBars();
        toolBarDecorator.setCurrentContainer("MainToolbar");

        decorator.addEditIniFileButton(chatManager.getLlmModelProvider().getModelFileName());

        toolBarDecorator.addToolbarButton("Speichern Main", IC.SAVE_ADD, (x)->{});
        toolBarDecorator.addToolbarButton("Edit Requests", ICF.Flowchart, (x)->{new EdRequestCtrl();});
        toolBarDecorator.addToolbarButton("Edit Contexts", ICF.Code, (x)->{SwingUtilities.invokeLater(ContextEditDlg::Show);});
        toolBarDecorator.addToolbarButton("Open DataFiles", ICF.History_Edit, (x)-> {
            FileUtil.open(chatManager.getLlmModelProvider().getModelFileName());
        });
    }

    private void decorateToolbarTree() {
        String tbName = window.getToolbarTreePanelName();
        window.getDecorator().toolBars().setCurrentContainer(tbName);
        window.getDecorator().toolBars().addToolbarButton("Set Request", ICF.TextDocument_Export, x->handleSetRequest((LlmRequest) cbRequests.getSelectedItem()));
        cbRequests = window.getDecorator().toolBars().addToolbarComboBox("Requests", llmRequestManager.getLlmRequests(), x->window.getAiInputOutputPanelCtrl().handleRequestChanged(x));
    }

    private void decorateMenuBar() {
        decorator.menuBars().setCurrentMenu("Datei");
        decorator.menuBars().addMenuItem("Abfrage neu starten",()->window.getAiInputOutputPanelCtrl().sendRequest(false), KeyEvent.VK_F5, 0);
        decorator.menuBars().addMenuItem("Abfrage fortsetzen",()->window.getAiInputOutputPanelCtrl().sendRequest(true), KeyEvent.VK_F4,0);
        JCheckBoxMenuItem chkBoxProtokoll = decorator.menuBars().addJCheckBoxMenuItem("Abfragen protokollieren", () -> {
        }, 0, 0);
        decorator.menuBars().addMenuItem("Speichern",()->window.getAiInputOutputPanelCtrl().speichereRequest(), KeyEvent.VK_S,InputEvent.CTRL_DOWN_MASK);
    }

    private void handleSetRequest(LlmRequest request) {
        LlmRequest.LastLlmMessageInfo info = request.getLastLlmMessageInfo();

        window.setInputPraefixText(info.system.replace("<system>",""));
        window.setInputText(info.user.replace("<user>",""));
        window.setOutputText(info.assistant.replace("<assistant>",""));
    }
}
