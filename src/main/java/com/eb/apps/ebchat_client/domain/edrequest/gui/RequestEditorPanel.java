/*
 * Copyright parcIT GmbH
 *
 * Dieser Source-Code steht unter dem alleinigen Urheberschutz der parcIT GmbH.
 * Die Nutzung und Weitergabe ist nur mit ausdrücklicher Erlaubnis der parcIT GmbH gestattet.
 *
 */

package com.eb.apps.ebchat_client.domain.edrequest.gui;

import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.gui.EbGridBagUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;

public final class RequestEditorPanel extends JPanel {

    @Getter
    private JTextField edContentMemo;

    @Getter     @Setter
    private JToolBar messagesToolBar;
    private Runnable saveListener;
    @Getter
    private JList<LlmRequest> lstRequests;
    @Getter
    private JList<LlmMessage> lstMessages;
    @Getter
    private JTextField edModel;

    public RequestEditorPanel() {
        super(new GridBagLayout());

        initializeComponents();
    }

    private void initializeComponents() {
        setBorder(BorderFactory.createEmptyBorder(2, 4, 4, 4));

        setMinimumSize(new Dimension(20, 20));

        EbGridBagUtil util = new EbGridBagUtil(this, 0);

        lstRequests = util.addLabeledScrollPaneWithFieldRow(new JList<LlmRequest>(), "Rqsts", 1.0, GridBagConstraints.BOTH);
        edContentMemo = util.addLabeledFieldRow(new JTextField(), "Info", 0.0, GridBagConstraints.HORIZONTAL);
        edModel = util.addLabeledFieldRow(new JTextField(), "Model", 0.0, GridBagConstraints.HORIZONTAL);
        messagesToolBar = util.addToolBarRow(false);
        lstMessages = util.addLabeledScrollPaneWithFieldRow(new JList<LlmMessage>(), "Msgs", 1.0, GridBagConstraints.BOTH);
    }

    public void transferModelToView(LlmRequest request) {
        edContentMemo.setText(request.getContentMemo());
        edModel.setText(request.getModel());
    }

    public void transferViewToModel(LlmRequest request) {

        request.setContentMemo(edContentMemo.getText());
        request.setModel(edModel.getText());
    }

    public void setSaveListener(Runnable listener){
        saveListener = listener;
    }

    public void transferViewToModel() {
    }


}