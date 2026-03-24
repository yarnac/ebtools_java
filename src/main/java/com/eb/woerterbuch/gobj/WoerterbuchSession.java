package com.eb.woerterbuch.gobj;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.eb.base.SearcherPair;
import com.eb.misc.DictionarySearchStrategyFactory;
import com.eb.misc.DictionarySearcher;
import com.eb.misc.IDictionarySearchStrategy;
import com.eb.misc.IGeneralSearcher;
import com.eb.misc.SearcherWithDictionary;
import com.eb.misc.WordSplitter;
import com.eb.system.StringUnifier;
import com.eb.system.WordStack;

public class WoerterbuchSession {
	
	private Woerterbuch woerterbuch;
	private WbDirection direction;
	List<SearcherPair<Vokabel>> searcher = new ArrayList<>();
	private SearcherPair<Vokabel> theSearcher;
	private WordStack stack;
	private String stackFilename;
	
	
	public WoerterbuchSession(String aktSprache, String source) {
		WbDirection dir = "A".equals(source) ? WbDirection.A_Nach_B : WbDirection.B_Nach_A;
		
		initialize(aktSprache, null, dir);
	}
		

	public WoerterbuchSession(String aktSprache, String newStackfilename, WbDirection dir, IDictionarySearchStrategy<Vokabel> newSearcher) {
			
		initialize(aktSprache, newStackfilename, dir);
	}


	private void initialize(String aktSprache, String newStackfilename, WbDirection dir) {
		Woerterbuch wb = WoerterbuchManager.getCurrent().getWoerterbuch(aktSprache);
		woerterbuch = wb;
		setDirection(dir);
		stackFilename = newStackfilename;
			
		List<IGeneralSearcher<Vokabel>> searcherA;
		List<IGeneralSearcher<Vokabel>> searcherB;
		
		searcherA = createSearcher(wb.getVokabelnAnachB().getVokabeln());
		searcherB = createSearcher(wb.getVokabelnBnachA().getVokabeln());
	
		for (int i=0;i<searcherA.size();i++)
		{			
			searcher.add(new SearcherPair<>(searcherA.get(i), searcherB.get(i)));
		}
		
		setTheSearcher(searcher.get(0));
	}
	
	

	private List<IGeneralSearcher<Vokabel>> createSearcher(List<Vokabel> vokabeln) {
	
		List<IGeneralSearcher<Vokabel>> res = new ArrayList<>();
		
		DictionarySearcher<Vokabel> searcher = new DictionarySearcher<Vokabel>();
		searcher.setGrundliste(vokabeln);
		
		List<IDictionarySearchStrategy<Vokabel>> searchStrategies = DictionarySearchStrategyFactory.getDirectorySearcher();
		searcher.addMatcher(searchStrategies);
		
		
		for (IDictionarySearchStrategy<Vokabel> iDictionarySearchStrategy : searchStrategies) {
			res.add(new SearcherWithDictionary<>(searcher, iDictionarySearchStrategy));
		}
		
		//res.add(new CombinedSearcher<Vokabel>("Index oder enthält",getSearcher(res, "Indexsuche"),getSearcher(res, "enthält")));
		//res.add(new CombinedSearcher<Vokabel>("Start,Index,enthält",getSearcher(res, "beginnt mit"), getSearcher(res, "Indexsuche"), getSearcher(res, "enthält")));
		
		
		return res;
	}



	public IGeneralSearcher<Vokabel> getSearcher(List<IGeneralSearcher<Vokabel>> res, String string) {		
		return res.stream().filter(x->x.getLabel().equals(string)).findFirst().get();
	}



	public String getSprache() {
		return woerterbuch.getSpracheA();
	}

	public WbDirection getDirection() {
		if (direction==null)
			return WbDirection.A_Nach_B;
		return direction;
	}

	public void setDirection(WbDirection direction) {
		this.direction = direction;
	}

	public List<SearcherPair<Vokabel>> getSearcher() {
		return searcher;
	}

	public void setSearcher(List<SearcherPair<Vokabel>> searcher) {
		this.searcher = searcher;
	}

	public List<Vokabel> fetchVokabeln(String string) {
		
	
		String unifiedString = StringUnifier.getUnifiedString(string);
		
		if (getWoerterbuch().getFileName().contains("Franz"))
			{
				if (unifiedString.length()>3 && unifiedString.charAt(1)=='\'')
					unifiedString = unifiedString.substring(2);
				if (unifiedString.length()>4 && unifiedString.charAt(2)=='\'')
					unifiedString = unifiedString.substring(3);
			}
			
		
		WordSplitter wortSplitter = WordSplitter.createWortSplitter();
		List<String> split = wortSplitter.split(unifiedString);
		
		if (split.size()==0)
			return new ArrayList<>();
		Stream<Vokabel> stream = fetch_Vokabeln(split.get(0)).stream();
				
		for (int i=1; i<split.size();i++) {
			
			List<Vokabel> nextRes = fetchVokabeln(split.get(i));
			stream = stream.filter(x->nextRes.contains(x));								
		}
		
		
		return stream.collect(Collectors.toList());			
	}



	private List<Vokabel> fetch_Vokabeln(String unifiedString) {
		List<Vokabel> result = new ArrayList<Vokabel>();
		
		int len = unifiedString.length();
		do
		{
			String str = unifiedString.substring(0, len);
			
			
			if (direction!=WbDirection.B_Nach_A) {
				List<Vokabel> matchingVokabeln = theSearcher.getMatchingA(str);
				sort(matchingVokabeln);				
				result.addAll(matchingVokabeln);
			}
			
			if (direction!=WbDirection.A_Nach_B)
			{
				List<Vokabel> matchingVokabeln = theSearcher.getMatchingB(str);
				sort(matchingVokabeln);				
				result.addAll(matchingVokabeln);
			}
				
			len--;
		}while (result.isEmpty() && len>=1);
		
		return result;
	}

	private void sort(List<Vokabel> matchingVokabeln) {
		matchingVokabeln.sort((x,y)->CompareVokabeln(x, y));
	}



	private int CompareVokabeln(Vokabel x, Vokabel y) {
		int n = x.getUnifiedWort().length() - y.getUnifiedWort().length();
		if (n!=0)
			return n;
		
		return x.getUnifiedWort().compareTo(y.getUnifiedWort());
	}



	public void addVokabel(Vokabel v) {
		addVokabelToVokabellisten(v,false);
		addVokabelToSearchers(v);
	}
	
	public void replaceVokabel(Vokabel v) {
		addVokabelToVokabellisten(v, true);
		addVokabelToSearchers(v);
	}



	private void addVokabelToVokabellisten(Vokabel v, boolean replace) {
		VokabelListe l1;
		VokabelListe l2;
		if (direction == WbDirection.B_Nach_A)
		{
			l2 = woerterbuch.getVokabelnAnachB();
			l1 = woerterbuch.getVokabelnBnachA();
		}
		else
		{
			l1 = woerterbuch.getVokabelnAnachB();
			l2 = woerterbuch.getVokabelnBnachA();
		}
		
		l1.addAdditionalVokabel(v, replace);
		l2.AddVokabelBedeutungen(v);
	}

	public void deleteVokabel(String wort) {
		VokabelListe l1;
		VokabelListe l2;
		if (direction == WbDirection.B_Nach_A)
		{
			l2 = woerterbuch.getVokabelnAnachB();
			l1 = woerterbuch.getVokabelnBnachA();
		}
		else
		{
			l1 = woerterbuch.getVokabelnAnachB();
			l2 = woerterbuch.getVokabelnBnachA();
		}
		
		Vokabel vokabel = l1.getVokabel(wort);
		if (vokabel==null)
			return;
		
		l1.deleteVokabel(vokabel);
		l2.deleteVokabelBedeutungen(vokabel);
		
		this.woerterbuch.save();
	}


	private void addVokabelToSearchers(Vokabel v) {
		for (SearcherPair<Vokabel> searcherPair : searcher) {
			
			if ( !(searcherPair.getSeacherA() instanceof SearcherWithDictionary<?>))
				return;			
			SearcherWithDictionary<Vokabel> s1,s2;			
			
			if (direction == WbDirection.B_Nach_A)
			{
				s1 = (SearcherWithDictionary<Vokabel>)searcherPair.getSeacherB();
				s2 = (SearcherWithDictionary<Vokabel>)searcherPair.getSeacherA();
				
			}
			else
			{
				s1 = (SearcherWithDictionary<Vokabel>)searcherPair.getSeacherA();
				s2 = (SearcherWithDictionary<Vokabel>)searcherPair.getSeacherB();
			}
			
			s1.addElement(v);
			v.getReverse().stream().forEach(vv->s2.addElement(vv));					
		}
	}

	public Woerterbuch getWoerterbuch() {
		return woerterbuch;
		
	}
	
	public String previousSuchwort() {
		return null;
	}

	public String nextSuchwort() {
		return null;
	}

	public SearcherPair<Vokabel> getTheSearcher() {
		return theSearcher;
	}

	public void setTheSearcher(SearcherPair<Vokabel> theSearcher) {
		this.theSearcher = theSearcher;
	}



	public void setTheSearcherForName(String aktModus) {
		Optional<SearcherPair<Vokabel>> findFirst = getSearcher().stream().filter(x->x.toString().equals(aktModus)).findFirst();
		SearcherPair<Vokabel> pair = findFirst.isPresent() ? findFirst.get() : getSearcher().get(0);
		setTheSearcher(pair);
	}



	public SearcherPair<Vokabel> getSearcherPair(String string) {
		for (SearcherPair<Vokabel> searcherPair : searcher) {
			if (searcherPair.toString().equals(string))
				return searcherPair;
		}
		
		return null;
	}


	private String getStackFileName() {
		return stackFilename;		
	}
	

	public WordStack getStack() {
		if (stack==null)
			stack = new WordStack();
		return stack;
	}

	public void setStack(WordStack stack) {
		this.stack = stack;
	} 



	

}
