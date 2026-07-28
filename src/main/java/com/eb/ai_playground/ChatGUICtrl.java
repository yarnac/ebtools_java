package com.eb.ai_playground;

import com.eb.ai_service.llm_client.api.LlmClient;
import com.eb.ai_service.llm_client.api.LlmRequest;
import com.eb.ai_service.llm_client.api.LlmResponse;
import com.eb.ai_service.llm_client.infrastructure.LlmModel;
import com.eb.ai_service.llm_client.infrastructure.ModelProvider;

import javax.swing.*;

public class ChatGUICtrl {
    private JFrame frame;
    private ChatGUI chatBotGUI;
    private static ChatGUICtrl guiController;

    public static void main(String[] args) {
        guiController = new ChatGUICtrl();
        SwingUtilities.invokeLater(guiController::initializeView);
    }

    private void initializeView() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            System.err.println("Error setting look and feel: " + ex.getMessage());
            ex.printStackTrace();
        }

        frame = new JFrame("ChatBot GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);

        chatBotGUI = new ChatGUI();
        frame.getContentPane().add(chatBotGUI);

        fillComboBoxes();

        chatBotGUI.setTextUser("<<Du bist ein freundlicher Assistent.>> \nSag Hallo");

        registerEvents();

        frame.setVisible(true);
    }

    private void registerEvents() {
        chatBotGUI.getBtnNewChat().addActionListener(e -> newButtonClicked());
        chatBotGUI.getBtnSendRequest().addActionListener(e -> sendRequest());
    }

    private void sendRequest() {
        LlmModel model = (LlmModel) chatBotGUI.getCbModels().getSelectedItem();

        LlmRequest request = LlmRequest.builder()
                .addRequestMsg(chatBotGUI.getTextUser())
                .setModel(model.getModelName())
                .build();

        LlmResponse response = new LlmClient().sendRequest(request);
        chatBotGUI.getEdOutput().setText(response.getAnswer());
    }

    private static void newButtonClicked() {
    }

    private void fillComboBoxes() {
        ModelProvider provider = new ModelProvider();
        LlmModel[] array = provider.getModels().toArray(new LlmModel[0]);
        chatBotGUI.getCbModels().setModel(new DefaultComboBoxModel<>(array));

    }

    public JFrame getFrame() {
        return frame;
    }

    public void setFrame(JFrame frame) {
        this.frame = frame;
    }

    public ChatGUI getChatBotGUI() {
        return chatBotGUI;
    }

    public void setChatBotGUI(ChatGUI chatBotGUI) {
        this.chatBotGUI = chatBotGUI;
    }
}
