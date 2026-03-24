package com.eb.system;

public class StringUnifier {
	private static String[] UnifyPatterns = new String[] {  "ıi", "çc", "şs", "ğg", "áa", "àa", "êe", "üu", "äa", "öo", "ãa", "óo", "úu", "òo", "ôo", "çc", "âa", "ûu", "ñn", "íi", "ée", "èe", "ãa" };
	private static String[] UmlautePatterns = new String[] {  "äae", "üue", "öoe", "ßss"};

	public static String getUnifiedString(String str)
    {
		if (str==null)
			return "";
        str = str.replace("İ","i").toLowerCase();
        for (String string : UnifyPatterns) {        	
        	str = str.replace(string.charAt(0), string.charAt(1));
		}
       
        return str;
    }

	public static String getStringOhneUmlaute(String str)
	{
		if (str==null)
			return "";
		for (String string : UmlautePatterns) {
			str = str.replace(string.substring(0,1), string.substring(1));
		}

		return str;
	}
}
