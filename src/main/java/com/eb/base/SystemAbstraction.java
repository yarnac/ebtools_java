package com.eb.base;

import com.eb.system.StringUnifier;

import java.util.List;

public interface SystemAbstraction {
		
	public static boolean hasUmlaute(List<String> lines) {
		/*
		Optional<String> findFirst 
		= lines.stream().filter( x->(3 + x.indexOf('ä') + x.indexOf('ö') + x.indexOf('ü')) > 0 ).findFirst();
		if (findFirst.isPresent())
		{
			findFirst.get().toString();
			return true;
		}
		 */
		return false;		
	}

	@SuppressWarnings("nls")
	public static String getStringNameOhneUmlaute(String fn) {
		return StringUnifier.getUnifiedString(fn);
	}
	
	
	

}
