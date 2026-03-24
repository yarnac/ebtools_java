package com.eb.misc;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public abstract class IDictionarySearchStrategy<T> {
	public abstract String getLabel();
	
	public abstract String getKey(T obj);
	public abstract String getKey(String unifiedString);
	public abstract boolean elementsAreEqual(T element1, T element2);
	
	public Dictionary<String, List<T>> dictionary = new Hashtable<>();
	
	public List<String> getKeys(T obj)
	{
		List<String> res = new ArrayList<String>();
		res.add(getKey(obj));
		return res; 
	}
	
	public List<String> getKeys(String s)
	{
		List<String> res = new ArrayList<String>();
		res.add(getKey(s));
		return res; 
	}
	
	public boolean isUsingGrundliste()
	{
		return false;
	}
		
	public boolean isUsingOwnIndex()
	{
		return true;
	}
	
	public List<String> getAllKeySubstrrings(T obj, String praefix, int maxLen)
	{
		List<String> res = new ArrayList<String>();
		String key = getKey(obj).substring(praefix.length());
		
		int n = Math.min(key.length(), maxLen);
				
		res.add(praefix + key.substring(0,n));
		
		/*				
		for (int i=key.length(); i<=key.length() && i<=maxLen; i++)
		{
			res.add(praefix + key.substring(0,i));
		}
		*/		
		return res; 
	}
	
	public abstract boolean matches(T obj, String unifiedWord);
	
	@Override
	public String toString() {return getLabel();}
}
