package com.eb.woerterbuch.tobj;

import com.eb.base.inifile.api.IniFile;
import com.eb.woerterbuch.gobj.WbEinstellungen;

public class GoogleSearchManager
{
	private static GoogleSearchManager current;

	public static GoogleSearchManager getCurrent() {
		if (current==null)
			current = new GoogleSearchManager();
		return current;
	}

	public void setEinstellungen(IniFile iniFile, WbEinstellungen einstellungen) {
		iniFile.setSectionValue("Suche", "Websuche", einstellungen.getGoogleWeb());
		iniFile.setSectionValue("Suche", "Bilder", einstellungen.getGoogleBilder());
		iniFile.setSectionValue("Suche", "Sprache", einstellungen.getGoogleSprache());
		iniFile.setSectionValue("Suche", "Verbix", einstellungen.getVerbix());
		iniFile.Write();
	}

	public WbEinstellungen getEinstellungen(IniFile iniFile) {
		WbEinstellungen einstellungen = new WbEinstellungen();
		iniFile.setSectionValue("Suche", "Suche", einstellungen.getGoogleWeb());
		iniFile.setSectionValue("Suche", "Bilder", einstellungen.getGoogleBilder());
		iniFile.setSectionValue("Suche", "Sprache", einstellungen.getGoogleSprache());
		iniFile.setSectionValue("Suche", "Verbix", einstellungen.getVerbix());
		iniFile.Write();
		return einstellungen;
	}
	 

}