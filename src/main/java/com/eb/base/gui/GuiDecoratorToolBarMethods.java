package com.eb.base.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.function.Consumer;

interface GuiDecoratorToolBarMethods {

    GuiDecoratorToolBars getToolBarDecorator();


    default JButton addToolbarButton(String tooltipText, ICF ic, ActionListener listener) {
        return getToolBarDecorator().addToolbarButton(tooltipText, ic, listener);
    }

    default JButton addToolbarButton(String tooltipText, IC ic, ActionListener listener) {
        return getToolBarDecorator().addToolbarButton(tooltipText, ic, listener);
    }

    default JButton addOpenFileToolbarButton(String tooltipText, IC ic, String fileName) {
        return getToolBarDecorator().addOpenFileToolbarButton(tooltipText, ic, fileName);
    }

    default JButton addOpenFileToolbarButton(String toolbarName, String tooltipText, ICF ic, String fileName) {
        return getToolBarDecorator().addOpenFileToolbarButton(toolbarName, tooltipText, ic, fileName);
    }

    default void addContainer(String string, Container toolbarVokabeln) {
        getToolBarDecorator().addContainer(string, toolbarVokabeln);
    }

    default JToggleButton addToggleButton(String tooltipText, IC pinOrange, IC pinRed, Consumer<Boolean> listener) {
        return getToolBarDecorator().addToggleButton(tooltipText, pinOrange, pinRed, listener);
    }

    default <T> JComboBox<T> addToolbarComboBox(String requests, List<T> llmRequests, Consumer<T> consumer) {
        return getToolBarDecorator().addToolbarComboBox(requests, llmRequests, consumer);
    }

    default <T> JComboBox<T> addToolbarComboBox(String requests, T[] llmRequests, Consumer<T> consumer) {
        return getToolBarDecorator().addToolbarComboBox(requests, llmRequests, consumer);
    }

    default JProgressBar addToolbarProgressBar(String huhu) {
        return getToolBarDecorator().addToolbarProgressBar(huhu);
    }

    default void addToolbarSeparator() {
        getToolBarDecorator().addToolbarSeparator();
    }

    default void setCurrentContainer(String main) {
        getToolBarDecorator().setCurrentContainer(main);
    }

    default void addTopMostButton(JFrame jFrame) {
        getToolBarDecorator().addTopMostButton(jFrame);
    }
}
