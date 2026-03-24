package com.eb.ebookreader.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

public class TooltipFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextPane textPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TooltipFrame frame = new TooltipFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	public static void show(String arg) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TooltipFrame frame = new TooltipFrame();
					frame.setVisible(true);
					frame.textPane.setText(arg);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private static Rectangle bounds;

	/**
	 * Create the frame.
	 */
	public TooltipFrame() {
		initializeView();
		addWindowListener(this);
		addKeyListener(this, textPane);
		
	}


	private void addKeyListener(JFrame frame, JTextPane pane) {
		pane.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {				
				close(frame);
				
			}

			
			
			@Override
			public void keyReleased(KeyEvent e) {
				
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				
			}
		});
		
	}


	private void addWindowListener(JFrame frame) {
		addWindowListener(new WindowListener() {
			
			

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				if (bounds!=null)
					setBounds(bounds);
				
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
				close(frame);	
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				bounds = getBounds();
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void close(JFrame frame) {
		dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
	}
	
	
	private void initializeView() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(300, 300, 450, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		textPane = new JTextPane();
		textPane.setFont(new Font("Lucida Grande", Font.PLAIN, 20));
		
		JScrollPane pane = new JScrollPane();
		pane.setViewportView(textPane);
		contentPane.add(pane, BorderLayout.CENTER);
	}

}
