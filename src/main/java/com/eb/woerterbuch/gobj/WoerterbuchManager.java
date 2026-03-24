package com.eb.woerterbuch.gobj;

import com.eb.base.EbAppContext;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.ebookreader.tobj.StringConsumer;
import com.eb.misc.DictionarySearchStrategyFactory;
import com.eb.system.StringUnifier;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class WoerterbuchManager {
	
	private static String woerterbuchPfad;
		
	private static WoerterbuchManager current;
	
	private String pfad;

	private IniFile iniFile;

	private List<String> sprachen;

	private HashMap<String, Woerterbuch> woerterbuchDict;
	
	private StringConsumer uebersetzungsRequestHandler;
	
	private WoerterbuchManager()
	{
		woerterbuchDict = new HashMap<>();
		
		setIniFile(IniFileProvider.createIniFile(getPfad()+"/Java.ini"));
		getIniFile().Read();
		InitSprachen();
		Woerterbuch.setAllSearcher(DictionarySearchStrategyFactory.getDirectorySearcher());		
	}
	
	private void InitSprachen() {
		sprachen = iniFile.getSectionValues("[WB-Sprachen]");
	}
	
	public Woerterbuch getWoerterbuch(String sprache)
	{
		return getWoerterbuchDict().computeIfAbsent(sprache, spr -> LoadWoerterbuch(spr));
	}
	
	private Woerterbuch LoadWoerterbuch(String aktSprache) {
		String fileName = getWoerterbuchFileName(aktSprache);
		if (fileName==null)
			return null;
		
		Woerterbuch wb = Woerterbuch.LoadWoerterbuch(fileName);
		return wb;
	}

	private String getFileName(String aktSprache) {
		return getPfad() + (getPfad().endsWith("/") ? "": "/") + aktSprache;
	}
	
	private String getWoerterbuchFileName(String aktSprache) {
		// TODO Auto-generated method stub
		String fn = getFileName(aktSprache+"_Deutsch.txt");
		if (new File(fn).exists())
			return fn;
		
		fn = StringUnifier.getStringOhneUmlaute(fn);
		
		if (new File(fn).exists())
			return fn;

		throw(new RuntimeException("Kann Wörterbuch nicht finden: " + fn));
	}
	

	public String getPfad() {
		if (pfad==null)
		{
			pfad = getWoerterbuchPfad();
			if (getWoerterbuchPfad()!=null)			
				return pfad;
			
			pfad = EbAppContext.getJavaDataFilename("Woerterbuch");
		}
		return pfad;
	}

	public IniFile getIniFile() {
		return iniFile;
	}

	public void setIniFile(IniFile iniFile) {
		this.iniFile = iniFile;
	}

	public List<String> getSprachen() {
		return sprachen;
	}
	
	private HashMap<String, Woerterbuch> getWoerterbuchDict() {
		return woerterbuchDict;
	}

	public static WoerterbuchManager getCurrent() {
		if (current==null)
			current = new WoerterbuchManager();
		return current;
	}

	public static void setCurrent(WoerterbuchManager current) {
		WoerterbuchManager.current = current;
	}

	public static String getWoerterbuchPfad() {
		return woerterbuchPfad;
	}

	public static void setWoerterbuchPfad(String woerterbuchPfad) {
		WoerterbuchManager.woerterbuchPfad = woerterbuchPfad;
	}
	
	
}
