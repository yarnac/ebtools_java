package com.eb.inspector.domain.attributes;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class InspectCtrl
{
    private static int maxSubObjectCount = 20;
    private final InspectWindow window;
    private final IniFile myIniFile;
    private TableController tableController;

    public InspectCtrl() {
        window = new InspectWindow();
        myIniFile = IniFileProvider.createIniFile("Inspect.ini");
        GuiDecorator decorator = new GuiDecorator(window, myIniFile, "Einstellungen");
        GenericsInspectAttributeProvider.reset();

        // tableController = new TableController(window);


        JTree tree = window.getTree();
        tree.getSelectionModel().addTreeSelectionListener(e ->
                SwingUtilities.invokeLater(this::updateSelectedNode));


    }

    public static void setMaxSubObjectCount(int m) {
        maxSubObjectCount = m;
    }

    private void updateSelectedNode() {
        InspectTreeNode node = window.getSelectedNode();
        if (node == null || node.getParent() == null) {
            return;
        }

        if (!node.isCompleted())
        {
            InspectMapping mapping = node.getMapping();
            List list = (List) mapping.getSubObjects(node.getObject());
            if (list != null) {
                addObjects(node, list);
            }
            node.setCompleted(true);
        }
    }

    public static void main(String ...args)
    {
        InspectCtrl inspectCtrl = new InspectCtrl();
        SwingUtilities.invokeLater(()->{
            inspectCtrl.window.setVisible(true);
            //inspectCtrl.setObject(Example.createExample());
            inspectCtrl.setObject(inspectCtrl.window);
        });

    }


    public static void createInspectCtrl(Object obj, IInspectValueConverter erkInspectValueConverter)
    {
        InspectCtrl inspectCtrl = new InspectCtrl();
        SwingUtilities.invokeLater(()->{
            inspectCtrl.window.setVisible(true);
            //inspectCtrl.setObject(Example.createExample());
            inspectCtrl.addInspectValueConverter(erkInspectValueConverter);
            inspectCtrl.setObject(obj);
        });

    }


    private void setObject(Object obj) {
        InspectTreeNode root = new InspectTreeNode("Root");

        addObject(root, obj);

        window.getTree().setModel(new DefaultTreeModel(root));
        // obj ist eine Liste oder ein Array, also füge für jedes Objekt darin einen Knoten zu root
    }

    private void addObject(InspectTreeNode parent, Object obj) {
        if (obj == null)
            return;

        if (isListeOderArray(obj))
        {
            addObjects(parent, obj);
        }
        else {
            String name = null;
            if (obj instanceof Tupel) {
                name = "" + ((Tupel<?, ?>) obj).getFirst();
                obj = ((Tupel<?, ?>) obj).getSecond();
            }
            if (isListeOderArray(obj)) {
                InspectTreeNode node = new InspectTreeNode(name, obj);
                node.setParent(parent);
                parent.getChildren().add(node);
                addObjects(node, obj);
            }
            else {
                InspectMapping mapping = InspectAttributeProvider.getInspectMapping(obj.getClass());
                if (mapping != null) {
                    InspectTreeNode node = new InspectTreeNode(obj.getClass().getName(), obj);
                    node.setMapping(mapping);
                    node.setParent(parent);
                    if (name == null)
                        name = mapping.getName(obj);

                    node.setName(name);
                    parent.getChildren().add(node);
                }
            }

        }


    }

    /**
     * Iteriert über alle Elemente einer Collection, Map oder eines Arrays
     * und fügt sie als Unter-Knoten hinzu.
     */
    private void addObjects(InspectTreeNode root, Object obj) {
        if (obj == null)
            return;

        if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            for (int i = 0; i < Math.max(maxSubObjectCount, length); i++) {
                addObject(root, Array.get(obj, i));
            }
        } else if (obj instanceof Iterable<?> iterable) {
            int i=0;
            for (Object item : iterable) {
                if (i++ < maxSubObjectCount) {
                    addObject(root, item);
                }
            }
        } else if (obj instanceof Map<?, ?> map) {
            int i=0;
            // Bei Maps werden typischerweise die Werte (Values) inspiziert
            map.forEach((x,y)->{
                Tupel tupel = new Tupel(x,y);
                addObject(root, tupel);

            });
        }
    }


    /**
     * Prüft ob es sich um eine Liste, Collection, Map oder ein Array handelt.
     */
    private boolean isListeOderArray(Object obj) {
        if (obj == null) return false;

        // Pattern Matching for instanceof (Java 16+, in Java 21 Standard)
        return obj.getClass().isArray()
                || obj instanceof Iterable<?>
                || obj instanceof Map<?, ?>;
    }

    public void addInspectValueConverter(IInspectValueConverter inspectValueConverter) {
        window.addInspectValueConverter(inspectValueConverter);
    }
}
