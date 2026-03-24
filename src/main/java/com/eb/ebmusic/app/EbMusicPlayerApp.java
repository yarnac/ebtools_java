package com.eb.ebmusic.app;

import com.eb.ebmusic.gui.EbMusicPlayerView;

import java.awt.*;

public class EbMusicPlayerApp {
	
	private static EbMusicPlayerViewController controller;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				startApplication();
			}		
		});
	}
			
			
	private static void startApplication() {
		
		try {			
			EbMusicPlayerView window;
			window = new EbMusicPlayerView();
			
			setController(new EbMusicPlayerViewController(window));
			window.getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public static EbMusicPlayerViewController getController() {
		return controller;
	}


	public static void setController(EbMusicPlayerViewController controller) {
		EbMusicPlayerApp.controller = controller;
	}

}
