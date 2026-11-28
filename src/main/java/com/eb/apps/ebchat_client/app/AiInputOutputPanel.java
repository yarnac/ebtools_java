package com.eb.apps.ebchat_client.app;

import com.eb.baseswing.gui.EbSplitPanel;
import com.eb.baseswing.gui.adapter.JTextPaneAdapter;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class AiInputOutputPanel extends JPanel {


    private JTextPane textPaneInput;

    private JTextPane textPaneOutput;

    private JScrollPane scrollPaneInputText;
    @Getter
    private JTextPaneAdapter textPaneAdapterOutput;
    private EbSplitPanel splitPanelInput;
    private JLabel statusBarOutputLabel;
    private EbSplitPanel splitPanelMessages;
    private JScrollPane scrollPaneInputPraefix;
    private JTextPane textFieldInputPraefix;

    public AiInputOutputPanel() {
        setLayout(new BorderLayout());
        initializeView();
    }

    private void initializeView() {

        textPaneInput = new JTextPane();
        textPaneOutput = new JTextPane();


        JScrollPane scrollPaneOutput = new JScrollPane(textPaneOutput);
        textPaneAdapterOutput = new JTextPaneAdapter(scrollPaneOutput, textPaneOutput);

        UndoDecorator.registerForTextArea(getTextPaneInput());
        UndoDecorator.registerForTextArea(getTextPaneOutput());


        splitPanelInput = new EbSplitPanel("splitPanelInput", getScrollPaneInputPraefix(), getScrollPaneInputText(), 0);


        JPanel jPanelOutputAndStatus = new JPanel(new BorderLayout());
        jPanelOutputAndStatus.add(scrollPaneOutput, BorderLayout.CENTER);
        statusBarOutputLabel = new JLabel();
        statusBarOutputLabel.setText("Status");
        jPanelOutputAndStatus.add(statusBarOutputLabel, BorderLayout.SOUTH);

        splitPanelMessages = new EbSplitPanel("MessageSplitter", splitPanelInput.getMainPanel(), jPanelOutputAndStatus, JSplitPane.VERTICAL_SPLIT);
        splitPanelMessages.setDividerLocation(200);
        add(splitPanelMessages, BorderLayout.CENTER);
    }

    public JScrollPane getScrollPaneInputPraefix() {
        if (scrollPaneInputPraefix==null) {
            scrollPaneInputPraefix = new JScrollPane(getTextFieldInputPraefix());
        }
        return scrollPaneInputPraefix;
    }


    public JScrollPane getScrollPaneInputText() {
        if (scrollPaneInputText==null) {
            scrollPaneInputText = new JScrollPane(getTextPaneInput());
        }
        return scrollPaneInputText;
    }

    public  JTextPane getTextFieldInputPraefix() {
        if (textFieldInputPraefix == null) {
            textFieldInputPraefix = new JTextPane();

            textFieldInputPraefix.setText("Übersetze ins Deutsche:");
        }
        return textFieldInputPraefix;
    }

}
