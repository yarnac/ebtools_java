package com.eb.apps.ebwoerterbuch.tobj.searcher;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.eb.base.system.StringUnifier;
import com.eb.apps.ebwoerterbuch.gobj.Vokabel;

public class DictionarySearchStrategyFactory
{
	public static List<IDictionarySearchStrategy<Vokabel>> createDirectorySearcher()
	{
		List<IDictionarySearchStrategy<Vokabel>> res = new ArrayList<IDictionarySearchStrategy<Vokabel>>();
		
	 

		res.add(new IDictionarySearchStrategy<>() {
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public String getLabel() {return "x*";}
			@Override public List<String> getKeys(Vokabel obj) { return getAllKeySubstrrings(obj,"BEGIN ", 3);}			
			@Override public String getKey(String str) { return "BEGIN " + getFirst3Chars(StringUnifier.getUnifiedString(str));}	
			@Override public boolean matches(Vokabel obj, String unifiedWord) {return obj.getUnifiedWort().startsWith(unifiedWord);}
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			});

		res.add(new IDictionarySearchStrategy<>() {
			@Override public String getLabel() {return "*x";}			
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public List<String> getKeys(Vokabel obj) { return getAllKeySubstrrings(obj,"END ", 3);}
			@Override public String getKey(String str) { return "END " + getFirst3Chars(reverse(str));}
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			@Override public boolean matches(Vokabel obj, String str) {return reverse(obj.getUnifiedWort()).startsWith(reverse(StringUnifier.getUnifiedString(str)));}});
		
		
		res.add(new IDictionarySearchStrategy<>() {
			@Override public String getLabel() {return "*x*";}			
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public String getKey(String str) { return "* flatList *";}
			@Override public boolean isUsingGrundliste() {return true;	}
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			@Override public boolean matches(Vokabel obj, String str) {return obj.getUnifiedWort().contains(StringUnifier.getUnifiedString(str));}});
		
		// ist identisch benutzt den gleichen Index wie beginnt mit
		res.add(new IDictionarySearchStrategy<>() {
			@Override public boolean isUsingOwnIndex() {return false;}
			@Override public String getLabel() {return "x";}			
			@Override public String getKey(Vokabel obj) { return getKey(obj.getWort());}
			@Override public List<String> getKeys(Vokabel obj) { return getAllKeySubstrrings(obj,"BEGIN ", 3);}			
			@Override public String getKey(String str) { return "BEGIN " + getFirst3Chars(StringUnifier.getUnifiedString(str));}	
			@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}
			@Override public boolean matches(Vokabel obj, String str) {return obj.getUnifiedWort().equals(StringUnifier.getUnifiedString(str));}});


		return res;
	}
	
	public static IDictionarySearchStrategy<Vokabel> createDirectorySearcher(String aktModus) {
		return createDirectorySearcher()
				.stream()
				.filter(x->x.getLabel().equals(aktModus))
				.findAny().get();
	}
	
	private static String getFirst3Chars(String str)
	{
		return str.length()< 3 ? str : str.substring(0, 3);
	}
	
	private static String reverse(String getUnifiedWort) {

		StringBuffer strb = new StringBuffer();
		for (int i=getUnifiedWort.length()-1;i>=0;i--)
			strb.append(getUnifiedWort.charAt(i));
		return strb.toString();	
	}
}