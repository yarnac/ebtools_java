package com.eb.base.gui;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class GuiDecoratorMenuBars {
    @Setter
    private JMenuBar menuBar;
    @Getter
    private final JFrame frame;

    @Setter
    private String currentMenu;

    private final Dictionary<String, JMenu> nameToMenuBarDictionary = new Hashtable<>();

    public GuiDecoratorMenuBars(JFrame frame) {
        this.frame = frame;
    }


    public JMenuBar getMenuBar() {
        if (menuBar!=null)
            return menuBar;
        if (getFrame()==null)
            return null;
        if (getFrame().getJMenuBar()==null)
        {
            menuBar = new JMenuBar();
            getFrame().setJMenuBar(menuBar);
        }
        else
            menuBar = getFrame().getJMenuBar();

        return menuBar;
    }

    public void addMenuSeparator(String string) {
        JMenu menu = getMenu(string);
        menu.add(new JSeparator());
    }

    public JMenu getMenu(String label) {
        JMenu jMenu = nameToMenuBarDictionary.get(label);
        if (jMenu==null)
        {
            jMenu = new JMenu(label);
            getMenuBar().add(jMenu);
            nameToMenuBarDictionary.put(label,  jMenu);
        }
        return jMenu;
    }

    public JMenuItem addMenuItem(String menuString, String label, Runnable object) {
        JMenu menu = getMenu(menuString);

        JMenuItem item = new JMenuItem(label);

        item.addActionListener(x->object.run());
        menu.add(item);
        return item;
    }

    public JCheckBoxMenuItem addJCheckBoxMenuItem(String menuString, String label, Runnable object) {
        JMenu menu = getMenu(menuString);

        JCheckBoxMenuItem item = new JCheckBoxMenuItem(label);

        item.addActionListener(x->object.run());
        menu.add(item);
        return item;
    }

    public void addMenuItem(String string, Runnable object) {
        addMenuItem(currentMenu, string, object);
    }

    public JMenuItem addMenuItem(String string, Runnable object, int key, int mask) {
        JMenuItem item = addMenuItem(currentMenu, string, object);
        item.setAccelerator( KeyStroke.getKeyStroke(key, mask) );
        return item;
    }

    public JCheckBoxMenuItem addJCheckBoxMenuItem(String string, Runnable object, int key, int mask) {
        JCheckBoxMenuItem item = addJCheckBoxMenuItem(currentMenu, string, object);
        item.setAccelerator( KeyStroke.getKeyStroke(key, mask) );
        return item;
    }

    public void addMenuSeparator() {
        addMenuSeparator(currentMenu);

    }
}
