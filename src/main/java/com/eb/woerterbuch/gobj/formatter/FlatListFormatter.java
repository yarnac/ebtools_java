package com.eb.woerterbuch.gobj.formatter;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class FlatListFormatter <T> implements IVokabelListFormatter<T>  {
	
	double prozentAbgeschnitteneVokabeln;
	private ItemWithListAdapter<T> adapter;
	
	public FlatListFormatter(double newProzentAbgeschnitteneVokabeln, ItemWithListAdapter<T> newadapter)
	{
		prozentAbgeschnitteneVokabeln = newProzentAbgeschnitteneVokabeln;
		adapter = newadapter;
	}
	
	

	private int maxCount;
	@Override
	public String getString(List<T> itemListe) {
		
		StringBuffer strb = new StringBuffer();
		
		
		int maxLen = getMaxLength(itemListe);
		
		int j=0;
		for (T item : itemListe) {
			
			if (j++>=maxCount)
				continue;
			
			String str = adapter.getName(item);
			
			strb.append(str);
			
			int len = str.length();
			if (len<maxLen)
			{
				for (int i=0;len+i<=maxLen;i+=1)
					strb.append(" ");
				
			}
			else
				strb.append("  ");
			strb.append("  ");
			
			
			int i=0;
			List<String> strings = adapter.getStrings(item);
			for (String bed : strings) {
				if (i++>0)
					strb.append("; ");
				strb.append(bed);
			}
			strb.append("\n");
		}			
		
		return strb.toString();
							
	}
	
	
	
	private int getMaxLength(List<T> itemListe) {
		int maxLen = 0;
		int count = 0;
		int j=0;
		Dictionary<Integer, Integer> dict = new Hashtable<Integer, Integer>();
		for (T item : itemListe) {
			count++;
			
			if (j++>=maxCount)
				continue;
			
			
			int length = adapter.getName(item).length();
			Integer valueOfLength = Integer.valueOf(length);
			Integer i = dict.get(valueOfLength);
			if (i==null)
				i = Integer.valueOf(1);
			else
				Integer.valueOf(i.intValue()+1);
			dict.put(valueOfLength, i);
			
			if (maxLen<length)
				maxLen = length;
		}
		
		double percent = 0;
		int zuLang=0;
		
		while (maxLen>1 && percent < prozentAbgeschnitteneVokabeln)
		{			
			Integer actCount = dict.get(Integer.valueOf(maxLen));
			if (actCount!=null)
				zuLang += actCount.intValue();
			
			percent = (double) zuLang /(double)count * 100.0;
			
			maxLen--;
			
		}
		
		maxLen+=1;
		return maxLen;
	}





	@Override
	public void setMaxVokabeln(int n) {
		maxCount = n;
	}


}
