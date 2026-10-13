package com.eb.aaswingsample;

import java.awt.EventQueue;

import javax.swing.JFrame;

/**
 * @author ab
 *
 */
public class SwingJtreeDnDExample {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Runnable runnable = new Runnable(){
			public void run(){
				JFrame frame = new SwingDnDExampleFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setVisible(true);
			}
		};
		EventQueue.invokeLater(runnable);
	}
}


