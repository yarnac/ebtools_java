package com.eb.ebmusic.gobj;

import com.eb.base.EbAppContext;
import com.eb.base.inifile.api.IniFile;

import com.eb.base.inifile.api.IniFileProvider;
import com.eb.ebmusic.tobj.MusicFolderReader;

import java.io.File;

public class EbMusicLib {
	
	private String name;
	private IniFile inifile;
	private com.eb.ebmusic.gobj.MusicFolder rootNode;
	
	public EbMusicLib(String inifileName)
	{
		if (inifileName == null )
			throw new NullPointerException("inifileName is null");

		File file = new File(inifileName);
		if (!file.exists())
			throw new IllegalArgumentException("inifileName " + inifileName + " does not exist");
		System.out.println("Loading inifileName " + inifileName);
		IniFile f = IniFileProvider.createIniFile(inifileName);
		setInifile(f);
		getInifile().Read();
		setName(inifile.getSectionValue("Einstellungen", "Name", "New Bibliothek"));
	}
	
	@Override
	public String toString() {
		return getName() ;
	}
	
	public void updateContents() {
		MusicFolder musicFolder = MusicFolderReader.readFromLibraryFolder(getRootNode().getPath());
		setRootNode(musicFolder);
		saveRootNode();		
	}

	public String fetchDatafilename()
	{
		String name = getInifile().getSectionValue("Einstellungen", "Datenbankdatei", "");
		String fileName = EbAppContext.getEbToolsDatenDir(name);

		File file = new File(fileName);
		if (!file.exists())
			throw new IllegalArgumentException("file " + fileName + " does not exist");
		return fileName;
	}
	
	private void loadRootNode() {
		inifile.Read();
		String datenbankFilename = fetchDatafilename();
		IniFile datenFile = IniFileProvider.createIniFile(datenbankFilename);
		datenFile.Read();

		MusicFolder folder = MusicFolderReader.readFromDatendatei(datenbankFilename);
		setRootNode(folder);
	}
		
	private void saveRootNode() {
		MusicFolderReader.writeToDatendatei(fetchDatafilename(), getRootNode());		
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public IniFile getInifile() {
		return inifile;
	}
	public void setInifile(IniFile inifile) {
		this.inifile = inifile;
	}
	public MusicFolder getRootNode() {
		if (rootNode==null)
			loadRootNode();		
		
		return rootNode;
	}

	public void setRootNode(MusicFolder rootNode) {
		this.rootNode = rootNode;
	}	
}
