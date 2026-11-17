package com.eb.base.gui.persist;

import com.eb.base.extensions.JsonExtensions;
import com.eb.base.extensions.StringExtensions;
import com.eb.base.gui.EbSplitPanel;

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
    public void addComponentItem(Object newComponent, String key) {

        Object component = (newComponent instanceof EbSplitPanel ebSplitPanel)
                ? ebSplitPanel.getSplitPane()
                : newComponent;

        if (component instanceof JTextPane jTextPane)
            components.add(new ComponentItem<>(jTextPane, key, ()-> getJsonStringFromString(jTextPane.getText()), x->jTextPane.setText(getStringFromJsonString(x))));

        if (component instanceof JTextField jTextField)
            components.add(new ComponentItem<>(jTextField, key, ()-> getJsonStringFromString(jTextField.getText()), x->jTextField.setText(getStringFromJsonString(x))));

        else if (component instanceof JTextArea jTextArea)
            components.add(new ComponentItem<>(jTextArea, key, ()-> getJsonStringFromString(jTextArea.getText()), x->jTextArea.setText(getStringFromJsonString(x))));

        else if (component instanceof JComboBox jComboBox)
            components.add(new ComponentItem<>(jComboBox, key, () -> "" + jComboBox.getSelectedIndex(), x->jComboBox.setSelectedIndex(getInt(x))));

        else if (component instanceof JList jList)
            components.add(new ComponentItem<>(jList,key, () -> "" + jList.getSelectedIndex(), x->jList.setSelectedIndex(getInt(x))));

        else if (component instanceof JSplitPane jSplitPane)
        {
            components.add(new ComponentItem<>(jSplitPane, key + "_Distance", () -> "" + max(jSplitPane.getDividerLocation(),50), x->jSplitPane.setDividerLocation(max(getInt(x), 50))));
            components.add(new ComponentItem<>(jSplitPane, key + "_Orientation", () -> "" + jSplitPane.getOrientation(), x->jSplitPane.setOrientation(getInt(x))));
        }

        else if (component instanceof JScrollPane jScrollPane)
        {
            components.add(new ComponentItem<>(jScrollPane,key, () -> "" + getPointString(jScrollPane.getViewport().getViewPosition()), x->jScrollPane.getViewport().setViewPosition(getPoint(x))));
            Point pos = jScrollPane.getViewport().getViewPosition();
            jScrollPane.getViewport().setViewPosition(new Point(pos.x, pos.y));
        }
    }

    private String getJsonStringFromString(String text) {

        return JsonExtensions.jsonFromString(text);
    }

    private String getStringFromJsonString(String text) {
        return JsonExtensions.jsonToString(text);
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
