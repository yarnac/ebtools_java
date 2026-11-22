package com.eb.base.gui.decorator;

import com.eb.base.gui.BrowserLaunch;
import com.eb.base.gui.GuiUtil;
import com.eb.base.gui.IC;
import com.eb.base.gui.ICF;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.System.out;

public class GuiDecoratorToolBars {
    private final Map<String, Container> nameToContainerDictionary = new HashMap<>();
    private Container currentContainer;
    @Setter
    private Component frame;

    GuiDecoratorToolBars(Component frame) {
        this.frame = frame;
    }


    public GuiDecoratorToolBars() {
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

    public JButton addToolbarButton(String tooltipText, ICF icf, ActionListener listener) {

        return getToolBarButton(tooltipText, listener, icf.getImageIcon(), "Image nicht gefunden: " + icf);
    }

    private JButton getToolBarButton(String tooltipText, ActionListener listener, ImageIcon imageIcon, String s) {

        checkCurrentContainer();

        JButton btn = new JButton();
        try {
            btn.setIcon(imageIcon);
        } catch (Exception e) {
            out.println(s);
        }
        btn.setToolTipText(tooltipText);
        currentContainer.add(btn);

        btn.addActionListener(e -> {
            SwingUtilities.invokeLater(()->listener.actionPerformed(e));
        });

        checkAndInvalidateContainerIfNecessary();
        return btn;
    }

    private void checkCurrentContainer() {
        if (currentContainer == null) throw new IllegalStateException("Current container not set.");
    }

    public JButton addOpenFileToolbarButton(String tooltipText, IC ic, String fileName) {

        checkCurrentContainer();
        JButton btn = new JButton();
        try {
            btn.setIcon(ic.getImageIcon());
        } catch (Exception e) {
            out.println("Image nicht gefunden: " + ic);
        }
        btn.setToolTipText(tooltipText);
        currentContainer.add(btn);
        btn.addActionListener(x -> {
            SwingUtilities.invokeLater(()->openFile(fileName));
        });

        checkAndInvalidateContainerIfNecessary();
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

        checkCurrentContainer();

        JButton btn = new JButton();
        try {
            btn.setIcon(ic.getImageIcon());
        } catch (Exception e) {
            out.println("Image nicht gefunden: " + ic);
        }
        btn.setToolTipText(tooltipText);
        currentContainer.add(btn);
        btn.addActionListener(x -> {
            SwingUtilities.invokeLater(()->openFile(fileName));
        });
        checkAndInvalidateContainerIfNecessary();

        return btn;
    }

    private void checkAndInvalidateContainerIfNecessary() {
        if (!(currentContainer instanceof JToolBar)) {
            currentContainer.revalidate();
            currentContainer.repaint();
        }
    }

    public JToggleButton addToggleButton(String tooltipText, IC pinOrange, IC pinRed, Consumer<Boolean> listener) {

        checkCurrentContainer();

        Image image = pinOrange.getCachedImage();
        Image selectedImage = pinRed.getCachedImage();

        JToggleButton btn = new JToggleButton("");

        btn.setIcon(new ImageIcon(image));
        btn.setSelectedIcon(new ImageIcon(selectedImage));

        btn.setToolTipText(tooltipText);
        currentContainer.add(btn);

        btn.addActionListener(x -> {
            SwingUtilities.invokeLater(()->listener.accept(btn.isSelected()));
        });

        checkAndInvalidateContainerIfNecessary();
        return btn;
    }

    public JToggleButton addToggleButton(String tooltipText, ICF pinOrange, ICF pinRed, Consumer<Boolean> listener) {

        checkCurrentContainer();

        Image image = pinOrange.getCachedImage();
        Image selectedImage = pinRed.getCachedImage();

        JToggleButton btn = new JToggleButton("");

        btn.setIcon(new ImageIcon(image));
        btn.setSelectedIcon(new ImageIcon(selectedImage));

        btn.setToolTipText(tooltipText);
        currentContainer.add(btn);

        btn.addActionListener(x -> {
            SwingUtilities.invokeLater(()->listener.accept(btn.isSelected()));
        });

        checkAndInvalidateContainerIfNecessary();

        return btn;
    }

    public void addToolbarSeparator() {

        checkCurrentContainer();

        if (!(currentContainer instanceof JToolBar)) return;

        JSeparator seo = new JSeparator();
        seo.setMaximumSize(new Dimension(20,200));
        seo.setOpaque(true);
        seo.setSize(202,20);				
        seo.getInsets().left = 4;
        currentContainer.add(seo);

        checkAndInvalidateContainerIfNecessary();
    }

    public <T> JComboBox<T> addToolbarComboBox(String tooltipText, List<T> elements, Consumer<T> consumer) {

        checkCurrentContainer();

        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setToolTipText(tooltipText);
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>();
        elements.forEach(model::addElement);
        comboBox.setModel(model);
        currentContainer.add(comboBox);

        comboBox.addActionListener(x -> {
            SwingUtilities.invokeLater(()->consumer.accept((T)comboBox.getSelectedItem()));
        });



        checkAndInvalidateContainerIfNecessary();
        return comboBox;
    }

    public JProgressBar addToolbarProgressBar(String tooltipText) {

        checkCurrentContainer();

        JProgressBar progressBar = new JProgressBar();
        currentContainer.add(progressBar);

        checkAndInvalidateContainerIfNecessary();

        return progressBar;
    }

    public <T> JComboBox<T> addToolbarComboBox(String tooltipText, T[] elements, Consumer<T> consumer) {

        checkCurrentContainer();

        JComboBox<T> comboBox = new JComboBox<>();
        comboBox.setToolTipText(tooltipText);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        DefaultComboBoxModel<T> model = new DefaultComboBoxModel<>(elements);
        comboBox.setModel(model);
        currentContainer.add(comboBox);
        comboBox.addActionListener(x -> consumer.accept((T)comboBox.getSelectedItem()));
        comboBox.setVisible(true);

        checkAndInvalidateContainerIfNecessary();

        return comboBox;
    }

    public void addTopMostButton(JFrame jFrame) {
        addToggleButton("Topmost", IC.PIN_ORANGE, IC.PIN_RED, x->switchTopMost(jFrame));
    }
    private void switchTopMost(JFrame jFrame) {
        jFrame.setAlwaysOnTop(!jFrame.isAlwaysOnTop());
    }
}