package com.eb.aaswingsample;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

public class SwingDnDExampleFrame extends JFrame {
	
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -224525322599947463L;

	public SwingDnDExampleFrame(){

		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(200);
		
		JTree driveTree = new JTree(getTreeStructure());
		final JEditorPane content = new JEditorPane();
		JScrollPane treeContent = new JScrollPane(driveTree);
		JScrollPane scrollContent = new JScrollPane(content);

		driveTree.setDragEnabled(true);
		driveTree.setTransferHandler(new FileTransferHandler());
		
		JPanel panel = new JPanel(new BorderLayout());
		JTextField fileName = new JTextField();
		JList<String> list = new JList<>(new String[]{"Asia","Europe","America","Africa"});
		list.setDragEnabled(true);
				
		panel.add(fileName,BorderLayout.NORTH);
		panel.add(scrollContent, BorderLayout.CENTER);
		panel.add(list, BorderLayout.EAST);
				
		splitPane.setLeftComponent(treeContent);
		splitPane.setRightComponent(panel);
		
		content.setDropTarget(new DropTarget() {
			/**
			 * 
			 */
			@Serial
			private static final long serialVersionUID = -6418118605479053389L;

			@SuppressWarnings("unchecked")
			public synchronized void drop(DropTargetDropEvent evt) {
				try {
					evt.acceptDrop(DnDConstants.ACTION_COPY);
					List<File> droppedFiles = (List<File>) evt
					.getTransferable().getTransferData(
							DataFlavor.javaFileListFlavor);
					if(droppedFiles.size() > 1){
						JOptionPane.showMessageDialog(content, "Sorry...can't handle more than one files together.");
					}
					else{
						File droppedFile = droppedFiles.getFirst();
						if(droppedFile.getName().endsWith(".txt")){
							char[] contentBytes = readFile(droppedFile);
							if(contentBytes == null){
								JOptionPane.showMessageDialog(content, "Sorry...file size is too long.");
							}
							else if(contentBytes.length == 0){
								JOptionPane.showMessageDialog(content, "Sorry...file is empty.");
							}
							else{
								content.setText(new String(contentBytes));
							}
						}
						else{
							JOptionPane.showMessageDialog(content, "Sorry...not a text file.");
						}

					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});

		add(splitPane);
		pack();
	}

	private char[] readFile(File inputFile){
		BufferedReader inputReader;
		char[] content = null;
		long availableHeap = Runtime.getRuntime().freeMemory();
		long fileSize = inputFile.length();
		try {
			if(fileSize <= availableHeap){
				content = new char[(int)inputFile.length()];
				inputReader = new BufferedReader(new FileReader(inputFile)); 

				inputReader.read(content);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

	private ArrayList<File> getAllDirectories(File file){

		ArrayList<File> directories = new ArrayList<>();
		File[] allSub = file.listFiles();
		if(allSub != null){
			for(File sub: allSub){
				if(sub.isDirectory()){
					directories.add(sub);
				}
			}
		}
		return directories;
	}

	private ArrayList<File> getAllTXTs(File directory){
		ArrayList<File> pdfs = new ArrayList<>();
		File[] allSub = directory.listFiles();
		if(allSub != null){
			for(File sub: allSub){
				if(sub.isFile() && sub.getName().endsWith(".txt")){
					pdfs.add(sub);
				}
			}
		}
		return pdfs;
	}

	private DefaultMutableTreeNode getTreeStructure(){
		File[] roots = new File[]{new File("c:\\")};
		DefaultMutableTreeNode drives = new DefaultMutableTreeNode("Drives");
		for(File root: roots){
			DefaultMutableTreeNode drive = new DefaultMutableTreeNode(root);
			ArrayList<File> folderNodes = getAllDirectories(root);

			for(File folderNode : folderNodes){
				DefaultMutableTreeNode childDrive =new DefaultMutableTreeNode(folderNode.getName());
				ArrayList<File> txts = getAllTXTs(folderNode);
				for(File txt : txts){
					childDrive.add(new DefaultMutableTreeNode(txt));
				}
				drive.add(childDrive);
			}
			drives.add(drive);
		}
		return drives;
	}

}
