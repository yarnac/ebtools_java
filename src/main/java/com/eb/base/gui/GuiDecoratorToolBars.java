package com.eb.base.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.System.out;

public class GuiDecoratorToolBars {
    private final Dictionary<String, JMenu> nameToMenuBarDictionary = new Hashtable<>();
    private final Dictionary<String, Container> nameToContainerDictionary = new Hashtable<>();
    private String currentMenuName;
    private Container currentContainer;
    private Component frame;

    GuiDecoratorToolBars(Component frame) {
        this.frame = frame;
    }


    GuiDecoratorToolBars() {
    }

    private Container fetchContainer(String toolbarName) {
        Container toolbar = nameToContainerDictionary.get(toolbarName);
        if (toolbar!=null)
            return toolbar;

        if (frame==null)
            return null;

        List<Component> toolbars = GuiUtil.findComponents(frame, x -> x instanceof JToolBar);
        for(Component component : toolbars)
            if (component.getName() != null) {
                String name = component.getName();
                nameToContainerDictionary.put(name, (Container) component);
            }
        return nameToContainerDictionary.get(toolbarName);
    }

    private void setCurrentContainer(Container currentContainer) {
        this.currentContainer  = currentContainer;
    }

    public void setCurrentContainer(String currentContainer) {
        setCurrentContainer(fetchContainer(currentContainer));
    }

    public void addContainer(String string, Container toolbarVokabeln) {
        nameToContainerDictionary.put(string, toolbarVokabeln);
        setCurrentContainer(toolbarVokabeln);
    }

    // === Verschobene Methoden für currentContainer ===

    public JButton addToolbarButton(String tooltipText, IC ic, ActionListener listener) {
        return getToolBarButton(tooltipText, listener, ic.getImageIcon(), "Image nicht gefunden: " + ic);
    }

    JButton addToolbarButton(String tooltipText, ICF icf, ActionListener listener) {

        return getToolBarButton(tooltipText, listener, icf.getImageIcon(), "Image nicht gefunden: " + icf);
    }

    private JButton getToolBarButton(String tooltipText, ActionListener listener, ImageIcon imageIcon, String s) {
        Container toolbar = currentContainer;
        JButton btn = new JButton();
        try {
            btn.setIcon(imageIcon);
        } catch (Exception e) {
            out.println(s);
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

    public JButton addOpenFileToolbarButton(String tooltipText, IC ic, String fileName) {
        Container toolbar = currentContainer;
        JButton btn = new JButton();
        try {
            btn.setIcon(ic.getImageIcon());
        } catch (Exception e) {
            out.println("Image nicht gefunden: " + ic);
        }
        btn.setToolTipText(tooltipText);
        toolbar.add(btn);
        btn.addActionListener(x -> openFile(fileName));

        if (!(toolbar instanceof JToolBar)) {
            toolbar.revalidate();
            toolbar.repaint();
        }
        return btn;
    }

    private void openFile(String fileName) {
        if (fileName==null || fileName.isEmpty())
            return;
        if (fileName.startsWith("http")) {
            BrowserLaunch.openBrowser(fileName);
        }
    }


    public JButton addOpenFileToolbarButton(String toolbarName, String tooltipText, ICF ic, String fileName) {
        Container toolbar = fetchContainer(toolbarName);
        JButton btn = new JButton();
        try {
            btn.setIcon(ic.getImageIcon());
        } catch (Exception e) {
            out.println("Image nicht gefunden: " + ic);
        }
        btn.setToolTipText(tooltipText);
        toolbar.add(btn);
        btn.addActionListener(x -> openFile(fileName));

        if (!(toolbar instanceof JToolBar)) {
            toolbar.revalidate();
            toolbar.repaint();
        }
        return btn;
    }

    public JToggleButton addToggleButton(String tooltipText, IC pinOrange, IC pinRed, Consumer<Boolean> listener) {
        Container toolbar = currentContainer;
        if (toolbar==null)
            return null;

        Image image = pinOrange.getCachedImage();
        Image selectedImage = pinRed.getCachedImage();

        JToggleButton btn = new JToggleButton("");

        btn.setIcon(new ImageIcon(image));
        btn.setSelectedIcon(new ImageIcon(selectedImage));

        btn.setToolTipText(tooltipText);
        toolbar.add(btn);
        btn.addActionListener(x -> listener.accept(btn.isSelected()));
        return btn;
    }

    public JToggleButton addToggleButton(String tooltipText, ICF pinOrange, ICF pinRed, Consumer<Boolean> listener) {
        Container toolbar = currentContainer;
        Image image = pinOrange.getCachedImage();
        Image selectedImage = pinRed.getCachedImage();

        JToggleButton btn = new JToggleButton("");

        btn.setIcon(new ImageIcon(image));
        btn.setSelectedIcon(new ImageIcon(selectedImage));

        btn.setToolTipText(tooltipText);
        toolbar.add(btn);
        btn.addActionListener(x -> listener.accept(btn.isSelected()));
        return btn;
    }

    public void addToolbarSeparator() {
        Container container = currentContainer;
        if (!(container instanceof JToolBar)) return;

        JSeparator seo = new JSeparator();
        seo.setMaximumSize(new Dimension(20,200));
        seo.setOpaque(true);
        seo.setSize(202,20);				
        seo.getInsets().left = 4;
        container.add(seo);
    }

    public <T> JComboBox<T> addToolbarComboBox(String tooltipText, List<T> elements, Consumer<T> consumer) {
        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setToolTipText(tooltipText);
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
        elements.forEach(model::addElement);
        comboBox.setModel(model);
        currentContainer.add(comboBox);
        comboBox.addActionListener(x -> consumer.accept((T)comboBox.getSelectedItem()));
        return comboBox;
    }

    public JProgressBar addToolbarProgressBar(String tooltipText) {
        JProgressBar progressBar = new JProgressBar();
        currentContainer.add(progressBar);
        return progressBar;
    }

    public <T> JComboBox<T> addToolbarComboBox(String tooltipText, T[] elements, Consumer<T> consumer) {
        if (currentContainer==null)
            return null;

        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setToolTipText(tooltipText);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>(elements);
        comboBox.setModel(model);
        currentContainer.add(comboBox);
        comboBox.addActionListener(x -> consumer.accept((T)comboBox.getSelectedItem()));
        comboBox.setVisible(true);
        return comboBox;
    }

    public void addTopMostButton(JFrame jFrame) {
        addToggleButton("Topmost", IC.PIN_ORANGE, IC.PIN_RED, x->switchTopMost(jFrame));
    }
    private void switchTopMost(JFrame jFrame) {
        jFrame.setAlwaysOnTop(!jFrame.isAlwaysOnTop());
    }
}