package com.eb.aaswingsample;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FileTransferable implements Transferable {

	final private List<File> files;
	final private DataFlavor[] flavors;


	public FileTransferable(Collection<File> files) {
		this.files = List.copyOf(new ArrayList<>(files));
		this.flavors = new DataFlavor[] 
		                              { DataFlavor.javaFileListFlavor };
	}

	public DataFlavor[] getTransferDataFlavors() {

		return this.flavors;
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {

		return DataFlavor.javaFileListFlavor.equals(flavor);
	}


	@Override
    public Object getTransferData(DataFlavor flavor){

		if (isDataFlavorSupported(flavor))
			return this.files;
		else
			return "";
	}

}
