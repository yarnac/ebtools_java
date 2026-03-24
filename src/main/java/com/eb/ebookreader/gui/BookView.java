package com.eb.ebookreader.gui;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class BookView {
	private JScrollPane pane;
	private JTextPane edit;
	
	
	public JScrollPane getPane() {
		return pane;
	}
	public void setPane(JScrollPane pane) {
		this.pane = pane;
	}
	public JTextPane getEdit() {
		return edit;
	}
	public void setEdit(JTextPane edit) {
		this.edit = edit;
	}
}
