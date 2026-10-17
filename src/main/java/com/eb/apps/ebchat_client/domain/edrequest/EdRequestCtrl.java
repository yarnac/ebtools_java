package com.eb.apps.ebchat_client.domain.edrequest;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llmreqstore.LlmRequestManager;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import javax.swing.plaf.basic.BasicListUI;
import javax.swing.plaf.basic.BasicListUI.ListSelectionHandler;

import java.awt.event.ActionEvent;

import static java.awt.SystemColor.window;

public class EdRequestCtrl {

    private final IniFile myIniFile;
    private LlmRequestManager manager;
    private LlmRequest actRequest;
    private EdRequestWindow window;


    public EdRequestCtrl() {

        window = new EdRequestWindow();

        myIniFile = IniFileProvider.createIniFile("EdRequests.ini");
        GuiDecorator decorator = new GuiDecorator(window, myIniFile, "Einstellungen");
        decorator.addContainer("tbMessages", window.getMessagesToolBar());
        decorator.addToolbarButton("tbMessages", "Delete Message", ICF.DeleteRed, this::deleteClicked);

        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);

        });

        manager = new LlmRequestManager();
        actRequest = manager.getLlmRequests().getFirst();
        window.setContentMemo(actRequest.getContentMemo());
        window.setModel(actRequest.getModel());

        window.setRequests(manager.getLlmRequests());
/*
        window.getEditPanel().getLstRequests().getModel().addListDataListener(new ListSelectionHandler() {

        });
*/
    }

    private void deleteClicked(ActionEvent actionEvent) {
        LlmMessage msg = window.getSelectedMessage();
        if (msg!=null)
        {
            actRequest.getMessages().remove(msg);
            manager.store(actRequest);
            DefaultListModel<LlmMessage> model = new DefaultListModel<LlmMessage>();
            model.addAll(actRequest.getMessages());
            //editPanel.getLstMessages().setModel(model);
        }
    }

    public static void main(String[] args) {
        EdRequestCtrl ctrl = new EdRequestCtrl();


    }
}
