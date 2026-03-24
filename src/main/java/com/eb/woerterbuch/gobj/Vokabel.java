package com.eb.woerterbuch.gobj;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.eb.system.StringUnifier;

public class Vokabel {
	private String wort;
	private String unifiedWort;
	private List<String> bedeutungen = new ArrayList<>();
	private WbDirection direction;
 
	Array array;
	public Vokabel(String newWort, Iterable<String> newBedeutungen) {
		setWort(newWort);
		setBedeutungen(new ArrayList<>());
		for (String string : newBedeutungen) {
			getBedeutungen().add(string);	
		}
				
	}

	public Vokabel() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String toString() {
		return getWort();
	}

	
	
	public String toStringBU() {
		StringBuffer strb = new StringBuffer();
		strb.append(super.toString() +" " + wort + " ");
		for (String string : bedeutungen) {
			
			strb.append(string + " ");
		}
		return strb.toString();
	}
	
	public String toStringCrypt()
	{
		StringBuffer strb = new StringBuffer();
		strb.append(wort);
		strb.append(": ");
		int i=0;
		for (String s : bedeutungen) {
			if (i++>0)
				strb.append(", ");
			strb.append(s);			
		}
		return strb.toString();
		
	}

	public String getUnifiedWort() {
		return unifiedWort;
	}

	public String getWort() {
		return wort;
	}

	public void setWort(String wort) {
		this.wort = wort;
		unifiedWort = StringUnifier.getUnifiedString(wort);
	}

	public List<String> getBedeutungen() {
		return bedeutungen;
	}

	public void setBedeutungen(Collection<? extends String> newBedeutungen) {
		bedeutungen.clear();
		bedeutungen.addAll(newBedeutungen);
	}

	public static Vokabel ReadFromLine(String line) {
		
		List<String> list = Arrays.stream(line.split("\t"))
				.map(x->ReadWord(x))
				.filter(x->x.trim().length()>0)
				.collect(Collectors.toList());
		
		String w = list.get(0);
		list.remove(w);
		list.remove(0);	// Unified Wort
		
		return new Vokabel(w, list);
	}
	
	 public void removeBedeutung(String wort2) {
		getBedeutungen().remove(wort2);
		
	}

	private static String ReadWord(String p)
     {
         String str = p.trim();
         return str;

     }

	public void addBedeutung(String untrimmed) {
		String trim = untrimmed.trim();
		if (!bedeutungen.contains(trim))
			bedeutungen.add(trim);
		List<String> tmp = new ArrayList<>();
		for (String string : bedeutungen) {
			if (!tmp.contains(string))
				tmp.add(string);
		}
		bedeutungen = tmp;
	}

	public boolean matches(String wort2) {
		return getWort().equals(wort2);
	}

	public void addBedeutungen(List<String> bedeutungen2) {
		for (String string : bedeutungen2) {
			addBedeutung(string);
		}
		
	}
	
	public List<Vokabel> getReverse()
	{

		List<Vokabel> res = new ArrayList<Vokabel>();
		for (String string : bedeutungen) {
			Vokabel n = new Vokabel();
			n.setWort(string);
			n.addBedeutung(getWort());
			res.add(n);
		};
		return res ;
	}

	public boolean isSameVokabel(Vokabel element2) {
		// TODO Auto-generated method stub
		return getWort().equals(element2.getWort());
	}

	public WbDirection getDirection() {
		return direction;
	}

	public void setDirection(WbDirection direction) {
		this.direction = direction;
	}

	public static List<String> getStringsFromBedeutung(String string) {				
		String newString = string.replace(","," ");
		
		
		do
		{
			int startIndex = newString.indexOf("{");
			int stopIndex = newString.indexOf("}");
			if (startIndex<0 || stopIndex<0)
				break;
			newString = (newString.substring(0,startIndex) + newString.substring(stopIndex+1)).trim();
		} while (true);
		
		
		String[] parts = newString.split(" ");
		
		
		
		List<String> result = new ArrayList<>();
		for (String string2 : parts) {
			if (string2.length()<3)
				continue;
			result.add(string2);
		}
		
		return result;
	}

	public String toStringWithBedeutungen(int wortLen) {
		StringBuilder strb = new StringBuilder();
		
		
		strb.append(wort);
		for (int i=wort.length(); i<wortLen;i++)
			strb.append(' ');
		
		for (String string : bedeutungen) {
			strb.append(string + "; ");
		}
		return strb.toString();
	}


}
