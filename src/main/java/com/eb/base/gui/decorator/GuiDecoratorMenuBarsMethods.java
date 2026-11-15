package com.eb.base.gui.decorator;

import javax.swing.*;

public interface GuiDecoratorMenuBarsMethods {

    GuiDecoratorMenuBars getGuiDecoratorMenuBars();

    private GuiDecoratorMenuBars gdmb(){ return getGuiDecoratorMenuBars(); }
    default JMenu getMenu(String label) { return gdmb().getMenu(label); }
    default JMenuItem addMenuItem(String menuString, String label, Runnable object) { return gdmb().addMenuItem(menuString, label, object); }
    default JCheckBoxMenuItem addJCheckBoxMenuItem(String menuString, String label, Runnable object) { return gdmb().addJCheckBoxMenuItem(menuString, label, object); }
    default JMenuBar getMenuBar() { return gdmb().getMenuBar(); }
    default void addMenuSeparator(String string) { gdmb().addMenuSeparator(string); }
    default void setCurrentMenu(String string) { gdmb().setCurrentMenu(string); }
    default void addMenuItem(String string, Runnable object) { gdmb().addMenuItem(string, object); }
    default JMenuItem addMenuItem(String string, Runnable object, int key, int mask) { return gdmb().addMenuItem(string, object, key, mask); }
    default JCheckBoxMenuItem addJCheckBoxMenuItem(String string, Runnable object, int key, int mask) { return gdmb().addJCheckBoxMenuItem(string, object, key, mask); }
    default void addMenuSeparator() { gdmb().addMenuSeparator(); }
    default void setMenuBar(JMenuBar jMenuBar) { gdmb().setMenuBar(jMenuBar); }
}
