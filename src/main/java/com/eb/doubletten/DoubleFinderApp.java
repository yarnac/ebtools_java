package com.eb.doubletten;

import java.awt.*;

public class DoubleFinderApp {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    DoubleFinderFrame frame = new DoubleFinderFrame();
                    frame.initializeView();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
