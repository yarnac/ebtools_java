package com.eb.base.gui.decorator;

import com.eb.base.gui.IC;
import com.eb.base.gui.ICF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

interface GuiDecoratorToolBarsMethods {

    GuiDecoratorToolBars getToolBarDecorator();

    private GuiDecoratorToolBars gtbd(){return getToolBarDecorator();}

    default JButton addToolbarButton(String tooltipText, ICF ic, ActionListener listener) { return gtbd().addToolbarButton(tooltipText, ic, listener); }
    default JButton addToolbarButton(String tooltipText, IC ic, ActionListener listener) { return gtbd().addToolbarButton(tooltipText, ic, listener); }
    default JButton addOpenFileToolbarButton(String tooltipText, IC ic, String fileName) { return gtbd().addOpenFileToolbarButton(tooltipText, ic, fileName); }
    default JButton addOpenFileToolbarButton(String toolbarName, String tooltipText, ICF ic, String fileName) { return gtbd().addOpenFileToolbarButton(toolbarName, tooltipText, ic, fileName); }
    default void addContainer(String string, Container toolbarVokabeln) { gtbd().addContainer(string, toolbarVokabeln); }
    default JToggleButton addToggleButton(String tooltipText, IC pinOrange, IC pinRed, Consumer<Boolean> listener) { return gtbd().addToggleButton(tooltipText, pinOrange, pinRed, listener); }
    default <T> JComboBox<T> addToolbarComboBox(String requests, List<T> llmRequests, Consumer<T> consumer) { return gtbd().addToolbarComboBox(requests, llmRequests, consumer); }
    default <T> JComboBox<T> addToolbarComboBox(String requests, T[] llmRequests, Consumer<T> consumer) { return gtbd().addToolbarComboBox(requests, llmRequests, consumer); }
    default JProgressBar addToolbarProgressBar(String huhu) { return gtbd().addToolbarProgressBar(huhu); }
    default void addToolbarSeparator() { gtbd().addToolbarSeparator(); }
    default void setCurrentContainer(String main) { gtbd().setCurrentContainer(main); }
    default void addTopMostButton(JFrame jFrame) { gtbd().addTopMostButton(jFrame); }
}