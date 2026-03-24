package com.eb.vlc.gui;

// Chat GPR

import javax.swing.*;

public class JListUtils {

    public static void selectNextBlock(JList<?> list, int direction) {
        int size = list.getModel().getSize();

        if (size == 0) return;

        int selectionSize = list.getMaxSelectionIndex()-list.getMinSelectionIndex();

        int newMinselecktionIndex = list.getMinSelectionIndex() + selectionSize * direction + direction;
        int newMaxselectionIndex = list.getMaxSelectionIndex() + selectionSize * direction + direction;

        if (newMinselecktionIndex < 0)
        {
            newMinselecktionIndex = 0;
            newMaxselectionIndex = selectionSize;
        }
        if (newMaxselectionIndex >= size)
            newMaxselectionIndex = size-1;

        if (newMinselecktionIndex < newMaxselectionIndex) {
            list.setSelectionInterval(newMinselecktionIndex, newMaxselectionIndex);
        }

        list.ensureIndexIsVisible(newMinselecktionIndex);
        list.ensureIndexIsVisible(newMaxselectionIndex);
    }

    public static  void selectLineInterval(JList list, int n)
    {
        int start = list.getMinSelectionIndex() < 0 ? 0 : list.getMinSelectionIndex();
        int end = start + n - 1;
        list.setSelectionInterval(start, end);
    }
}