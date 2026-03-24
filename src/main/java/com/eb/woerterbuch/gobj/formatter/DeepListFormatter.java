package com.eb.woerterbuch.gobj.formatter;

import java.util.List;

public class DeepListFormatter <T> implements IVokabelListFormatter<T>  {
	
	double prozentAbgeschnitteneVokabeln;
	private ItemWithListAdapter<T> adapter;
	
	public DeepListFormatter(ItemWithListAdapter<T> newadapter)
	{		
		adapter = newadapter;
	}
	
	
	int icall;
	private int maxCount;
	@Override
	public String getString(List<T> itemListe) {
		
		StringBuffer strb = new StringBuffer();
		
				
		int j=0;
		for (T item : itemListe) {
			
			if (j++>=maxCount)
				continue;
			
			String str = adapter.getName(item);
			
			strb.append(str);
			
			strb.append("\n");
			
			List<String> strings = adapter.getStrings(item);
			for (String bed : strings) {
				strb.append("\t\t");
				strb.append(bed);
				strb.append("\n");
				
			}
			strb.append("\n");
		}			
		
		return strb.toString();
							
	}
		



	@Override
	public void setMaxVokabeln(int n) {
		maxCount = n;
	}


}
