package com.eb.base.gui;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import com.eb.base.inifile.api.IniFile;

public class GuiPersister {

	public static void registerAndLoadStatus(JFrame frame, IniFile inifile, String sectionName) {
				
		loadStatus(frame, inifile, sectionName);					
		registerWindowEvents(frame, inifile, sectionName);
		
	}
	
	private static void saveStatus(JFrame frame, IniFile inifile, String sectionName) {
		
		Point location = frame.getLocation();
		inifile.setSectionValue(sectionName, "PosX", "" + (int) location.getX());
		inifile.setSectionValue(sectionName, "PosY", "" + (int) location.getY());
		
		Dimension size = frame.getSize();
		inifile.setSectionValue(sectionName, "SizeX", "" + (int) size.getWidth());
		inifile.setSectionValue(sectionName, "SizeY", "" + (int) size.getHeight());				
		inifile.Write();
	}
	
	private static void loadStatus(JFrame frame, IniFile inifile, String sectionName) {
		
		Point location = frame.getLocation();				
		int x = inifile.getSectionValueAsInteger(sectionName, "PosX", (int) location.getX());
		int y = inifile.getSectionValueAsInteger(sectionName, "PosY", (int) location.getY());
		
		Dimension size = frame.getSize();
		int width = inifile.getSectionValueAsInteger(sectionName, "SizeX", (int) + size.getWidth());
		int height = inifile.getSectionValueAsInteger(sectionName, "SizeY", (int) + size.getHeight());
		
		frame.setLocation(x, y);
		frame.setSize(width, height);
	}
	
	private static void registerWindowEvents(JFrame frame, IniFile inifile, String sectionName) {
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				saveStatus(frame, inifile, sectionName);
			}
			
			@Override
			public void windowClosed(WindowEvent e) {				
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	

}
