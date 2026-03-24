package com.eb.misc;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public abstract class IIndexer<T> {
	public abstract String getKey(T obj);
	public abstract String getKey(String unifiedString);
	private Dictionary<String,List<T>> index = new Hashtable<String, List<T>>();

	private List<T> grundliste;
	
	public List<String> getKeys(T obj)
	{
		List<String> res = new ArrayList<String>();
		res.add(getKey(obj));
		return res; 
	}
	
	public List<String> getKeys(String s)
	{
		List<String> res = new ArrayList<String>();
		res.add(getKey(s));
		return res; 
	}
	
	public void Add(T element)
	{
		getKeys(element).stream().forEach(x->Add(x, element));
	}
	
	private void  Add(String x, T element) {
		List<T> lst = index.get(x);
		if (lst==null)
		{
			lst = new ArrayList<>();
			index.put(x, lst);
		}
		lst.add(element);
	}
	
	public List<T> getElements(String key)
	{
		List<T> list = index.get(key);
		if (list==null)
			list = new ArrayList<>();
		return list;
	}
	public List<T> getGrundliste() {
		return grundliste;
	}
	public void setGrundliste(List<T> grundliste) {
		this.grundliste = grundliste;
		for (T t : grundliste) {
			Add(t);
		}
	}
	
		
}
