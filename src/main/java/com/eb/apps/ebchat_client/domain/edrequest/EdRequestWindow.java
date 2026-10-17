package com.eb.apps.ebchat_client.domain.edrequest;

import com.eb.apps.ebchat_client.domain.edrequest.gui.RequestEditorPanel;
import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llmreqstore.LlmRequestManager;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class EdRequestWindow extends JFrame {

    private static LlmRequest request;

    @Getter
    private final RequestEditorPanel editPanel;

    public EdRequestWindow() {


        setTitle("Editiere Kontexte");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        editPanel = new RequestEditorPanel();
        add(editPanel, BorderLayout.CENTER);

    }




    public static void main(String[] args) {

            EdRequestWindow ctrl = new EdRequestWindow();
            ctrl.setVisible(true);
    }

    public void setContentMemo(String text) {
        getEditPanel().getEdContentMemo().setText(text);
    }
    public String getContentMemo() {
        return getEditPanel().getEdContentMemo().getText();
    }

    public void setModel(String text) {
        getEditPanel().getEdModel().setText(text);
    }
    public String getModel() {
        return getEditPanel().getEdModel().getText();
    }

    public Container getMessagesToolBar() {
        return editPanel.getMessagesToolBar();
    }

    public LlmMessage getSelectedMessage() {
        return editPanel.getLstMessages().getSelectedValue();
    }

    public void setRequests(List<LlmRequest> llmRequests) {
        DefaultListModel<LlmRequest> requestsModel = new DefaultListModel<LlmRequest>();
        requestsModel.addAll(llmRequests);
        editPanel.getLstRequests().setModel(requestsModel);
    }

    public void setMessages(List<LlmMessage> llmMessages) {
        DefaultListModel<LlmMessage> messagesModel = new DefaultListModel<LlmMessage>();
        messagesModel.addAll(llmMessages);
        //editPanel.getLstRequests().setModel(messagesModel);
    }
}
