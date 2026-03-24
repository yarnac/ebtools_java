package com.eb.misc;

import com.eb.woerterbuch.gobj.Vokabel;

public class NewSearcherFactory {
	
	public static NewSearcher createFirsCharsSearcher () {
		return new NewSearcher() {
	
			@Override public boolean matches(Vokabel obj, String unifiedWord) {return obj.getUnifiedWort().startsWith(unifiedWord);}
			@Override public IIndexer<Vokabel> createIndexer() {return VokabelIIndexerFactory.createFirstCharsIndex();}
			
		};
	}

}
