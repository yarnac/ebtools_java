package com.eb.misc;

import java.util.List;

public class SearcherWithDictionary<T> implements IGeneralSearcher<T>{
	
	private DictionarySearcher<T> searcher;
	private IDictionarySearchStrategy<T> strategy;
	
	public SearcherWithDictionary(DictionarySearcher<T> _searcher, IDictionarySearchStrategy<T> _strategy)
	{
		setSearcher(_searcher);
		setStrategy(_strategy);
	}
	

	@Override
	public String getLabel() {
		
		return getStrategy().getLabel();
	}

	@Override
	public List<T> getMatchingEntries(String word) {
		
		return getSearcher().getMatchingVokabeln(getStrategy(), word);
	}


	public DictionarySearcher<T> getSearcher() {
		return searcher;
	}
	
	public void addElement(T element)
	{
		searcher.addElement(element);
	}


	public void setSearcher(DictionarySearcher<T> searcher) {
		this.searcher = searcher;
	}


	@Override
	public IDictionarySearchStrategy<T> getStrategy() {
		return strategy;
	}


	public void setStrategy(IDictionarySearchStrategy<T> strategy) {
		this.strategy = strategy;
	}

}
