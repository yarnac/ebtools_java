package com.eb.woerterbuch.gui;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JToggleButton;


public class WordListDlgCtrl {
	private WordListDlg view;
	private GuiDecorator decorator;
	private List<String> woerter;
	private JToggleButton btnTopMost;
	
	
	
	
	public WordListDlgCtrl(List<String> list) {
		view = new WordListDlg();
		view.setVisible(true);
		setDecorator(view.getGuiDecorator());
		decorate();
		setWoerter(list);
		// TODO Auto-generated constructor stub
	}
	private void decorate() {
		btnTopMost = decorator.addToggleButton("Main", "TopMost", IC.PIN_ORANGE, IC.PIN_RED, x->topMostChanged());
		decorator.addToolbarButton("Main", "Delete", IC.DELETE_RED, x->deleteWord());
		decorator.addToolbarButton("Main", "Save Add", IC.SAVE_ADD, x->save());
	}
	private Object save() {
		// TODO Auto-generated method stub
		return null;
	}
	private void deleteWord() {
		String word = view.getLstWords().getSelectedValue();
		DefaultListModel<String> model = (DefaultListModel<String>) view.getLstWords().getModel();
		model.removeElement(word);
		woerter.remove(word);
		
	}
	private void topMostChanged() {
		view.setAlwaysOnTop(btnTopMost.isSelected());
	}
	public WordListDlg getView() {
		return view;
	}
	public void setView(WordListDlg view) {
		this.view = view;
	}
	public GuiDecorator getDecorator() {
		return decorator;
	}
	public void setDecorator(GuiDecorator decorator) {
		this.decorator = decorator;
	}
	public List<String> getWoerter() {
		return woerter;
	}
	public void setWoerter(List<String> woerter) {
		this.woerter = woerter;
		view.setWoerter(woerter);
	}

}
