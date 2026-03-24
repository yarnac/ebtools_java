package com.eb.doubletten;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

public abstract class AbstrPanelCtrl {
	private IniFile iniFile;
	private JFrame applicationFrame;
	protected boolean started;
	private Runnable onActivate; 

	public IniFile getIniFile() {
		if (iniFile==null)
		{
			iniFile = IniFileProvider.createIniFile(getIniFileName());
		}
			
			
		return iniFile;
	}

	protected abstract String getIniFileName(); 

	public void setIniFile(IniFile iniFile) {
		this.iniFile = iniFile;
	}

	public JFrame getApplicationFrame() {
		return applicationFrame;
	}

	public void setApplicationFrame(JFrame frame) {
		applicationFrame = frame;
		
		
		
	
		if (frame!=null)
		{
			frame.addWindowFocusListener(new WindowFocusListener() {
				
				@Override
				public void windowLostFocus(WindowEvent e) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void windowGainedFocus(WindowEvent e) {
					// TODO Auto-generated method stub
					if (onActivate!=null)
						onActivate.run();
					
				}
			});
			
			frame.addComponentListener(new ComponentListener() {
				
				@Override
				public void componentShown(ComponentEvent e) {

					
				}
				
				@Override
				public void componentResized(ComponentEvent e) {
					
				}
				
				@Override
				public void componentMoved(ComponentEvent e) {

				}

				@Override
				public void componentHidden(ComponentEvent e) {
					// TODO Auto-generated method stub
					
				}
								
			});
			
		}
		
	}
	public Runnable getOnActivate() {
		return onActivate;
	}
	public void setOnActivate(Runnable onActivate) {
		this.onActivate = onActivate;
	} 

}
