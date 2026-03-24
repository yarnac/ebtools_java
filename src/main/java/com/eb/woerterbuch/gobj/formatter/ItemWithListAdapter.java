package com.eb.woerterbuch.gobj.formatter;

import java.util.List;
import java.util.function.Function;

public class ItemWithListAdapter <T> {
	private Function<T, String> getNameFunction;
	private Function<T, List<String>> getListFunction;	
	
	public ItemWithListAdapter (Function<T, String> name, Function<T, List<String>> liste)
	{
		getNameFunction = name;
		getListFunction = liste;
	}
	
	public String getName(T obj)
	{
		return getNameFunction.apply(obj);
	}
	
	public List<String> getStrings(T obj)
	{
		return getListFunction.apply(obj);
	}
}
