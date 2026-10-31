package com.eb.inspector.domain.attributes;

import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.TreePath;
import java.util.List;

@Getter
public class TableController {
    private final InspectWindow window;

    private DefaultTableModel tableModel = new DefaultTableModel(new String[]{"Name", "Value"}, 0) {
        @Override
        public boolean isCellEditable(int row, int col) {
            return false; // Nur lesbar für Anzeige
        }
    };

    public TableController(InspectWindow window) {
        this.window = window;
        window.getTable().setModel(this.tableModel);
        setupTreeSelectionListener();
    }

    /**
     * Fügt einen TreeSelectionListener hinzu, der bei jeder Änderung im JTree
     * die Tabelle aktualisiert. {@code valueChanged} läuft standardmäßig auf dem Event Dispatch Thread (EDT),
     * aber {@code SwingUtilities.invokeLater()} sorgt für thread-sichere Model-Updates.
     */
    private void setupTreeSelectionListener() {
        JTree tree = window.getTree();
        tree.getSelectionModel().addTreeSelectionListener(e ->
                SwingUtilities.invokeLater(this::updateTable));
    }

    @SuppressWarnings("unchecked")
    private void updateTable() {
        // Selektierter Pfad ermitteln
        TreePath path = window.getTree().getSelectionPath();

        if (path == null) {
            tableModel.setRowCount(0);
            return;
        }

        var lastComp = path.getLastPathComponent();
        if (!(lastComp instanceof InspectTreeNode node)) {
            tableModel.setRowCount(0);
            return;
        }

        Object obj = node.getObject();
        InspectMapping<?> mapping = node.getMapping();

        if (obj == null || mapping == null) {
            tableModel.setRowCount(0);
            return;
        }

        // Tabelle zurücksetzen
        tableModel.setRowCount(0);

        // Attribute des Mappings auslesen (package-private Feldzugriff garantiert in selbem Package)
        // List<? extends InspectMappingAttribute<?>> attrs = mapping.getAttributes();
        List<InspectMappingAttribute> attrs = mapping.getAttributes();

        if (attrs != null) {
            for (var attr : attrs) {
                String name = attr.getName();
                Object value = attr.getValueProvider().apply(obj);
                tableModel.addRow(new Object[]{name, value == null ? "null" : value.toString()});
            }
        }

    }
}