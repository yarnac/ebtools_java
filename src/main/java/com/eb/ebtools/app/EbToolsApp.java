package com.eb.ebtools.app;

import com.eb.ebtools.gui.EbToolsView;

import javax.swing.*;
import java.awt.*;

public class EbToolsApp {
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
		      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		    } catch (Exception e) {
		      e.printStackTrace();
		    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					EbToolsView window = new EbToolsView();
					new EbToolsViewController(window);
					window.getFrame().setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
