package com.eb.base.gui;

import javax.swing.*;

public interface GuiDecoratorMenuBarMethods {

    GuiDecoratorMenuBars getGuiDecoratorMenuBars();

    default JMenu getMenu(String label)
    {
        return getGuiDecoratorMenuBars().getMenu(label);
    }

    default JMenuItem addMenuItem(String menuString, String label, Runnable object) {

        return getGuiDecoratorMenuBars().addMenuItem(menuString, label, object);
    }

    default JCheckBoxMenuItem addJCheckBoxMenuItem(String menuString, String label, Runnable object) {
        return getGuiDecoratorMenuBars().addJCheckBoxMenuItem(menuString, label, object);
    }

    default JMenuBar getMenuBar() {
        return getGuiDecoratorMenuBars().getMenuBar();
    }

    default void addMenuSeparator(String string) {
        getGuiDecoratorMenuBars().addMenuSeparator(string);
    }

    default void setCurrentMenu(String string) {
        getGuiDecoratorMenuBars().setCurrentMenu(string);
    }

    default void addMenuItem(String string, Runnable object) {
        getGuiDecoratorMenuBars().addMenuItem(string, object);
    }

    default JMenuItem addMenuItem(String string, Runnable object, int key, int mask) {
        return getGuiDecoratorMenuBars().addMenuItem(string, object, key, mask);
    }

    default JCheckBoxMenuItem addJCheckBoxMenuItem(String string, Runnable object, int key, int mask) {
        return getGuiDecoratorMenuBars().addJCheckBoxMenuItem(string, object, key, mask);
    }

    default void addMenuSeparator() {
        getGuiDecoratorMenuBars().addMenuSeparator();
    }

    default void setMenuBar(JMenuBar jMenuBar) {
        getGuiDecoratorMenuBars().setMenuBar(jMenuBar);
    }
}
