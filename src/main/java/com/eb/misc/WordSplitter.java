package com.eb.misc;

import java.util.ArrayList;
import java.util.List;

public class WordSplitter {
	
	public static WordSplitter createWortSplitter()
	{
		return new WordSplitter();
	}
	
	
	IIsLetter isLetter = ch -> Character.isAlphabetic(ch);
	IPrepareString prepareString = new IPrepareString() {
		
		@Override
		public String prepare(String string) {
			StringBuilder strb = new StringBuilder();
			for (char ch : string.toCharArray()) {
				if (ch==' '|| isLetter.isLettr(ch))
					strb.append(ch);				
			}
			
			return strb.toString();
			
		}
	};
	
	
	
	
	
	@FunctionalInterface 
	interface IIsLetter
	{
		boolean isLettr(char ch);				
	}
	
	@FunctionalInterface 
	interface IPrepareString
	{
		String prepare(String ch);		
	}
	
	public List<String> split(String input)
	{
		List<String> result = new ArrayList<String>();
		int index = 0;
		
		String stringToSplit = prepareString.prepare(input);
		StringBuilder strb = null;
		
		for (char ch : input.toCharArray()) {
			if (isLetter.isLettr(ch))
			{
				if (strb==null)					
					strb = new StringBuilder();
				strb.append(ch);									
			}
			else 
			{												
				addWord(result, strb);
				strb = null;
			}								
		}
		addWord(result, strb);
		
		
		return result;
	}

	private void addWord(List<String> result, StringBuilder strb) {
		if (strb==null)
			return;
		if (strb.length()==1)
		{
			switch(strb.charAt(0))
			{
				case 'm':
				case 'n':
				case 'f':
					return;
				default:
					;										
			}
		}
					
		result.add(strb.toString());		
	}

	
	
	
	
}
