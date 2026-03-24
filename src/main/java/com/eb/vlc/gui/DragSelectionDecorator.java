package com.eb.vlc.gui;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class DragSelectionDecorator {

    public static void enableDragSelection(JList<?> list) {

        list.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());

                if (index >= 0) {
                    // erweitert die Auswahl bis zu diesem Index
                    list.addSelectionInterval(index, index);
                }
            }
        });

        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());

                if (index >= 0) {
                    // Startpunkt der Auswahl setzen
                    list.setSelectionInterval(index, index);
                }
            }
        });
    }

}
