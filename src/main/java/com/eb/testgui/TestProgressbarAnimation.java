package com.eb.testgui;

import javax.swing.*;
import java.awt.*;

public class TestProgressbarAnimation {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Java 21 Progressbar");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new FlowLayout());

        // 1. Unendliche Animation (Indeterminate)
        JProgressBar indeterminateBar = new JProgressBar();
        indeterminateBar.setIndeterminate(true); // Aktiviert die Animation

        // 2. Bestimmte Progressbar mit Text
        JProgressBar determinateBar = new JProgressBar(0, 100);
        determinateBar.setValue(0);
        determinateBar.setStringPainted(true); // Zeigt Prozenttext an

        frame.add(new JLabel("Unbekannte Dauer:"));
        frame.add(indeterminateBar);
        frame.add(new JLabel("Bekannte Dauer:"));
        frame.add(determinateBar);

        frame.setVisible(true);

        // Simulation eines Hintergrundprozesses mit virtuellen Threads (Java 21 Feature)

    }
}