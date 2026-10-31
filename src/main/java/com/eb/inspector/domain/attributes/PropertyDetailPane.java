
package com.eb.inspector.domain.attributes;

import com.eb.base.gui.EbGridBagUtil;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;

public class PropertyDetailPane extends JPanel {
    private final InspectWindow window;
    private final JScrollPane scrollPane;

    public PropertyDetailPane(InspectWindow window) {
        this.window = window;
        setLayout(new BorderLayout());

        // Hauptpanel für die Listenansicht
        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new BoxLayout(detailPanel, BoxLayout.Y_AXIS));
        detailPanel.setOpaque(false);

        scrollPane = new JScrollPane(detailPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        add(scrollPane, BorderLayout.CENTER);

        // Tree-Synchronisation aufbauen
        window.getTree().getSelectionModel().addTreeSelectionListener(e ->
                SwingUtilities.invokeLater(this::updateDetailPanel)
        );
    }

    @SuppressWarnings("unchecked")
    private void updateDetailPanel() {
        TreePath path = window.getTree().getSelectionPath();
        JPanel mainPanel = (JPanel) scrollPane.getViewport().getView();

        if (path == null || !(path.getLastPathComponent() instanceof InspectTreeNode node)) {
            clearDetailPanel(mainPanel);
            return;
        }

        Object obj = node.getObject();
        InspectMapping<?> mapping = node.getMapping();
        if (obj == null || mapping == null) {
            clearDetailPanel(mainPanel);
            return;
        }

        EbGridBagUtil util = new EbGridBagUtil(mainPanel,0);

        List<InspectMappingAttribute> attrs = mapping.getAttributes();
        mainPanel.removeAll(); // Clean slate

        if (attrs != null) {
            for (var attr : attrs) {
                String name = attr.getName();
                Object value = attr.getValueProvider().apply(obj);
                JTextArea area = createPropertyEdit(name, value);
                util.addLabeledFieldRow(area, name, 0.25, GridBagConstraints.BOTH);
                JPanel row = createPropertyRow(name, value);
                mainPanel.add(row);
                mainPanel.add(Box.createVerticalStrut(6)); // Abstand
            }
        }

        revalidate();
        repaint();
    }

    private void clearDetailPanel(JPanel panel) {
        panel.removeAll();
        revalidate();
        repaint();
    }

    private JTextArea createPropertyEdit(String name, Object value) {
        String text = value == null ? "null" : value.toString();
        JTextArea area = new JTextArea(text);
        // area.setFont(VALUE_FONT);
        area.setEditable(false);
        area.setBackground(UIManager.getColor("Panel.background"));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        return area;
    }

    private JPanel createPropertyRow(String name, Object value) {
        JPanel row = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 4, 0, 4); // Oben, Links, Unten, Rechts (GAP)
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        // 🔹 SPALTENVERHÄLTNIS: [Name : Wert] z.B. 30% : 70%
        double[] ratio = {0.3, 0.7};

        // --- LINKE SPALTE (Label) ---
        JLabel nameLabel = new JLabel(name + ":");
        //nameLabel.setFont(LABEL_FONT);
        // nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        nameLabel.setVerticalAlignment(SwingConstants.TOP);

        gbc.gridx       = 0;
        gbc.gridwidth   = 1;
        gbc.weightx     = ratio[0];
        gbc.anchor      = GridBagConstraints.EAST;
        row.add(nameLabel, gbc);

        // --- RECHTE SPALTE (Value) ---
        String text = value == null ? "null" : value.toString();
        JComponent valueComp;

        if (text.contains(System.lineSeparator()) || text.length() > 0) {
            JTextArea area = new JTextArea(text);
            // area.setFont(VALUE_FONT);
            area.setEditable(false);
            area.setBackground(UIManager.getColor("Panel.background"));
            area.setLineWrap(true);
            area.setWrapStyleWord(true);
            valueComp = new JScrollPane(area);
        } else {
            JTextField field = new JTextField(text);
            // setupBaseComponent(field);
            field.setColumns(24);
            valueComp = field;
        }

        gbc.gridx       = 1;
        gbc.gridwidth   = 1;
        gbc.weightx     = ratio[1];
        gbc.anchor      = GridBagConstraints.WEST;
        row.add(valueComp, gbc);

        // makeCopyable(row, text);
        return row;
    }

    private JPanel createPropertyRow2(String name, Object value) {
        JPanel row = new JPanel(new GridLayout(1, 2, 4, 0));
        row.setOpaque(false);

        // Name (Label)
        JLabel nameLabel = new JLabel(name + ":");
        nameLabel.setFont(nameLabel.getFont().deriveFont(Font.BOLD));
        // nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        row.add(nameLabel);

        // Value (TextArea mit Wrap & ReadOnly)
        JTextField valueField = new JTextField(value == null ? "null" : value.toString());
        valueField.setFont(new Font("Monospaced", Font.PLAIN, 12));
        valueField.setEditable(false);
        valueField.setBackground(UIManager.getColor("Panel.background"));

        // Falls mehrzeilig: TextArea verwenden
        if (value != null) { // && value.toString().contains(System.lineSeparator())) {
            JTextArea textArea = new JTextArea(value.toString());
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
            textArea.setBorder(BorderFactory.createEmptyBorder(4, 6, 4, 6));
            JScrollPane areaScrollPane = new JScrollPane(textArea);
            row.add(areaScrollPane);
        } else {
            valueField.setColumns(20);
            row.add(valueField);
        }
        /*
        Kontextmenü für Copy-to-Clipboard (Debugging-Favorit)
        var contentPane = (value instanceof JTextField jft) ? jft :
                new AbstractMap.SimpleEntry<String, Object>("", value).toString();
        row.addMouseListener(new javax.swing.JMenuItem() {  ... ContextMenu Logik optional  }.getComponentListener());
        */

       return row;
    }
}