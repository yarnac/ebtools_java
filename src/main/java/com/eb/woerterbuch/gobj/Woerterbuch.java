package com.eb.woerterbuch.gobj;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.FileUtil;
import com.eb.misc.IDictionarySearchStrategy;
import com.eb.woerterbuch.tobj.GoogleSearchManager;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("nls")
public class Woerterbuch {
	IniFile iniFile;
	VokabelListe aNachB;
	VokabelListe bNachA;
	private VokabelListe vokabelnAnachB;
	private VokabelListe vokabelnBnachA;
	private String spracheA;
	private String spracheB; 
	private WbDirection wbDirection;
	private static List<IDictionarySearchStrategy<Vokabel>> allSearcher;
		
	
	
	 public Woerterbuch(String string) {
		// TODO Auto-generated constructor stub
	}
	 
	 public Woerterbuch() {
			// TODO Auto-generated constructor stub
		}
	 
	public void save() {
		 save("[A->B]", getVokabelnAnachB());
	 }
	 
	 private void save(String string, VokabelListe vokabeln) {
		
		 List<Vokabel> vList = vokabeln.getVokabeln();				 
		 vList.sort((a,b)->a.getWort().compareTo(b.getWort()));		 
		 List<String> list = vList.stream(). map( x->getLine(x) ) .collect(Collectors.toList());				 				 				 			
		 iniFile.setSectionValues(string, list);
		 iniFile.Write();
	}

	
	private String getLine(Vokabel v)
	 {
		StringBuilder strb = new StringBuilder();
		strb.append(v.getWort());
		strb.append('\t');
		strb.append(v.getUnifiedWort());
		strb.append('\t');
		for (String bed : v.getBedeutungen()) {
			strb.append(bed);
			strb.append('\t');
			
		}
		String res = strb.toString().trim();
		
		return res;
	 }
	
	public static Woerterbuch LoadStdWoerterbuch(String newfileName)
	{
		if (newfileName.equals("Türkisch"))
			return LoadWoerterbuch("~/Data/Woerterbuch/Tuerkisch_Deutsch.txt");
		if (newfileName.equals("Portugiesisch"))
			return LoadWoerterbuch("~/Data/Woerterbuch/Portugiesisch_Deutsch.txt");
		return null;
		
	}
	
	

	public static Woerterbuch LoadWoerterbuch(String newfileName)
     {
		 
		 IniFile file = IniFileProvider.createIniFile(newfileName);
         file.Read();
         
         Woerterbuch w = new Woerterbuch();
         w.iniFile = file;         
         w.setVokabelnAnachB(new VokabelListe());
         w.setVokabelnBnachA(new VokabelListe());
         
         w.setSpracheA(file.getSectionValue("[Woerterbuch]", "SpracheA", "-"));
         w.setSpracheB(file.getSectionValue("[Woerterbuch]", "SpracheB", "-"));
         
         SetVokabelStrings(w.getVokabelnAnachB(), file.getSectionValues("[A->B]"));
         SetVokabelStrings(w.getVokabelnBnachA(), file.getSectionValues("[B->A]"));
         
         for (Vokabel v : w.getVokabelnAnachB().getVokabeln()) 
        	 v.setDirection(WbDirection.A_Nach_B);
         for (Vokabel v : w.getVokabelnBnachA().getVokabeln()) 
        	 v.setDirection(WbDirection.B_Nach_A);    	
		
         return w;	         
     }


	private static void SetVokabelStrings(VokabelListe vokabelListe, List<String> vokabelStrings) {
		for (String s : vokabelStrings)
        {
            Vokabel v = Vokabel.ReadFromLine(s);
            vokabelListe.addVokabel(v);
        }            
		
	}

	public void setWBDirection(String aktRichtung) {
		setWbDirection(WbDirection.getFromString(aktRichtung));
		
	}

	public WbDirection getWbDirection() {
		return wbDirection;
	}

	public void setWbDirection(WbDirection wbDirection) {
		this.wbDirection = wbDirection;
	}

	public static List<IDictionarySearchStrategy<Vokabel>> getAllSearcher() {
		return allSearcher;
	}

	public static void setAllSearcher(List<IDictionarySearchStrategy<Vokabel>> allSearcher) {
		Woerterbuch.allSearcher = allSearcher;
	}

	public String getSpracheA() {return spracheA;}
	public void setSpracheA(String spracheA) {this.spracheA = spracheA;}

	public String getSpracheB() {return spracheB;}
	public void setSpracheB(String spracheB) {this.spracheB = spracheB;}

	public VokabelListe getVokabelnAnachB() {return vokabelnAnachB;}
	public void setVokabelnAnachB(VokabelListe vokabelnAnachB) {this.vokabelnAnachB = vokabelnAnachB;}

	public VokabelListe getVokabelnBnachA() {return vokabelnBnachA;}
	public void setVokabelnBnachA(VokabelListe vokabelnBnachA) {this.vokabelnBnachA = vokabelnBnachA;}
	
	public String getFileName() {return iniFile.getFileName();}

	public String getGoogleString() {return iniFile.getSectionValue("Suche", "Suche", null);}
	public String getGoogleStringBilder() {return iniFile.getSectionValue("Suche", "Bilder", null);}
	public String getGoogleStringVerbix() {return iniFile.getSectionValue("Suche", "Verbix", null);}
	public String getGoogleStringSprache() {return iniFile.getSectionValue("Suche", "Sprache", null);}
	

	public void setEinstellungen(WbEinstellungen einstellungen) {
		GoogleSearchManager.getCurrent().setEinstellungen(iniFile, einstellungen);
		
	}
	public WbEinstellungen getEinstellungen()
	{
		return GoogleSearchManager.getCurrent().getEinstellungen(iniFile);
	}

	public void exportForGoogle() {
		FileUtil.WriteLines("", iniFile.getFileName()+"A", iniFile.getSectionValues("A->B"));
		FileUtil.WriteLines("", iniFile.getFileName()+"B", iniFile.getSectionValues("B->A"));
	}
	
	
}
