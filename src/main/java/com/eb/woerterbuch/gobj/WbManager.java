package com.eb.woerterbuch.gobj;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;
import com.eb.woerterbuch.gui.ShowCryptedFileDlg;
import com.eb.woerterbuch.gui.WordListDlgCtrl;

public class WbManager {
	IniFile iniFile;
	Map<String, WoerterbuchSession> sessionDict = new HashMap<>();

	String[] Datenpfade = new String[]{
			"~/Data/Woerterbuch/Java.txt",
			"/Users/ekkart/Data/Woerterbuch",
			"/Users/ekkartbolten/Data/Woerterbuch",
			"z:\\data\\Woerterbuch",
			"c:\\data\\Woerterbuch"};
	
	private String dataFilesPath;
	private WoerterbuchSession aktWbSession;
	private WoerterbuchManager woerterbuchManager;
	
	
	
	public WbManager()
	{
		setWoerterbuchManager(WoerterbuchManager.getCurrent());
		iniFile = getWoerterbuchManager().getIniFile();
		if (iniFile==null)
			return;
		InitSprachen();
	}
	 
	
	
	

	private void InitSprachen() {		
		String aktSprache = iniFile.getSectionValue("[WB-Einstellungen]", "LastSprache", "Tuerkisch");						
		String aktModus = iniFile.getSectionValue("[WB-Einstellungen]", "Modus", "beginnt mit");
		String aktRichtung = iniFile.getSectionValue("[WB-Einstellungen]", "Richtung", "A --> B");				
		WbDirection fromString = WbDirection.getFromString(aktRichtung);					
		WoerterbuchSession session = getWbSession(aktSprache);					
		
		session.setDirection(fromString);
		session.setTheSearcherForName(aktModus);
				
		setAktWbSession(session);
	}

	
	private WoerterbuchSession getWbSession(String aktSprache) {
		return sessionDict.computeIfAbsent(aktSprache, sprache->createSession(sprache));
	}

	private WoerterbuchSession createSession(String aktSprache) {				
		WoerterbuchSession session = new WoerterbuchSession(aktSprache, getStackFileName(), WbDirection.A_Nach_B, null);		
		return session;
	}

	public WoerterbuchSession setWbSession(String aktSprache) {
		setAktWbSession(getWbSession(aktSprache));
		return getAktWbSession();
	}

	private void InitIniFile() {
		for(String pfad: Datenpfade)
		{
			String pathname = pfad + "/Java.txt";
			File file = new File(pathname);
			if (file.exists())
			{
				iniFile = IniFileProvider.createIniFile(pathname);
				iniFile.Read();
				setDataFilesPath(pfad + "/");
				return;
			}
		}
		
	}

	public WoerterbuchSession getAktWbSession() {
		return aktWbSession;
	}

	public void setAktWbSession(WoerterbuchSession aktWbSession) {
		this.aktWbSession = aktWbSession;
	}

	public List<String> getSprachen() {
		return woerterbuchManager.getSprachen();
	}
	
	public void switchSprache(String sprache) {
		setWbSession(sprache);
	}

	
	

	public void addVokabel(Vokabel v) {
		getAktWbSession().addVokabel(v);
		
		saveAktWoerterbuch();
		
	}
	
	public void replaceVokabel(Vokabel v) {
		getAktWbSession().replaceVokabel(v);
		
		saveAktWoerterbuch();
		
	}

	private void saveAktWoerterbuch() {
		Woerterbuch woerterbuch = getAktWbSession().getWoerterbuch();
		woerterbuch.save();
		
	}


	public Point getWindowLocation() {
		String[] point = iniFile.getSectionValue("[Einstellungen]", "Location","100,100").split(",");
		int x = Integer.parseInt(point[0]);
		int y = Integer.parseInt(point[1]);
		return new Point(x,y);
	}





	public Dimension getWindowSize() {
		String[] point = iniFile.getSectionValue("[Einstellungen]", "Size","100,100").split(",");
		int x = Integer.parseInt(point[0]);
		int y = Integer.parseInt(point[1]);
		return new Dimension(x,y);
	}





	public void setWindowLocation(Point location) {
		iniFile.setSectionValue("[Einstellungen]", "Location", "" + location.x + "," + location.y);		
	}





	public void setWindowSize(Dimension size) {
		iniFile.setSectionValue("[Einstellungen]", "Size", "" + size.width + "," + size.height);
	}





	public void save() {
		iniFile.setSectionValue("[WB-Einstellungen]", "LastSprache", getAktWbSession().getSprache());
		iniFile.setSectionValue("[WB-Einstellungen]", "Modus", getAktWbSession().getSearcher().toString());
		iniFile.setSectionValue("[WB-Einstellungen]", "Richtung", getAktWbSession().getDirection().toString());
					
		iniFile.Write();		
	}





	public String getIniFileName() {
		return iniFile.getFileName();
	}
	public void reloadIniFile() {
		iniFile.Read();
	}
	public String getCurrentWoerterbuchFileName() {			
		return getAktWbSession().getWoerterbuch().getFileName();
	}

	public List<Vokabel> fetchVokabeln(String wort, boolean showingAllLoaded) {
		if (!showingAllLoaded)
			return getAktWbSession().fetchVokabeln(wort);
		
		List<Vokabel> res = new ArrayList<>();
		Set<String> keys = sessionDict.keySet();
		for (String key : keys) {
			WoerterbuchSession nextElement = sessionDict.get(key);
			res.addAll(nextElement.fetchVokabeln(wort));
		}
		 
		return res;
	}

	public void openJava() {
		FileUtil.start("open /Applications/Eclipse.app");
	}
	
	public String getDataFilesPath() {
		return dataFilesPath;
	}

	public void setDataFilesPath(String dataFilesPath) {
		this.dataFilesPath = dataFilesPath;
	}





	public String getDataFilesFileName(String string) {
		return getDataFilesPath()+string;
	}





	public String getStackFileName() {
		return getDataFilesPath()+"Stack.txt";
	}
	
	public void reloadStackFile()
	{
		Collection<WoerterbuchSession> elements = sessionDict.values();
	}

	public void editStackFile() {
		FileUtil.open(getStackFileName());
	}

	public void editIniFile() {
		FileUtil.open(getIniFileName());
	}
	
	public void editCurrentWoerterbuch() {
		FileUtil.open(getCurrentWoerterbuchFileName());
	}





	public void openWordListDlg(List<String> list) {
		new WordListDlgCtrl(list);		
	}





	public void  openCrypter(String string) {
		try {
			ShowCryptedFileDlg showCryptedFileDlg = new ShowCryptedFileDlg();
			showCryptedFileDlg.setPasswort(string);
			showCryptedFileDlg.setVisible(true);
		} catch (Exception e)
		{
			e.toString();
		}
	}





	public WoerterbuchManager getWoerterbuchManager() {
		return woerterbuchManager;
	}





	public void setWoerterbuchManager(WoerterbuchManager woerterbuchManager) {
		this.woerterbuchManager = woerterbuchManager;
	}
}

