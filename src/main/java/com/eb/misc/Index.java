package com.eb.misc;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;



public class Index <T> {
	private List<String> keys = new ArrayList<String>();
	private Dictionary<String, List<T>> index = new Hashtable<>();
	private WordSplitter splitter = WordSplitter.createWortSplitter();
	
	public void add(String word, T t)
	{
		for (String string : splitter.split(word)) {
			addEinzelwort(string, t);
		} 
	}

	private void addEinzelwort(String string, T t) {
		List<T> list = index.get(string);
		if (list==null)
		{
			keys.add(string);
			list = new ArrayList<T>();
			index.put(string, list);
		}					
		list.add(t);
	}
	
	public List<T> get(String key)
	{
		List<T> list = index.get(key);
		if (list==null)
			list = new ArrayList<T>(); 
		return list;
	}
	
	
}
