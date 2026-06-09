package com.eb.reflection;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;

import static javax.swing.UIManager.getString;


public class ReflectionInspector extends JFrame {

    private JTree tree;
    private JTable table;
    private DefaultTableModel tableModel;
    private int MAX_LEVEL = 4;

    public ReflectionInspector(Object rootObject) {
        setTitle("Reflection Inspector");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tree
        DefaultMutableTreeNode rootNode = createTree(rootObject, "Root Object",0);
        tree = new JTree(new DefaultTreeModel(rootNode));

        // Table
        tableModel = new DefaultTableModel(new String[]{"Name", "Value", "Type"}, 0);
        table = new JTable(tableModel);

        // SplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(tree), new JScrollPane(table));
        splitPane.setDividerLocation(300);

        add(splitPane, BorderLayout.CENTER);

        // Listener
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
                if (selectedNode != null) {
                    Object userObject = selectedNode.getUserObject();
                    if (userObject instanceof NodeData) {
                        updateTable(((NodeData) userObject).value);
                    }
                }
            }
        });
    }

    private DefaultMutableTreeNode createTree(Object obj, String name, int level) {
        NodeData data = new NodeData(name, obj);
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(data);

        if (level>MAX_LEVEL)
            return node;

        level++;

        if (obj == null) return node;

        Class<?> clazz = obj.getClass();

        if (clazz.getName().startsWith("java.lang.String"))
            return node;


        // Fields
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                try {
                    Object value = field.get(obj);
                    String fieldName = field.getName();
                    DefaultMutableTreeNode child = createTree(value, "Field: " + fieldName, level);
                    node.add(child);
                } catch (IllegalAccessException e) {
                    node.add(new DefaultMutableTreeNode(new NodeData(field.getName(), "<inaccessible>")));
                }
            }
            catch (Exception e) {
                node.add(new DefaultMutableTreeNode(new NodeData(field.getName(), "<inaccessible>")));
            }
        }

        // Methods
        for (Method method : clazz.getDeclaredMethods()) {
            DefaultMutableTreeNode methodNode = new DefaultMutableTreeNode(
                    new NodeData("Method: " + method.getName(), method));
            node.add(methodNode);
        }

        return node;
    }

    private void updateTable(Object obj) {
        tableModel.setRowCount(0);


        if (obj == null) return;

        Class<?> clazz = obj.getClass();

        tableModel.addRow(new Object[]{"Class", clazz.getName()});

        if (clazz.getName().startsWith("java.lang.String"))
            return;

        // Fields
        for (Field field : clazz.getDeclaredFields()) {

            try {
                field.setAccessible(true);
                Object value = field.get(obj);
                tableModel.addRow(new Object[]{"Field: " + field.getName(), value});
            } catch (Exception e) {
                tableModel.addRow(new Object[]{"Field: " + field.getName(), "<inaccessible>"});
            }
        }

        // Methods
        for (Method method : clazz.getDeclaredMethods()) {
            String paramstr = this.getString(method.getParameterTypes(), method.getParameterAnnotations());
            tableModel.addRow(new Object[]{"Method", method.getName(), paramstr});
            // tableModel.addRow(new Object[]{"Return Type", method.getReturnType().getName()});

            /*
            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                tableModel.addRow(new Object[]{"Param " + i, params[i].getName()});
            }
            */

        }
    }

    private String getString(Class<?>[] parameterTypes, Annotation[][] parameterAnnotations) {
        StringBuilder strb = new StringBuilder();
        if (parameterTypes.length==0)
            return "()";
        for (int i = 0; i < parameterTypes.length; i++) {
            if (i>0)
                strb.append(", ");
            Annotation[] annotations = parameterAnnotations[i];
            annotations.toString();
            strb.append(parameterTypes[i].getSimpleName());



        }
        return strb.toString();
    }


    private static class NodeData {
        String name;
        Object value;

        NodeData(String name, Object value) {
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    // Test
    public static void main(String[] args) {

        Datenprovider provider = new Datenprovider();
        java.util.List<Object> object = provider.initialize();
        SwingUtilities.invokeLater(() -> {
            ReflectionInspector inspector = new ReflectionInspector(object.get(11));
            inspector.setVisible(true);
        });
    }

    static class TestObject {
        private String name = "Test";
        private int value = 42;
        private Nested nested = new Nested();

        public String getName() { return name; }

        static class Nested {
            double number = 3.14;
        }
    }
}
