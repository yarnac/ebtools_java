package com.eb.apps.ebchat_client.domain.edrequest;

import com.eb.apps.ebchat_client.domain.edrequest.gui.RequestEditorPanel;
import com.eb.base.ai_service.llm_client.api.LlmMessage;
import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.ai_service.llmreqstore.LlmRequestManager;
import com.eb.base.gui.EbSplitPanel;
import com.eb.base.gui.FontUtil;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.gui.adapter.JTextPaneAdapter;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static java.awt.SystemColor.window;

public class EdRequestWindow extends JFrame {

    private static LlmRequest request;

    @Getter
    private final RequestEditorPanel editPanel;
    private final JPanel textPanel;

    @Getter
    private final JScrollPane messageTextScrollPane;

    @Getter
    private final JTextPane messageTextPane;
    @Getter
    private final JSplitPane splitPane;
    private final JTextPaneAdapter messageTextAdapter;

    public EdRequestWindow() {
        setTitle("Editiere LLM-Requests");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window



        editPanel = new RequestEditorPanel();
        textPanel = new JPanel(new BorderLayout());
        textPanel.setMinimumSize(new Dimension(400, 40));
        messageTextPane = new JTextPane();
        messageTextScrollPane = new JScrollPane(messageTextPane);
        textPanel.add(messageTextScrollPane, BorderLayout.CENTER);
        EbSplitPanel ebSplitPanel = new EbSplitPanel("mainSplitPanel", editPanel, textPanel, 1);
        splitPane = ebSplitPanel.getSplitPane();

        messageTextAdapter = new JTextPaneAdapter(getMessageTextScrollPane(), getMessageTextPane());

        FontUtil.setMonospaceFont(16,
                editPanel,
                textPanel,
                messageTextPane);

        FontUtil.setMonospaceFont(14,
                editPanel.getLstMessages(), editPanel.getLstRequests());

        add(ebSplitPanel.getMainPanel(), BorderLayout.CENTER);

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

    public void setMessageText(String text) {
        messageTextPane.setText(text);
        SwingUtilities.invokeLater(() -> {
            messageTextAdapter.setFirstVisibleLine(0);

        });


    }

    public JToolBar getRequestsToolBar() {
        return editPanel.getRequestsToolBar();
    }


    public JToolBar getMessagesToolBar() {
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
        editPanel.getLstMessages().setModel(messagesModel);
    }

    public void setSelectedMessage(LlmMessage message) {
        getEditPanel().getLstMessages().setSelectedValue(message, true);
    }
}
