package com.eb.woerterbuch.app;

import com.eb.woerterbuch.gobj.WbManager;
import com.eb.woerterbuch.gui.WbPanelView;

import javax.swing.*;
import java.awt.*;

public class WbApp {

	
	public static void main(String[] args) {
		EventQueue.invokeLater(()->open());				
	}

	private static void open()
	{
		new WbApp();
	}



	private WbPanelCtrl wbCtrl;
	private JFrame woerterbuchFrame;
	private WbAppWindow woerterbuchWindow;
	private WbPanelView woerterbuchPanel;
	private WbManager wbManager;
	
	public WbApp() {		
		initializeWoerterbuchFields();
		initializeSubcontroller();
		setInitialLocationAndSize();
		registerSaveOnClose();
	
		getWoerterbuchFrame().setVisible(true);			
	}

	private void initializeWoerterbuchFields() {
		setWoerterbuchWindow(new WbAppWindow());
		woerterbuchPanel = getWoerterbuchWindow().getWoerterbuchPanel();
		setWoerterbuchFrame(getWoerterbuchWindow().getFrame());
	}

	private void initializeSubcontroller() {
		setWbCtrl(new WbPanelCtrl());
		getWbCtrl().setView(woerterbuchPanel);
		getWbCtrl().setTopMostHandler(x->getWoerterbuchFrame().setAlwaysOnTop(x));
		
		setWbManager(getWbCtrl().getManager());
		
		getWbCtrl().setRequestFocusActionListener(x->{
			if (!getWoerterbuchFrame().isAlwaysOnTop())
			{
				getWoerterbuchFrame().setAlwaysOnTop(true);
				getWoerterbuchFrame().setAlwaysOnTop(false);
			}});
	}

	private void setInitialLocationAndSize() {
		Point location = getWbManager().getWindowLocation();
		Dimension size = getWbManager().getWindowSize();
		getWoerterbuchFrame().setLocation(location);
		getWoerterbuchFrame().setSize(size);
	}

	private void registerSaveOnClose() {
		
		getWoerterbuchWindow().windowClosingActionListener = (e)->{
			getWbManager().setWindowLocation(getWoerterbuchFrame().getLocation());
			getWbManager().setWindowSize(getWoerterbuchFrame().getSize());
			getWbManager().save();
		};
	}

	public WbManager getWbManager() {
		return wbManager;
	}

	public void setWbManager(WbManager wbManager) {
		this.wbManager = wbManager;
	}

	public JFrame getWoerterbuchFrame() {
		return woerterbuchFrame;
	}

	public void setWoerterbuchFrame(JFrame woerterbuchFrame) {
		this.woerterbuchFrame = woerterbuchFrame;
	}

	public WbAppWindow getWoerterbuchWindow() {
		return woerterbuchWindow;
	}

	public void setWoerterbuchWindow(WbAppWindow woerterbuchWindow) {
		this.woerterbuchWindow = woerterbuchWindow;
	}

	public WbPanelCtrl getWbCtrl() {
		return wbCtrl;
	}

	public void setWbCtrl(WbPanelCtrl wbCtrl) {
		this.wbCtrl = wbCtrl;
	}


	
}
