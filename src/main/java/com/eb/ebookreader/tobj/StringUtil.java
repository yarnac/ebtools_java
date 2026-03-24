package com.eb.ebookreader.tobj;

public class StringUtil {
	
	private static String substringBefore(String word, String string, boolean includePattern, boolean toLower, IGetIndexOf i)
	{		
		if (word==null)
			return word;
		
		int index;
		
		if (toLower)
			index = i.indexOf(word.toLowerCase(), string.toLowerCase());
		else
			index = i.indexOf(word, string);
		
		if (index<0)
			return word;
		
		if (includePattern)
			index += string.length();
		
		return word.substring(0, index);
	}
	
	private static String substringAfter(String word, String string, boolean includePattern, boolean toLower, IGetIndexOf i)
	{		
		if (word==null)
			return word;
		
		int index;
		
		if (toLower)
			index = i.indexOf(word.toLowerCase(), string.toLowerCase());
		else
			index = i.indexOf(word, string);
		
		if (index<0)
			return word;
		
		if (!includePattern)
			index += string.length();
		
		return word.substring(index);
	}

	public static String substringBeforeFirst(String word, String string, boolean b) {
		return substringBefore(word, string, b, false, (x,y)->x.indexOf(y));
	}
	
	public static String substringBeforeLast(String word, String string, boolean b) {
		return substringBefore(word, string, b, false, (x,y)->x.lastIndexOf(y));
	}
	
	public static String substringAfterFirst(String word, String string, boolean b) {
		return substringAfter(word, string, b, false, (x,y)->x.indexOf(y));
	}
	
	public static String substringAfterLast(String word, String string, boolean b) {
		return substringAfter(word, string, b, false, (x,y)->x.lastIndexOf(y));
	}

    public static boolean isNullOrEmpty(Object value) {
		if (value == null)
			return true;
		return value.toString().trim().length() == 0;
    }
}
