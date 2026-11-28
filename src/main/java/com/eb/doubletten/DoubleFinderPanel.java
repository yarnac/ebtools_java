package com.eb.doubletten;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.Serial;
import java.util.function.Consumer;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.border.LineBorder;

import com.eb.baseswing.gui.decorator.GuiDecorator;

public class DoubleFinderPanel extends JPanel {
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;
	private JList<Doublette> lstDoubletten;
	private JComboBox<String> comboBox;
	private JToolBar toolBar;
	
	private Consumer<String> selectedDirectoryAction;
	private Consumer<Doublette> selectedDoubletteAction;
	private Consumer<Doublette> selectedDoubletteFileAction;
	JScrollPane scrollPane;
	private JTextField textFieldPos;
	private JTextField textFieldNeg;


	
	
	@Override
	public Dimension getMinimumSize() {

		return new Dimension(300, 20);
	}
	
	@Override
	public Dimension getPreferredSize() {

		if (getParent()!=null)
			return new Dimension(200, getParent().getHeight());
		return new Dimension(300, 600);
	}
	
	@Override
	public Dimension getMaximumSize() {

		return new Dimension(1000,  1000);
	}

	/**
	 * Create the panel.
	 */
	public DoubleFinderPanel() {		
		setBorder(new LineBorder(new Color(0, 0, 0)));
		setLayout(null);

		toolBar = new JToolBar();
		toolBar.setBounds(6, 6, 288, 40);
		add(toolBar);

		comboBox = new JComboBox<String>();
		comboBox.setBounds(1, 48, 100, 27);
		add(comboBox);

		textFieldPos = new JTextField();
		add(textFieldPos);
		textFieldPos.setBounds(1, 82, 300, 27);

		textFieldNeg = new JTextField();
		add(textFieldNeg);
		textFieldNeg.setBounds(310, 82, 300, 27);
		
		setLstDoubletten(new JList<Doublette>());
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(getLstDoubletten());
		scrollPane.setBounds(6, 150, 288, 234);
		
		add(scrollPane);
	}
	
	public void register()
	{
		comboBox.addActionListener(x->comboBoxChanged((String)comboBox.getSelectedItem()));
		lstDoubletten.addListSelectionListener(x->doubletteChanged(lstDoubletten.getSelectedValue()));
		
		GuiDecorator decorator = new GuiDecorator();
		decorator.toolBars().addContainer("main", getToolBar());
		
		this.addComponentListener(new ComponentAdapter() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				setSize(getSize());
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				setSize(getSize());
			}
		});
	}
	
	private Object doubletteChanged(Doublette selectedValue) {
		if (getSelectedDoubletteAction()!=null)
			getSelectedDoubletteAction().accept(selectedValue);
		return null;
	}

	private void comboBoxChanged(String selectedItem) {
		if (getSelectedDirectoryAction()!=null)
			getSelectedDirectoryAction().accept(selectedItem);
	}

	@Override
	public void setSize(int width, int height) {

		super.setSize(width, height);
		comboBox.setSize(width-10, comboBox.getHeight());
		scrollPane.setSize(width-10, height-40-30);
	}
	
	@Override
	public void setSize(Dimension d) {
		super.setSize(d);
	}

	
	
	public JList<Doublette> getLstDoubletten() {
		return lstDoubletten;
	}

	public void setLstDoubletten(JList<Doublette> lstDoubletten) {
		this.lstDoubletten = lstDoubletten;
		lstDoubletten.setBounds(1, 56, 293, 242);
	}
	
	public JComboBox<String> getComboBox() {
		return comboBox;
	}

	public void setComboBox(JComboBox<String> comboBox) {
		this.comboBox = comboBox;
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(JToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public Consumer<String> getSelectedDirectoryAction() {
		return selectedDirectoryAction;
	}
	
	public void setSelectedDirectoryAction(Consumer<String> action) {
		selectedDirectoryAction = action;
	}

	public Consumer<Doublette> getSelectedDoubletteAction() {
		return selectedDoubletteAction;
	}

	public void setSelectedDoubletteAction(Consumer<Doublette> selectedDoubletteAction) {
		this.selectedDoubletteAction = selectedDoubletteAction;
	}

	public Consumer<Doublette> getSelectedDoubletteFileAction() {
		return selectedDoubletteFileAction;
	}

	public void setSelectedDoubletteFileAction(Consumer<Doublette> selectedDoubletteFileAction) {
		this.selectedDoubletteFileAction = selectedDoubletteFileAction;
	}
	
	

	public void setKeysAction(Consumer<KeyEvent> consumer) {
		KeyListener textFieldKeysAction = getTextFieldKeysAction(consumer);
		textFieldPos.addKeyListener(textFieldKeysAction);
		textFieldNeg.addKeyListener(textFieldKeysAction);
		getLstDoubletten().addKeyListener(getDoublettenKeyActions(consumer));
	}
	
	private KeyListener getDoublettenKeyActions(Consumer<KeyEvent> consumer) {
		return new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				consumer.accept(e);
				
			}
		};
	}

	private KeyListener getTextFieldKeysAction(Consumer<KeyEvent> consumer) {
		return new  KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode()==10)
					consumer.accept(e);
				
			}
		};
	}

	public void setIndex(int n) {
		if (getLstDoubletten().getModel().getSize()<=n)
			return;
		getLstDoubletten().setSelectedIndex(n);
		getLstDoubletten().ensureIndexIsVisible(n+20);
		
	}

	public String getFilterPos() {
		return textFieldPos.getText();
	}
	
	public String getFilterNeg() {
		return textFieldNeg.getText();
	}

	
}
