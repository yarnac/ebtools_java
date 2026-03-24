package com.eb.ebookreader.gobj;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.ebookreader.app.EBReaderViewController;
import com.eb.ebookreader.gui.BookCtrl;
import com.eb.ebookreader.gui.BookView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class BookSession {
	private String name;
	private List<BookCtrl> bookControllers;
	
	private EBReaderViewController viewController;
	
	private IniFile iniFile;
	
	private int chapterNr;
	private int scrollLeft;
	private int scrollRight;
	private int scrollRight2;
	
	
	public BookSession(String sessionName) {
		name = sessionName;
		String filename = BookReader.getSessionFilename(name); 
		setIniFile(IniFileProvider.createIniFile(filename));
		getIniFile().Read();
		
		bookControllers = new ArrayList();
		String books = getIniFile().getSectionValue("Session", "Books", "");
		String[] parts = books.split("," );
		addBookController(parts[0]);
		if (parts.length>1)
			addBookController(parts[1]);
		if (parts.length>2)
			addBookController(parts[2]);
		
					
		chapterNr = getIniFile().getSectionValueAsInteger("Einstellungen", "Chapter", 0);
	}
	
	public void saveChanges() {
		for (BookCtrl bookCtrl : getBookControllers()) {
			bookCtrl.writeTo(getIniFile());
		}
		
	}

	
	public void store()
	{
		for (BookCtrl bookCtrl : getBookControllers()) {
			bookCtrl.store();
			bookCtrl.writeTo(getIniFile());
			
		}
		getIniFile().setSectionValue("Einstellungen", "ScrollLeft", scrollLeft);
		getIniFile().setSectionValue("Einstellungen", "ScrollRight", scrollRight);
		if (hasSecondBook())
			getIniFile().setSectionValue("Einstellungen", "ScrollRight2", scrollRight2);
		
		getIniFile().setSectionValue("Einstellungen", "Chapter", chapterNr);
		getIniFile().Write();
	}
	
	
	public List<Point> getCommonPoints(int chapterNr)
	{
		List<String> sectionValues = getIniFile().getSectionValues(getCommonPointSectionName(chapterNr));
		if (sectionValues==null)
			sectionValues = new ArrayList<>();
		List<Point> res;
		res = sectionValues.stream().map(x->createPoint(x)).collect(Collectors.toList());		
		return res;
	}
	
	
	
	
	private Point createPoint(String x) {
		String[] pairs = x.split(",");
		return new Point(Integer.parseInt(pairs[1]), Integer.parseInt(pairs[0]));
	}


	private String getCommonPointSectionName(int chapterNr) {
		return "CommonPoints Chapter " + chapterNr;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public void saveCommonPoint(int chapterNr, int y, int x, boolean add) {		
		String sectionName = getCommonPointSectionName(chapterNr);
		
		List<String> sectionValues = getIniFile().getSectionValues(sectionName);
		if (sectionValues==null)
			sectionValues = new ArrayList<>();
		if (add)
			sectionValues.add(""+x+","+y);
		else
			sectionValues.remove(""+x+","+y);
		getIniFile().setSectionValues(sectionName, sectionValues);
		getIniFile().Write();		
	}

	public void addBookController(String section) {
												 	
		BookCtrl bc = new BookCtrl();
		bc.setSection(section);		
		bc.readFrom(getIniFile());			
		bookControllers.add(bc);
	}

	public String getInifileName() {
		getIniFile().Write();
		return getIniFile().getFileName();
		
	}


	public boolean isLoaded() {
		return getBookControllers().size()>0;
	}


	public int getChapterNr() {
		return chapterNr;
	}


	public void setChapterNr(int chapterNr) {
		this.chapterNr = chapterNr;
	}

	public IniFile getIniFile() {
		return iniFile;
	}

	public void setIniFile(IniFile iniFile) {
		this.iniFile = iniFile;
	}

	public boolean hasSecondBook() {

		return getBookControllers().size()==3l;
	}

	public List<BookCtrl> getBookControllers() {
		return bookControllers;
	}

	public void setBookControllers(List<BookCtrl> bookControllers) {
		this.bookControllers = bookControllers;
	}

	public void setBookViews(List<BookView> bookViews) {
		for (int i=0; i<bookViews.size();i++)
		{
			if (bookControllers.size()>i)
				bookControllers.get(i).setView(bookViews.get(i));
			else
				bookViews.get(i).getPane().setVisible(false);
		}
	}

	public EBReaderViewController getViewController() {
		return viewController;
	}

	public void setViewController(EBReaderViewController viewController) {
		this.viewController = viewController;
	}


}
