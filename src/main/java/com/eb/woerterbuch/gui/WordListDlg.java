package com.eb.woerterbuch.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;

import com.eb.base.gui.GuiDecorator;
import com.eb.system.ClipboardAdapter;

public class WordListDlg extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JMenuBar menuBar;
	private JToolBar toolBar;
	private JList<String> lstWords;
	private JTextArea edText;
	private GuiDecorator guiDecorator;
	private WordListDlgCtrl wordListDlgCtrl;
	private ClipboardAdapter clipboardAdapter;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WordListDlg frame = new WordListDlg();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public WordListDlg() {
		
		initForm();
		setGuiDecorator(new GuiDecorator());
		getGuiDecorator().setMenuBar(menuBar);

		getGuiDecorator().addContainer("Main", toolBar);
		
		clipboardAdapter = new ClipboardAdapter();
		
		getLstWords().addListSelectionListener(x->wordSelected(x));
		
		decorate();
	}
	
	public void doNothing() {
		// TODO Auto-generated method stub		
	}

	private void wordSelected(ListSelectionEvent x) {
		if (getLstWords().getSelectedValue()!=null)			
			clipboardAdapter.setClipboardText(getLstWords().getSelectedValue());
	}

	private void decorate() {
		// TODO Auto-generated method stub
		
	}



	private void initForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
				
		setLstWords(new JList<>());
		JScrollPane jScrollPaneLst = new JScrollPane(getLstWords());

		contentPane.add(jScrollPaneLst, BorderLayout.CENTER);
		
		edText = new JTextArea();
		JScrollPane jScrollPane = new JScrollPane(edText);
				
		contentPane.add(jScrollPane, BorderLayout.SOUTH);
	}

	public void setWoerter(List<String> woerter) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		woerter.stream().forEach(x -> model.addElement(x));
		getLstWords().setModel(model);
	}

	public GuiDecorator getGuiDecorator() {
		return guiDecorator;
	}

	public void setGuiDecorator(GuiDecorator guiDecorator) {
		this.guiDecorator = guiDecorator;
	}

	public JList<String> getLstWords() {
		return lstWords;
	}

	public void setLstWords(JList<String> lstWords) {
		this.lstWords = lstWords;
	}

}
