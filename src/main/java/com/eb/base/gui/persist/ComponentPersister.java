package com.eb.base.gui.persist;

import com.eb.base.extensions.StringExtensions;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;

class ComponentPersister implements IComponentPersister {

    private IStringPersister persister;

    private List<ComponentItem> components = new ArrayList<>();


    public void setStringPersister(IStringPersister persister) {
        this.persister = persister;
    }

    public IStringPersister getStringPersister() {
        return persister;
    }

    @Override
    public void persistComponentItems() {
        for (ComponentItem item : components) {
            getStringPersister().setString(item.key, item.getString());
        }
        getStringPersister().commit();
    }

    @Override
    public void loadAndSetComponentItems() {
        for (ComponentItem item : components) {
            item.setString(getStringPersister().getString(item.getKey()));
            getStringPersister().setString(item.key, item.getString());
        }
    }

    @Override
    public void addComponentItem(Object component, String key) {

        if (component instanceof JTextPane) {
            JTextPane control = (JTextPane) component;
            components.add(new ComponentItem<>(control, key, ()-> getJsonStringFromString(control.getText()), x->control.setText(getStringFromJasonString(x))));
        }
        if (component instanceof JTextField) {
            JTextField control = (JTextField) component;
            components.add(new ComponentItem<>(control, key, ()-> getJsonStringFromString(control.getText()), x->control.setText(getStringFromJasonString(x))));
        }
        else if (component instanceof JTextArea) {
            JTextArea control = (JTextArea) component;
            components.add(new ComponentItem<>(control, key, ()-> getJsonStringFromString(control.getText()), x->control.setText(getStringFromJasonString(x))));
        }
        else if (component instanceof JSplitPane) {
            JSplitPane control = (JSplitPane) component;
            components.add(new ComponentItem<>(control, key + "_Distance", () -> "" + max(control.getDividerLocation(),50), x->control.setDividerLocation(max(getInt(x), 50))));
            components.add(new ComponentItem<>(control, key + "_Orientation", () -> "" + control.getOrientation(), x->control.setOrientation(getInt(x))));
        }
        else if (component instanceof JComboBox) {
            JComboBox control = (JComboBox) component;
            components.add(new ComponentItem<>(control, key, () -> "" + control.getSelectedIndex(), x->control.setSelectedIndex(getInt(x))));
        }
        else if (component instanceof JList) {
            JList control = (JList) component;
            components.add(new ComponentItem<>(control,key, () -> "" + control.getSelectedIndex(), x->control.setSelectedIndex(getInt(x))));
        }
        else if (component instanceof JScrollPane) {
            JScrollPane control = (JScrollPane) component;
            components.add(new ComponentItem<>(control,key, () -> "" + getPointString(control.getViewport().getViewPosition()), x->control.getViewport().setViewPosition(getPoint(x))));
            Point pos = control.getViewport().getViewPosition();
            control.getViewport().setViewPosition(new Point(pos.x, pos.y));
        }
    }

    private String getJsonStringFromString(String text) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(text);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getStringFromJasonString(String text) {
        if (StringExtensions.ebIsNilOrEmpty(text)) {
            return "";
        }
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(text, String.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Point getPoint(String x) {
        String[] parts = x.split(":");
        return new Point(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
    }

    private String getPointString(Point viewPosition) {
        return viewPosition.x + ":" + viewPosition.y;
    }

    private int getInt(String x) {

        if (StringExtensions.ebIsNilOrEmpty(x))
            return 0;
        return Integer.parseInt(x);
    }

    @Override
    public void removeComponentItem(Object component) {

    }

    @Override
    public Component getComponentItem(String key) {
        return null;
    }
}
