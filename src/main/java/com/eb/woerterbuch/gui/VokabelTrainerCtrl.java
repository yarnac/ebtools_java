package com.eb.woerterbuch.gui;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.woerterbuch.app.WbApp;

import java.awt.EventQueue;

public class VokabelTrainerCtrl {
	
	private VokabelTrainerView trainer;
	
	
	public VokabelTrainerCtrl(VokabelTrainerView t)
	{
		trainer = t;
	}

	public VokabelTrainerView getTrainer() {
		return trainer;
	}

	public void setTrainer(VokabelTrainerView trainer) {
		this.trainer = trainer;
	}

	public void start() {				 		;
		GuiDecorator decorator = new GuiDecorator();
		decorator.setFrame(trainer);
		decorator.addMenuItem("Datei", "Huhu", ()->{});
		decorator.addToolbarButton("Main", "Leer", IC.BLANK_DOC, (x)->openWoerterbuch());
	}
	
	private void openWoerterbuch() {
		
		new WbApp();
	
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			

			public void run() {
				try {
					VokabelTrainerView window = new VokabelTrainerView();
					window.setVisible(true);
					window.setCtrl(new VokabelTrainerCtrl(window));
					window.getCtrl().start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}
