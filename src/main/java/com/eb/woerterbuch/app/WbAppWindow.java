package com.eb.woerterbuch.app;
import com.eb.woerterbuch.gui.WbPanelView;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenuBar;

public class WbAppWindow {

	private JFrame frmWrterbuch;
	private WbPanelView woerterbuchPanel;

	/**
	 * Launch the application.
	 */
	public static void amain(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WbAppWindow window = new WbAppWindow();
					window.frmWrterbuch.setVisible(true);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WbAppWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWrterbuch = new JFrame();
		frmWrterbuch.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				handleWindowClosing(e);
			}
		});
		frmWrterbuch.setTitle("W\u00F6rterbuch");
		frmWrterbuch.setBounds(100, 100, 615, 372);
		frmWrterbuch.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		JMenuBar menuBar = new JMenuBar();
		frmWrterbuch.setJMenuBar(menuBar);
		
			
		woerterbuchPanel  = new WbPanelView(menuBar);						
		
		
		
		frmWrterbuch.getContentPane().add(woerterbuchPanel, BorderLayout.CENTER);
	}

	ActionListener windowClosingActionListener;
	protected void handleWindowClosing(WindowEvent e) {
		if (windowClosingActionListener!=null)
			windowClosingActionListener.actionPerformed(new ActionEvent(this,0,"Closing"));
	}

	public JFrame getFrame() {
		// TODO eb 5.5 2017 Auto-generated method stub
		return frmWrterbuch;
	}

	public WbPanelView getWoerterbuchPanel() {
		// TODO eb 5.5 2017 Auto-generated method stub
		return woerterbuchPanel;
	}

}
