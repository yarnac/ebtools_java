package com.eb.base;

import java.util.List;

import com.eb.misc.IDictionarySearchStrategy;
import com.eb.misc.IGeneralSearcher;

public class SearcherPair<T> {
	private IDictionarySearchStrategy<T> searchStrategy;
	private IGeneralSearcher<T> searcherA;
	private IGeneralSearcher<T> searcherB;
	
	@Override
	public String toString()
	{
		return searcherA.getLabel();
	}
	
	public List<T> getMatchingA(String word)
	{
		return searcherA.getMatchingEntries(word);
	}
	
	public List<T> getMatchingB(String word)
	{
		return searcherB.getMatchingEntries(word);
	}
	
	
	
	public SearcherPair (IGeneralSearcher<T>a,IGeneralSearcher<T>b)
	{
		searcherA=a;
		searcherB=b;
	}
	
	public IDictionarySearchStrategy<T> getStrategy()
	{
		return searcherA.getStrategy();
	}
	public IGeneralSearcher<T> getSeacherA() {
		return searcherA;
	}
		
	public void setSeacherA(IGeneralSearcher<T> seacherA) {
		this.searcherA = seacherA;
	}
	public IGeneralSearcher<T> getSeacherB() {
		return searcherB;
	}
	public void setSeacherB(IGeneralSearcher<T> seacherB) {
		this.searcherB = seacherB;
	}

	public IDictionarySearchStrategy<T> getSearchStrategy() {
		return searchStrategy;
	}

	public void setSearchStrategy(IDictionarySearchStrategy<T> searchStrategy) {
		this.searchStrategy = searchStrategy;
	}

}
