package com.eb.doubletten;

import java.awt.event.*;

import javax.swing.JFrame;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

public abstract class AbstrPanelCtrl {
	private IniFile iniFile;
	private JFrame applicationFrame;
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
			frame.addWindowFocusListener(new WindowAdapter() {

				@Override
				public void windowGainedFocus(WindowEvent e) {
					if (onActivate!=null)
						onActivate.run();
					
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
