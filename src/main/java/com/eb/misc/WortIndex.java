package com.eb.misc;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public abstract class WortIndex<T> {
	
	private Dictionary<String, List<T>> index;
	private List<String> keys;
	private WordSplitter splitter = new WordSplitter();
	
	List<T> get(String word)
	{
		List<T> list = index.get(word);
		if (list==null)
			list = new ArrayList<>();
		return list;		
	}
	
	
	
}
