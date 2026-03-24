package com.eb.misc;

import java.util.ArrayList;
import java.util.List;

import com.eb.system.StringUnifier;
import com.eb.woerterbuch.gobj.Vokabel;

public  class IndexSearchStrategy extends IDictionarySearchStrategy<Vokabel>  {
	
	
	WordSplitter splitter = new WordSplitter();
	
	@Override
	public boolean matches(Vokabel obj, String unifiedWord) {
		return true;
	}

	@Override
	public String getLabel() {
		return "Indexsuche";
	}

	
	@Override
	public List<String> getKeys(Vokabel obj)
	{
		List<String> res = new ArrayList<String>();
		for (String string : splitter.split(obj.getUnifiedWort())) {
			res.add(getKey(string));			
		}
		return res; 
	}
	

	@Override
	public String getKey(Vokabel obj) {		
		return getKey(obj.getWort());
	}

	@Override
	public String getKey(String str) {
		
		return "Index " + StringUnifier.getUnifiedString(str);
	}
	

	@Override public boolean elementsAreEqual(Vokabel element1, Vokabel element2) {return element1.isSameVokabel(element2);}

}
