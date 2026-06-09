package com.eb.reflection;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;
import java.util.List;

/**
 * Profi Reflection Inspector
 * - Zyklenerkennung
 * - Arrays / Collections / Maps
 * - Fields / Methods / Type Hierarchy
 * - Parameter + Annotationen
 * - Modifier + Interfaces
 */
public class ReflectionInspectorPro extends JFrame {

    private final JTree tree;
    private final JTable detailsTable;
    private final DefaultTableModel tableModel;
    private final JTabbedPane tabs;
    private final IdentityHashMap<Object, Boolean> visited = new IdentityHashMap<>();

    public ReflectionInspectorPro(Object rootObject) {
        setTitle("Reflection Inspector Pro");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        DefaultMutableTreeNode root = buildObjectNode("Root", rootObject);
        tree = new JTree(new DefaultTreeModel(root));

        tableModel = new DefaultTableModel(new String[]{"Property", "Value"}, 0);
        detailsTable = new JTable(tableModel);

        tabs = new JTabbedPane();
        tabs.addTab("Details", new JScrollPane(detailsTable));
        tabs.addTab("Summary", new JScrollPane(new JTextArea()));

        JSplitPane split = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree),
                tabs
        );
        split.setDividerLocation(420);

        add(split, BorderLayout.CENTER);

        tree.addTreeSelectionListener(this::onTreeSelection);
    }

    private void onTreeSelection(TreeSelectionEvent e) {
        DefaultMutableTreeNode node =
                (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if (node == null) return;
        Object uo = node.getUserObject();
        if (!(uo instanceof InspectorNode data)) return;

        updateDetails(data.value);
    }

    private DefaultMutableTreeNode buildObjectNode(String label, Object obj) {
        InspectorNode data = new InspectorNode(label, obj);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);

        if (obj == null) return node;

        Class<?> clazz = obj.getClass();

        if (isLeafType(clazz)) {
            return node;
        }

        if (visited.containsKey(obj)) {
            node.add(new DefaultMutableTreeNode(new InspectorNode("<cycle>", null)));
            return node;
        }
        visited.put(obj, true);

        if (clazz.isArray()) {
            int len = Array.getLength(obj);
            for (int i = 0; i < len; i++) {
                Object value = Array.get(obj, i);
                node.add(buildObjectNode("[" + i + "]", value));
            }
            return node;
        }

        if (obj instanceof Collection<?> col) {
            int i = 0;
            for (Object item : col) {
                node.add(buildObjectNode("[" + i++ + "]", item));
            }
            return node;
        }

        if (obj instanceof Map<?, ?> map) {
            for (Map.Entry<?, ?> e : map.entrySet()) {
                node.add(buildObjectNode("Key=" + e.getKey(), e.getValue()));
            }
            return node;
        }

        DefaultMutableTreeNode fieldsNode = new DefaultMutableTreeNode("Fields");
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                fieldsNode.add(buildObjectNode(field.getName(), value));
            } catch (Exception ex) {
                fieldsNode.add(new DefaultMutableTreeNode(
                        new InspectorNode(field.getName() + " <restricted>", null)
                ));
            }
        }
        node.add(fieldsNode);

        DefaultMutableTreeNode methodsNode = new DefaultMutableTreeNode("Methods");
        for (Method method : clazz.getDeclaredMethods()) {
            methodsNode.add(new DefaultMutableTreeNode(
                    new InspectorNode(buildMethodSignature(method), method)
            ));
        }
        node.add(methodsNode);

        return node;
    }

    private void updateDetails(Object obj) {
        tableModel.setRowCount(0);
        if (obj == null) return;

        Class<?> clazz = (obj instanceof Method m) ? m.getDeclaringClass() : obj.getClass();

        tableModel.addRow(new Object[]{"Class", clazz.getName()});
        tableModel.addRow(new Object[]{"Superclass",
                clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "-"});
        tableModel.addRow(new Object[]{"Modifiers", Modifier.toString(clazz.getModifiers())});

        for (Class<?> iface : clazz.getInterfaces()) {
            tableModel.addRow(new Object[]{"Interface", iface.getName()});
        }

        if (obj instanceof Method method) {
            tableModel.addRow(new Object[]{"Method", method.getName()});
            tableModel.addRow(new Object[]{"Return Type", method.getReturnType().getTypeName()});

            Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                Parameter p = params[i];
                tableModel.addRow(new Object[]{"Parameter " + i,
                        p.getType().getTypeName() + " " + p.getName()});

                for (Annotation ann : p.getAnnotations()) {
                    tableModel.addRow(new Object[]{"  Annotation",
                            ann.annotationType().getSimpleName()});
                }
            }
            return;
        }

        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                tableModel.addRow(new Object[]{field.getName(), String.valueOf(value)});
                tableModel.addRow(new Object[]{field.getName() + " : type",
                        field.getType().getTypeName()});
                tableModel.addRow(new Object[]{field.getName() + " : modifier",
                        Modifier.toString(field.getModifiers())});
            } catch (Exception e) {
                tableModel.addRow(new Object[]{field.getName(), "<restricted>"});
            }
        }
    }

    private boolean isLeafType(Class<?> clazz) {
        return clazz.isPrimitive()
                || clazz.getName().startsWith("java.lang")
                || Number.class.isAssignableFrom(clazz)
                || clazz == String.class
                || clazz == Boolean.class
                || clazz.isEnum();
    }

    private String buildMethodSignature(Method m) {
        StringBuilder sb = new StringBuilder();
        sb.append(m.getName()).append("(");
        Class<?>[] params = m.getParameterTypes();
        for (int i = 0; i < params.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(params[i].getSimpleName());
        }
        sb.append(") : ").append(m.getReturnType().getSimpleName());
        return sb.toString();
    }

    private record InspectorNode(String label, Object value) {
        @Override
        public String toString() {
            return label;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DemoRoot demo = new DemoRoot();
            ReflectionInspectorPro ui = new ReflectionInspectorPro(new Datenprovider().initialize().get(9));
            ui.setVisible(true);
        });
    }

    static class DemoRoot {
        String name = "Inspector";
        List<String> values = Arrays.asList("A", "B", "C");
        Map<String, Integer> scores = Map.of("X", 1, "Y", 2);
        Nested nested = new Nested();

        public String hello(String name) {
            return "Hello " + name;
        }
    }

    static class Nested {
        double amount = 42.42;
    }
}
