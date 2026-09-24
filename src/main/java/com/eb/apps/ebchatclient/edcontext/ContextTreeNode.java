package com.eb.apps.ebchatclient.edcontext;

import com.eb.apps.ebchatclient.domain.context.domain.ContextWithFiles;
import com.eb.apps.ebmusic.gobj.MusicFolder;
import lombok.Getter;
import lombok.Setter;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.Serial;
import java.util.Vector;


public class ContextTreeNode extends DefaultMutableTreeNode{

	@Getter
	@Setter
	private String kategorie;

	public ContextTreeNode(MusicFolder newFolder) {
		setUserObject(newFolder);
		setAllowsChildren(true);
		children = new Vector<>();
	}

	public ContextTreeNode(ContextWithFiles context) {
		setUserObject(context);
	}

	@Override
	public String toString() {
		return getUserObject() == null ? getKategorie() : getUserObject().toString();
	}

	public ContextTreeNode(String kategorie) {
		if (kategorie == null || kategorie.isEmpty())
			setUserObject("unbekannt");
		else
			setKategorie(kategorie);
	}

	
	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 1L;

}
