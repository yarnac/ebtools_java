package com.eb.system;

import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextChangedHandlerUtil {
		
	public static void addTextChangedHandler(JTextField edit, Runnable textChanged) {
		
		edit.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged.run();										
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged.run();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				textChanged.run();
			}
		});
		
	}
	
	public static void addTextChangedHandler(JTextArea edit, Runnable textChanged) {
		
		edit.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				textChanged.run();										
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				textChanged.run();
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				textChanged.run();
			}
		});
		
	}

}
