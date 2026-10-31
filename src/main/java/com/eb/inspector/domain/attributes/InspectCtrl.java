package com.eb.inspector.domain.attributes;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.inspector.Example;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Array;

public class InspectCtrl
{
    private final InspectWindow window;
    private final IniFile myIniFile;
    private TableController tableController;

    public InspectCtrl()
    {
        window = new InspectWindow();
        myIniFile = IniFileProvider.createIniFile("Inspect.ini");
        GuiDecorator decorator = new GuiDecorator(window, myIniFile, "Einstellungen");

        // tableController = new TableController(window);
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
            InspectMapping mapping = InspectAttributeProvider.getInspectMapping(obj.getClass());
            if (mapping != null)
            {
                InspectTreeNode node = new InspectTreeNode(obj.getClass().getName(), obj);
                node.setMapping(mapping);
                node.setName(mapping.getName(obj));
                parent.getChildren().add(node);

                List list = (List) mapping.getSubObjects(obj);
                if (list != null) {
                    addObjects(node, list);
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
            for (int i = 0; i < length; i++) {
                addObject(root, Array.get(obj, i));
            }
        } else if (obj instanceof Iterable<?> iterable) {
            for (Object item : iterable) {
                addObject(root, item);
            }
        } else if (obj instanceof Map<?, ?> map) {
            // Bei Maps werden typischerweise die Werte (Values) inspiziert
            for (Object value : map.values()) {
                addObject(root, value);
            }
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
}
