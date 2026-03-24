package com.eb.misc;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eb.system.StringUnifier;
import com.eb.woerterbuch.gobj.Vokabel;

public class DictionarySearchStrategyFactory
{
	public static List<IDictionarySearchStrategy<Vokabel>> directorySearcher;
	
	public static List<String> getSearchStrategyStrings() {
		return getDirectorySearcher().stream().map(x->x.toString()).collect(Collectors.toList());
	}
	
	public static List<IDictionarySearchStrategy<Vokabel>> getDirectorySearcher()
	{
		if (directorySearcher != null)
			return directorySearcher;
		
		List<com.eb.misc.IDictionarySearchStrategy<Vokabel>> res = new ArrayList<IDictionarySearchStrategy<Vokabel>>();
		
	 

		res.add(new IDictionarySearchStrategy<Vokabel>() {										
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public String getLabel() {return "x*";}
			@Override public List<String> getKeys(Vokabel obj) { return getAllKeySubstrrings(obj,"BEGIN ", 3);}			
			@Override public String getKey(String str) { return "BEGIN " + getFirst3Chars(StringUnifier.getUnifiedString(str));}	
			@Override public boolean matches(Vokabel obj, String unifiedWord) {return obj.getUnifiedWort().startsWith(unifiedWord);}
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			});

		res.add(new IDictionarySearchStrategy<Vokabel>() {					
			@Override public String getLabel() {return "*x";}			
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public List<String> getKeys(Vokabel obj) { return getAllKeySubstrrings(obj,"END ", 3);}
			@Override public String getKey(String str) { return "END " + getFirst3Chars(reverse(str));}
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			@Override public boolean matches(Vokabel obj, String str) {return reverse(obj.getUnifiedWort()).startsWith(reverse(StringUnifier.getUnifiedString(str)));}});
		
		
		res.add(new IDictionarySearchStrategy<Vokabel>() {					
			@Override public String getLabel() {return "*x*";}			
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public String getKey(String str) { return "* flatList *";}
			@Override public boolean isUsingGrundliste() {return true;	}
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			@Override public boolean matches(Vokabel obj, String str) {return obj.getUnifiedWort().contains(StringUnifier.getUnifiedString(str));}});
		
		// ist identisch benutzt den gleichen Index wie beginnt mit
		res.add(new IDictionarySearchStrategy<Vokabel>() {					
			@Override public boolean isUsingOwnIndex() {return false;}
			@Override public String getLabel() {return "x";}			
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public List<String> getKeys(Vokabel obj) { return getAllKeySubstrrings(obj,"BEGIN ", 3);}			
			@Override public String getKey(String str) { return "BEGIN " + getFirst3Chars(StringUnifier.getUnifiedString(str));}	
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			@Override public boolean matches(Vokabel obj, String str) {return obj.getUnifiedWort().equals(StringUnifier.getUnifiedString(str));}});
				
		// res.add(new IndexSearchStrategy());		
		
		
		directorySearcher = res;
		return res;
	}
	
	public static IDictionarySearchStrategy<Vokabel> getDirectorySearcher(String aktModus) {
		return getDirectorySearcher()
				.stream()
				.filter(x->x.getLabel().equals(aktModus))
				.findAny().get();
	}
	
	private static String getFirst3Chars(String str)
	{
		return str.length()< 3 ? str : str.substring(0, 3);
	}
	
	private static String reverse(String getUnifiedWort) {
		// TODO Auto-generated method stub
		StringBuffer strb = new StringBuffer();
		for (int i=getUnifiedWort.length()-1;i>=0;i--)
			strb.append(getUnifiedWort.charAt(i));
		return strb.toString();	
	}

	public static IDictionarySearchStrategy<Vokabel> defaultSearcher() {
		return getDirectorySearcher().get(0);
	}

	
}