package com.eb.misc;

import java.util.List;
import java.util.stream.Collectors;

import com.eb.woerterbuch.gobj.Vokabel;

public abstract class NewSearcher {
	IIndexer<Vokabel> indexer;
	List<Vokabel> grundliste;

	public abstract boolean matches(Vokabel obj, String unifiedWord);
	public abstract IIndexer<Vokabel> createIndexer();
	
	public List<Vokabel> getMatchingElements(String unifiedString)
	{
		List<Vokabel> elements = indexer.getElements(unifiedString);
		return elements.parallelStream().filter(v->matches(v, unifiedString)).collect(Collectors.toList());
	}
	
	
	public IIndexer<Vokabel> getIndexer() {
		return indexer;
	}
	public void setIndexer(IIndexer<Vokabel> indexer) {
		this.indexer = indexer;
	}
	public List<Vokabel> getGrundliste() {
		return grundliste;
	}
	public void setGrundliste(List<Vokabel> grundliste) {
		this.grundliste = grundliste;
		indexer.setGrundliste(grundliste);
	}
}
