package com.eb.misc;

import java.util.List;

public interface IGeneralSearcher<T> {

	public String getLabel();
	public IDictionarySearchStrategy<T> getStrategy();
	public List<T> getMatchingEntries(String word);
}
