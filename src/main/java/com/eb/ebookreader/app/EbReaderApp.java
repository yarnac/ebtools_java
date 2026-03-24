package com.eb.ebookreader.app;

import java.awt.EventQueue;

public class EbReaderApp {
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				InvokeWithColumns(2);
			}
		});
	}
	
	public static void InvokeWithColumns(int n)
	{
		try {
			EBReaderViewController ebReader = new EBReaderViewController(n);
			ebReader.getView().getFrame().setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
