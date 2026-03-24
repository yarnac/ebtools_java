package com.eb.misc;

import java.util.List;

import com.eb.woerterbuch.gobj.Vokabel;

public class VokabelIIndexerFactory {
	
	
	public static IIndexer<Vokabel> createFirstCharsIndex()
	{
		return new IIndexer<Vokabel>() {
			@Override public String getKey(Vokabel obj) {return getKey(obj.getUnifiedWort());}
			@Override public String getKey(String unifiedString) {return getFirst3Chars(unifiedString);}
		};
	}
	
	
	public static IIndexer<Vokabel> createNopIndex()
	{
		return new IIndexer<Vokabel>() {
			@Override public String getKey(Vokabel obj) {return "";}
			@Override public String getKey(String unifiedString) {return "";}
			@Override public List<Vokabel> getElements(String str) {return getGrundliste();}
		};
	}
	
	
	public static IIndexer<Vokabel> createLastCharsIndex()
	{
		return new IIndexer<Vokabel>() {
			@Override public String getKey(Vokabel obj) {return getKey(obj.getUnifiedWort());}
			@Override public String getKey(String unifiedString) {return getFirst3Chars(reverse(unifiedString));}
		};
	}
	
	public static IIndexer<Vokabel> createWordsIndex()
	{
		
		return new IIndexer<Vokabel>() {
			WordSplitter splitter = new WordSplitter();
			
			@Override public String getKey(Vokabel obj) {return getKey(obj.getUnifiedWort());}
			@Override public String getKey(String unifiedString) {return unifiedString;}
			
			@Override
			public List<String> getKeys(Vokabel obj)
			{
				return splitter.split(obj.getUnifiedWort());
			}
		};
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
}
