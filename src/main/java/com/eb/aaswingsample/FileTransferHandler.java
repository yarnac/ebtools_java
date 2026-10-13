package com.eb.aaswingsample;

import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.Serial;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;

public class FileTransferHandler extends TransferHandler {
	
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 4278824893466339483L;

	public int getSourceActions(JComponent Source){

		return COPY_OR_MOVE;
	}

	protected Transferable createTransferable(JComponent source){

		JTree tree = (JTree)source; 
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)tree.getLastSelectedPathComponent();
		if(node == null)
			return null;

		File file = new File(node.getUserObject().toString());
		ArrayList<File> fileList = new ArrayList<>();
		fileList.add(file);
		return new FileTransferable(fileList);
	}

}
