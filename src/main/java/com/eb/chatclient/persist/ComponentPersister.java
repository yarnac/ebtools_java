package com.eb.chatclient.persist;

import com.eb.base.extensions.StringExtensions;
import com.eb.base.inifile.api.IniFile;
import com.eb.chatclient.EbSplitPanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;

public class ComponentPersister implements IComponentPersister{

    private IStringPersister persister;

    public static ComponentPersister createIniFilePersister(IniFile iniFile)
    {
        ComponentPersister persister = new ComponentPersister();
        persister.setStringPersister(new IniFilePersister(iniFile));
        return persister;
    }

    private List<ComponentItem> components = new ArrayList<>();


    @Override
    public void setStringPersister(IStringPersister persister) {
        this.persister = persister;
    }

    @Override
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
            components.add(new ComponentItem<>(control, key, ()->control.getText(), x->control.setText(x)));
        }
        if (component instanceof JTextField) {
            JTextField control = (JTextField) component;
            components.add(new ComponentItem<>(control, key, ()->control.getText(), x->control.setText(x)));
        }
        else if (component instanceof JTextArea) {
            JTextArea control = (JTextArea) component;
            components.add(new ComponentItem<>(control, key, ()->control.getText(), x->control.setText(x)));
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
