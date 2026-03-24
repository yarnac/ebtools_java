package com.eb.misc;


public class EbUtils {
	
	public static String substringAfterLast(String s1, String pattern, boolean includePattern)
	{
		int n = s1.lastIndexOf(pattern);
		if (n<0)
			return s1;
		
		if (!includePattern)
			n += pattern.length();
		
		return s1.substring(n);		
	}
	
	public static String substringBeforeLast(String s1, String pattern, boolean includePattern)
	{
		int n = s1.lastIndexOf(pattern);
		if (n<0)
			return s1;
		
		
		
		if (includePattern)
			n += pattern.length();
		
		return s1.substring(0, n);		
	}
	
	public static String localFileName(String fileName)
	{
		return substringAfterLast(fileName, "\\", false);
	}
	
	public static String localFileRoot(String fileName)
	{
		return substringBeforeLast( localFileName(fileName), ".", false);
	}


    public static String getTrimmedString(String filterString) {
		return filterString==null ? "" : filterString.trim();
    }
}
