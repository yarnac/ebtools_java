package com.eb.system;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClipboardAdapter implements ClipboardOwner {
	ActionListener actionListener;
	String text;
	
	public ClipboardAdapter(ActionListener newActionListener)
	{
		actionListener = newActionListener;
		new Thread(new Runnable() {
			public void run() {
				try {					
					Initialize();					
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}			
		}).start();	
	}
		
	public ClipboardAdapter() {
		// TODO Auto-generated constructor stub
	}

	String oldtext;
	private void Initialize() {
		// TODO eb 5.5 2017 Auto-generated method stub
			
		while(true)
		{
			try {
				String textFromClipboard = getTextFromClipboard();
				setText(textFromClipboard);
				if (textFromClipboard!=null && !textFromClipboard.equals(oldtext))
				{
					oldtext = getText();
					
					if (textFromClipboard.length()<=40 
							&& !textFromClipboard.contains("."))
					{
						if (actionListener!=null)
							actionListener.actionPerformed(new ActionEvent(this, 0, null));
					}					
				}	
				
			}
			catch (Exception e) {	
				e.toString();
			}
			try {
				Thread.sleep(250);
			}
			catch (InterruptedException e) {				
			}
		}
	}
									
	public void setClipboardText(String text)
	{
		Transferable contents = new Transferable() {
			
			@Override
			public boolean isDataFlavorSupported(DataFlavor flavor) {
				if (flavor.equals(DataFlavor.stringFlavor))
					return true;	
				return false;
			}
			
			@Override
			public DataFlavor[] getTransferDataFlavors() {
				// TODO Auto-generated method stub
				return new DataFlavor[]{DataFlavor.stringFlavor};
			}
			
			@Override
			public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
				// TODO Auto-generated method stub
				return text;
			}
		};
				
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, this);
	}
	
	private String getTextFromClipboard() {
		// TODO eb 5.5 2017 Auto-generated method stub
		try {			
			String data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
			if (data==null)
				return "";
						
			return data.trim();
		}
		catch (HeadlessException e) {
			// TODO eb 5.5 2017 eventuell cleanup
			
		}
		catch (UnsupportedFlavorException e) { 
		}
		catch (IOException e) {
			
		}
		
		return null;
	}
	
	public String getText() {
		if (text==null)
			return "";
		return text.trim();
	}

	private void setText(String text) {
		this.text = text;
	}

	@Override
	public void lostOwnership(Clipboard clipboard, Transferable contents) {
		// TODO Auto-generated method stub
		
	}

}
