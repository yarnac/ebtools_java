package com.eb.aaswingsample;

import java.awt.*;
import java.io.Serial;

import javax.swing.*;
import javax.swing.border.*;

public class SwingDnDTest {


	public static void main(String[] args) {

		Runnable runnable = () -> {
            JFrame frame = new SwingDnDFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        };
		EventQueue.invokeLater(runnable);
	}
}

class SwingDnDFrame extends JFrame{

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 3105813336080565828L;

	public SwingDnDFrame(){
		setTitle("Drag and Drop");
		JTabbedPane tabbedPane = new JTabbedPane();

		String[] columns = new String[] {
	            "Id", "Name", "Hourly Rate", "Part Time"
	    };
	         
		Object[][] data = new Object[][] {
				{1, "Charles", 40.0, false},
				{2, "Jonathan", 70.0, false},
				{3, "Kingsley", 60.0, true},
		};
		
		JTable table = new JTable(data,columns);
		tabbedPane.addTab("Table",table);
		
		JList<String> list = new JList<>(new String[]{"Java","C++","C#","Python"});
		tabbedPane.addTab("List",list);
		
		JTree tree = new JTree();
		tabbedPane.addTab("Tree",tree);

		JFileChooser fileChooser = new JFileChooser();
		tabbedPane.addTab("File Chooser",fileChooser);

		JColorChooser colorChooser = new JColorChooser();
		tabbedPane.addTab("Color Chooser",colorChooser);

		final JTextArea textArea = new JTextArea(4, 40);
		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setBorder(new TitledBorder(new EtchedBorder(), "Drag text here."));

		JTextField textField = new JTextField("Drag color here");
		textField.setTransferHandler(new TransferHandler("background"));
		textArea.setTransferHandler(new TransferHandler("text"));

		list.setDragEnabled(true);
		table.setDragEnabled(true);
		tree.setDragEnabled(true);
		fileChooser.setDragEnabled(true);
		colorChooser.setDragEnabled(true);
		textField.setDragEnabled(true);

		add(tabbedPane, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);
		add(textField, BorderLayout.SOUTH);

		pack();

	}
}


