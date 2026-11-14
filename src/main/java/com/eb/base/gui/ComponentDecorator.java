package com.eb.base.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import static java.lang.System.out;

class ComponentDecorator {
    private final Dictionary<String, JMenu> nameToMenuBarDictionary = new Hashtable<>();
    private final Dictionary<String, Container> nameToContainerDictionary = new Hashtable<>();
    private String currentMenuName;
    private Container currentContainer;
    private JFrame frame;

    ComponentDecorator(JFrame frame) {
        this.frame = frame;
    }


    public JButton addToolbarButton(String tooltipText, ICF ic, ActionListener listener) {

        Container toolbar = currentContainer;

        JButton btn = new JButton();

        try {
            btn.setIcon(ic.getImageIcon());
        } catch (Exception e) {
            out.println("Image nicht gefunden: " + ic);
        }
        btn.setToolTipText(tooltipText);
        toolbar.add(btn);
        btn.addActionListener(listener);

        if (!(toolbar instanceof JToolBar)) {
            toolbar.revalidate();
            toolbar.repaint();
        }

        return btn;
    }




    private Container fetchContainer(String toolbarName) {
        Container toolbar = nameToContainerDictionary.get(toolbarName);
        if (toolbar!=null)
            return toolbar;

        List<Component> toolbars = GuiUtil.findComponents(frame, x -> x instanceof JToolBar);
        for(Component component : toolbars)
            if (component.getName() != null) {
                String name = component.getName();
                nameToContainerDictionary.put(name, (Container) component);
            }
        return nameToContainerDictionary.get(toolbarName);
    }

}
