
package com.eb.misc;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.stream.Collectors;

import com.eb.system.StringUnifier;

public class DictionarySearcher<T> {
	Dictionary<String, List<T>> dictionary = new Hashtable<>();
	private List<T> grundliste;
	List<IDictionarySearchStrategy<T>> listOfSearcher = new ArrayList<IDictionarySearchStrategy<T>>();
	
	public void setGrundliste( List<T> liste)
	{
		grundliste = liste;
	}
	
	/**
	 * Jeder Search erzeugt eigenen Keys, die Listen werden in einem gemeinsamen Dictioanary geführt.
	 * @param theListOfSearcher
	 */ 
	public void addMatcher( List<IDictionarySearchStrategy<T>> theListOfSearcher)
	{
		
		listOfSearcher = theListOfSearcher;
		
		for (IDictionarySearchStrategy<T> searcher : theListOfSearcher) {
			
			addMatcherToList(grundliste, searcher);
			
		} 
	}
	
	

	private void addMatcherToList(List<T> liste, IDictionarySearchStrategy<T> searcher) {
		liste.parallelStream().forEach(x->addElement(x));		
		//for (T element : liste) {			
		//	addElement(searcher, element);
		//}
	}
	
	public void addElement(T element)
	{
		for (IDictionarySearchStrategy<T> searcher : listOfSearcher) {
			addElement(searcher, element);
		}
	}
	

	private void addElement(IDictionarySearchStrategy<T> searcher, T element) {
		if (!searcher.isUsingOwnIndex())
			return;
		
		
		if (searcher.isUsingGrundliste())
			return;
		
		String k = searcher.getKey(element);
		
		List<String> keys = searcher.getKeys(element);	
		for (String key : keys) 
		{					
			List<T> lst = searcher.dictionary.get(key);
			if (lst==null)
			{
				lst = new ArrayList<T>();			
				AddToDictionary(key, lst, searcher);				
			}
			AddToListe(element, lst, searcher);
			
				
		}
		return;
	}

	private synchronized void AddToListe(T element, List<T> lst, IDictionarySearchStrategy<T> searcher) {
		 if (!vokabelIstVorhanden(searcher, lst, element))
			 lst.add(element);
	}

	private synchronized void AddToDictionary(String key, List<T> lst, IDictionarySearchStrategy<T> strategy) {
		strategy.dictionary.put(key, lst);
	}
	
	private boolean vokabelIstVorhanden(IDictionarySearchStrategy<T> searcher, List<T> lst, T element) {
		for (T t : lst) {
			
			
			
			if (searcher.elementsAreEqual(t, element))
				return true;
		}
		return false;
	}

	public List<T> getMatchingVokabeln(IDictionarySearchStrategy<T> searcher, String word)
	{
		return getMatchingVokabelnUnified(searcher, StringUnifier.getUnifiedString(word));
	}
	
	
	private List<T> getMatchingVokabelnUnified(IDictionarySearchStrategy<T> searcher, String unifiedString) {
		List<T> result = new ArrayList<>();
		if (searcher.isUsingGrundliste())
			result = grundliste;
		else
		{
			List<String> keys = searcher.getKeys(unifiedString);
			for (String string : keys) {
				result = searcher.dictionary.get(string);	
			}
		}
		
		if (result==null)
			return new ArrayList<T>() ;
		
		return result
				.stream()
				.filter(obj -> searcher.matches(obj, unifiedString))
				.collect(Collectors.toList());	
	}
}



