package com.eb.apps.ebchat_client.domain.edrequest;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llmreqstore.LlmRequestManager;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.gui.adapter.JTextPaneAdapter;
import com.eb.base.gui.persist.ComponentPersisterFactory;
import com.eb.base.gui.persist.IComponentPersister;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;

import java.awt.event.ActionEvent;
import java.util.List;

public class EdRequestCtrl {

    private final IniFile myIniFile;
    private LlmRequestManager manager;
    private LlmRequest actRequest;
    private EdRequestWindow window;


    public EdRequestCtrl() {

        window = new EdRequestWindow();

        myIniFile = IniFileProvider.createIniFile("EdRequests.ini");
        GuiDecorator decorator = new GuiDecorator(window, myIniFile, "Einstellungen");


        decorator.addContainer("tbRequests", window.getRequestsToolBar());
        decorator.addContainer("tbMessages", window.getMessagesToolBar());

        decorator.addToolbarButton("tbRequests", "Delete Request", ICF.DeleteRed, this::deleteRequestClicked);
        decorator.addToolbarButton("tbMessages", "Delete Message", ICF.DeleteRed, this::deleteMessageClicked);

        IComponentPersister persister = ComponentPersisterFactory.createIniFilePersister(myIniFile);
        persister.addComponentItem(window.getSplitPane(),"SplitMain");
        persister.loadAndSetComponentItems();
        decorator.addCloseAction(persister::persistComponentItems);

        SwingUtilities.invokeLater(() -> {
            window.setVisible(true);

        });

        manager = new LlmRequestManager();
        window.setRequests(manager.getLlmRequests());

        window.getEditPanel().getLstRequests().addListSelectionListener(x->handleSelectedRequest(x));
        window.getEditPanel().getLstMessages().addListSelectionListener(x->handleSelectedMessage(x));

        setSelectedRequest(manager.getLlmRequests().getFirst());
/*
        window.getEditPanel().getLstRequests().getModel().addListDataListener(new ListSelectionHandler() {

        });
*/
    }

    private void handleSelectedMessage(ListSelectionEvent x) {

        List<LlmMessage> actMessages = window.getEditPanel().getLstMessages().getSelectedValuesList();
        StringBuilder sb = new StringBuilder();
        for(LlmMessage msg : actMessages) {
            sb.append(msg.getMessageString()).append("\n");
        }
        window.setMessageText(sb.toString());

    }

    private void handleSelectedRequest(ListSelectionEvent x) {
        actRequest = window.getEditPanel().getLstRequests().getSelectedValue();
        if (actRequest != null) {
            setSelectedRequest(actRequest);

        }
    }

    private void setSelectedRequest(LlmRequest request) {
        window.getEditPanel().getLstRequests().setSelectedValue(request, true);

        actRequest = request;
        window.setContentMemo(actRequest.getContentMemo());
        window.setModel(actRequest.getModel());

        window.setMessages(actRequest.getMessages());
        window.setSelectedMessage(actRequest.getMessages().getFirst());
    }

    private void deleteRequestClicked(ActionEvent actionEvent) {
        for(LlmRequest r : window.getEditPanel().getLstRequests().getSelectedValuesList()) {
            manager.deleteRequest(r);
            DefaultListModel<LlmRequest> model = (DefaultListModel<LlmRequest>) window.getEditPanel().getLstRequests().getModel();
            model.removeElement(r);
        }

    }


    private void deleteMessageClicked(ActionEvent actionEvent) {
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
