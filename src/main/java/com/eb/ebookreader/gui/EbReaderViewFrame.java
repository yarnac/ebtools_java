package com.eb.ebookreader.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;

public class EbReaderViewFrame extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int columns;
	

	public EbReaderViewFrame(int newColumns) {
		columns = newColumns;
		initialize();				
	}
	
	
	
	private JToolBar tbUpper;
	
	private List<BookView> bookViews = new ArrayList<>();

	private void initialize() {						
		setBounds(100, 100, 517, 781);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("EB Reader");
		
		createViewColumns(columns);
	}

	public void createViewColumns(int cols) {
		
		columns = cols;
		
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {100, 100, 100, 0};
		gridBagLayout.rowHeights = new int[] {0, 0};
		if (columns==3)
			gridBagLayout.columnWeights = new double[]{1.0, 1.8, 1.8};
		else
			gridBagLayout.columnWeights = new double[]{1.0, 1.8};
		gridBagLayout.rowWeights = new double[]{0.01, 1.0};
		getContentPane().setLayout(gridBagLayout);
		
			
		setTbUpper(new JToolBar());
		
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.fill = GridBagConstraints.HORIZONTAL;
		gbc_toolBar.gridwidth = 3;
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		getContentPane().add(getTbUpper(), gbc_toolBar);
		
		while(getBookViews().size()>0 && false)
		{			
			BookView view = getBookViews().get(0);
			getContentPane().remove(view.getPane());
			getBookViews().remove(0);
		}
		
		for (int i=0; i<columns; i++)
		{
			BookView bv = new BookView();
			getBookViews().add(bv);
			bv.setEdit(new JTextPane());
			bv.getEdit().setFont(new Font("Lucida Grande", Font.PLAIN, 20));
			
			GridBagConstraints gbc_edit = new GridBagConstraints();
			gbc_edit.weighty = 1.0;
			gbc_edit.weightx = 2.0;
			gbc_edit.fill = GridBagConstraints.BOTH;
			gbc_edit.gridx = i;
			gbc_edit.gridy = 1;
			
			bv.setPane(new JScrollPane());
			bv.getPane().setViewportView(bv.getEdit());
			
			getContentPane().add(bv.getPane(), gbc_edit);			
		}
	}

	public JToolBar getTbUpper() {
		return tbUpper;
	}

	public void setTbUpper(JToolBar tbUpper) {
		this.tbUpper = tbUpper;
	}
	
	public List<BookView> getBookViews() {
		return bookViews;
	}

	public void setBookViews(List<BookView> bookViews) {
		this.bookViews = bookViews;
	}
}
