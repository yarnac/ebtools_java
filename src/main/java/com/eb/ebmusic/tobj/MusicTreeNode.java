package com.eb.ebmusic.tobj;

import com.eb.ebmusic.gobj.MusicFolder;

import java.util.Vector;

import javax.swing.tree.DefaultMutableTreeNode;


public class MusicTreeNode extends DefaultMutableTreeNode{

	private MusicFolder folder;

	public MusicTreeNode(MusicFolder newFolder) {
		setUserObject(newFolder);
		setFolder(newFolder);
		setAllowsChildren(true);
		children = new Vector<>();
		if (newFolder.isDirectory())
		{
			for ( MusicFolder subfolder : newFolder.getEntries()) {
				MusicTreeNode musicTreeNode = new MusicTreeNode(subfolder);
				add(musicTreeNode);				
			}		
		}
		
	}

	public MusicFolder getFolder() {		
		return folder;
	}

	public void setFolder(MusicFolder folder) {
		this.folder = folder;
	}

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
