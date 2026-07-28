package com.eb.ai_playground;

import com.eb.ai_service.llm_client.infrastructure.LlmModel;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class ChatBotGUI extends JPanel {
    private JToolBar toolBar;
    private JButton btnNewChat;
    private JButton btnSendRequest;
    private JPanel panelMain;
    private JPanel panelSystemMessage;
    private JTextArea edSystemMessage;
    private JScrollPane scrollSystemMessage;
    private JPanel panelUserMessage;
    private JTextArea edUserMessage;
    private JScrollPane scrollUserMessage;
    private JPanel panelOutput;
    private JTextArea edOutput;
    private JScrollPane scrollOutput;
    private JSplitPane splitPaneTop;
    private JSplitPane splitPaneMain;

    private JComboBox<LlmModel> cbModels;
    private JComboBox<LlmModel> cbContext;

    public ChatBotGUI() {
        initializeComponents();
        layoutComponents();
    }

    private void initializeComponents() {
        this.setLayout(new BorderLayout(5, 5));
        this.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Toolbar
        toolBar = new JToolBar();
        toolBar.setFloatable(false);

        int size = 30;
        
        btnNewChat = new JButton(createNewChatIcon());
        btnNewChat.setToolTipText("New Chat");
        btnNewChat.setPreferredSize(new Dimension(size, size));
        
        btnSendRequest = new JButton(createSendIcon());
        btnSendRequest.setToolTipText("Send Request");
        btnSendRequest.setPreferredSize(new Dimension(size, size));

        cbModels = new JComboBox<>();
        cbModels.setPreferredSize(new Dimension(300, size));

        cbContext = new JComboBox<>();
        cbContext.setPreferredSize(new Dimension(200, size));
        
        toolBar.add(btnNewChat);
        toolBar.addSeparator();
        toolBar.add(btnSendRequest);
        toolBar.add(cbModels);
        toolBar.add(cbContext);

        // System Message Panel
        panelSystemMessage = new JPanel(new BorderLayout());
        panelSystemMessage.setBorder(BorderFactory.createTitledBorder("System Message"));
        edSystemMessage = new JTextArea();
        edSystemMessage.setLineWrap(true);
        edSystemMessage.setWrapStyleWord(true);
        edSystemMessage.setFont(new Font("Monospaced", Font.PLAIN, 12));
        edSystemMessage.setEditable(true);
        scrollSystemMessage = new JScrollPane(edSystemMessage);
        scrollSystemMessage.setPreferredSize(new Dimension(100, 75));
        panelSystemMessage.add(scrollSystemMessage, BorderLayout.CENTER);

        // User Message Panel
        panelUserMessage = new JPanel(new BorderLayout());
        panelUserMessage.setBorder(BorderFactory.createTitledBorder("User Message"));
        edUserMessage = new JTextArea();
        edUserMessage.setLineWrap(true);
        edUserMessage.setWrapStyleWord(true);
        edUserMessage.setFont(new Font("Monospaced", Font.PLAIN, 12));
        edUserMessage.setEditable(true);
        scrollUserMessage = new JScrollPane(edUserMessage);
        scrollUserMessage.setPreferredSize(new Dimension(100, 150));
        panelUserMessage.add(scrollUserMessage, BorderLayout.CENTER);

        // Output Panel
        panelOutput = new JPanel(new BorderLayout());
        panelOutput.setBorder(BorderFactory.createTitledBorder("Output"));
        edOutput = new JTextArea();
        edOutput.setLineWrap(true);
        edOutput.setWrapStyleWord(true);
        edOutput.setEditable(true);
        edOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollOutput = new JScrollPane(edOutput);
        scrollOutput.setPreferredSize(new Dimension(100, 600));
        panelOutput.add(scrollOutput, BorderLayout.CENTER);

        // Main Panel mit SplitPanes
        // Erste SplitPane: System Message über User Message
        splitPaneTop = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelSystemMessage, panelUserMessage);
        splitPaneTop.setDividerLocation(0.2);
        splitPaneTop.setResizeWeight(0.0);
        splitPaneTop.setOneTouchExpandable(true);
        configureSplitPane(splitPaneTop);

        // Zweite SplitPane: (System + User) über Output
        splitPaneMain = new JSplitPane(JSplitPane.VERTICAL_SPLIT, splitPaneTop, panelOutput);
        splitPaneMain.setDividerLocation(0.2);
        splitPaneMain.setResizeWeight(0.5);
        splitPaneMain.setOneTouchExpandable(true);
        configureSplitPane(splitPaneMain);

        panelMain = new JPanel(new BorderLayout());
        panelMain.add(splitPaneMain, BorderLayout.CENTER);
    }

    private void configureSplitPane(JSplitPane splitPane) {
        splitPane.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(UIManager.getColor("controlShadow"));
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });
    }

    private Icon createNewChatIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(70, 130, 180));
                
                // Draw document icon
                g2d.fillRect(x + 8, y + 4, 16, 20);
                g2d.setColor(Color.WHITE);
                g2d.fillRect(x + 10, y + 6, 12, 16);
                
                // Draw lines
                g2d.setColor(new Color(70, 130, 180));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawLine(x + 12, y + 10, x + 20, y + 10);
                g2d.drawLine(x + 12, y + 14, x + 20, y + 14);
                g2d.drawLine(x + 12, y + 18, x + 18, y + 18);
            }

            @Override
            public int getIconWidth() {
                return 24;
            }

            @Override
            public int getIconHeight() {
                return 24;
            }
        };
    }

    private Icon createSendIcon() {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(34, 139, 34));
                
                // Draw envelope/send icon
                int[] xPoints = {x + 4, x + 20, x + 12};
                int[] yPoints = {y + 6, y + 14, y + 20};
                g2d.fillPolygon(xPoints, yPoints, 3);
                
                // Draw envelope back
                g2d.setColor(Color.WHITE);
                g2d.drawRect(x + 4, y + 4, 16, 12);
                g2d.drawLine(x + 4, y + 4, x + 12, y + 10);
                g2d.drawLine(x + 20, y + 4, x + 12, y + 10);
            }

            @Override
            public int getIconWidth() {
                return 24;
            }

            @Override
            public int getIconHeight() {
                return 24;
            }
        };
    }

    private void layoutComponents() {
        this.add(toolBar, BorderLayout.NORTH);
        this.add(panelMain, BorderLayout.CENTER);
    }

    public String getTextSystem()
    {
        return edSystemMessage.getText();
    }

    public void setTextSystem(String text)
    {
        edSystemMessage.setText(text);
    }

    public String getTextUser()
    {
        return edUserMessage.getText();
    }

    public void setTextUser(String text)
    {
        edUserMessage.setText(text);
    }

    public String getTextOutput()
    {
        return edOutput.getText();
    }

    public void setTextOutput(String text)
    {
        edOutput.setText(text);
    }


    // Getter und Setter für Toolbar-Buttons
    public JButton getBtnNewChat() {
        return btnNewChat;
    }

    public JButton getBtnSendRequest() {
        return btnSendRequest;
    }

    // Getter und Setter für TextAreas
    public JTextArea getEdSystemMessage() {
        return edSystemMessage;
    }

    public JTextArea getEdUserMessage() {
        return edUserMessage;
    }

    public JTextArea getEdOutput() {
        return edOutput;
    }

    // Getter und Setter für Panels
    public JPanel getPanelMain() {
        return panelMain;
    }

    public JPanel getPanelSystemMessage() {
        return panelSystemMessage;
    }

    public JPanel getPanelUserMessage() {
        return panelUserMessage;
    }

    public JPanel getPanelOutput() {
        return panelOutput;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    // Getter und Setter für ScrollPanes
    public JScrollPane getScrollSystemMessage() {
        return scrollSystemMessage;
    }

    public JScrollPane getScrollUserMessage() {
        return scrollUserMessage;
    }

    public JScrollPane getScrollOutput() {
        return scrollOutput;
    }

    public JSplitPane getSplitPaneTop() {
        return splitPaneTop;
    }

    public JSplitPane getSplitPaneMain() {
        return splitPaneMain;
    }

    public JComboBox<LlmModel> getCbModels() {
        return cbModels;
    }

    public JComboBox<LlmModel> getCbContext() {
        return cbContext;
    }
}
