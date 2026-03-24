package com.eb.system;

import java.util.ArrayList;
import java.util.List;

public class WordStack {
	
	List<String> stringList;
	int index=0;
		
	public WordStack() {
		stringList = new ArrayList<String>();		
	}
	
	public void addWord(String word)
	{
		if (stringList.contains(word))
			stringList.remove(word);
		
		stringList.add(0, word);
		index=0;				
	}
	
	
	

	public void reset() {
		index = -1;
	}
	
	public boolean hasNext()
	{
		return index>0 && index < stringList.size();
	}
	
	public boolean hasPrev()
	{
		return index < stringList.size()-1;
	}
	
	
	public String getPrev()
	{		
		if (++index>=stringList.size())
			index = stringList.size()-1;
		
		
		
		return stringList.get(index);
	}
	
	public String getNext()
	{		
		if (!hasNext())
			return null;
		
		if (--index<0)
			index=0;			
		
		return stringList.get(index);
	}
	
	public String getNext(String wort)
	{		
		if (!hasNext())
			return null;
		
		if (index >= 0 && index == stringList.size()-1 && !wort.equals(stringList.get(index)))
			return stringList.get(index);
			
		if (--index<0)
			index=0;			
		
		return stringList.get(index);
	}
	
	public String getPrev(String word)
	{		
		if (!hasPrev())
			return null;
		
		if (index == 0 && index< stringList.size() && !word.equals(stringList.get(index)))
			return stringList.get(index);
		return getPrev();
	}
	
	public  List<String> getStrings() {
		return stringList;
	}

	public void setStrings(List<String> sectionValues) {
		if (sectionValues==null)
			return;
		stringList.clear();
		stringList.addAll(sectionValues);
		
	}

}
