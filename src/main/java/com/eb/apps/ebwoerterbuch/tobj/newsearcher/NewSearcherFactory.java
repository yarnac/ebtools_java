package com.eb.apps.ebwoerterbuch.tobj.newsearcher;

import com.eb.apps.ebwoerterbuch.gobj.Vokabel;
import com.eb.apps.ebwoerterbuch.tobj.searcher.IIndexer;

public class NewSearcherFactory {
	
	public static NewSearcher createFirsCharsSearcher () {
		return new NewSearcher() {
	
			@Override public boolean matches(Vokabel obj, String unifiedWord) {return obj.getUnifiedWort().startsWith(unifiedWord);}
			@Override public IIndexer<Vokabel> createIndexer() {return VokabelIIndexerFactory.createFirstCharsIndex();}
			
		};
	}

}
