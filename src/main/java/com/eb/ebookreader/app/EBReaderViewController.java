package com.eb.ebookreader.app;

import com.eb.base.EbAppContext;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.FileUtil;
import com.eb.ebookreader.gobj.BookReader;
import com.eb.ebookreader.gobj.BookSession;
import com.eb.ebookreader.gui.*;
import com.eb.ebookreader.tobj.EbStringUtil;
import com.eb.woerterbuch.gobj.Vokabel;
import com.eb.woerterbuch.gobj.WoerterbuchManager;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class EBReaderViewController  {
		
	IniFile iniFile;
	private boolean ignoreBookselection;
	private JComboBox<String> cbBuecher;	
	private BookSession bookSession;
	private String sessionName;
	private EbReaderView view;
	


	/**
	 * Launch the application.
	 */

	/**
	 * Create the application.
	 */
	public EBReaderViewController(int n) {
		// java.awt.Taskbar.getTaskbar().setIconImage(IC.eBookReader.getImage32());
		setView(new EbReaderView(this, n));		
		loadAppl();							
		decorateToolbar();		
		initViewEvents();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				loadLastBook();
			}
		});
		
	}

	private void initViewEvents() {
		
		addFrameWindowListener();
		
		EbReaderView vw = getView();			
		
		vw.setScrollUnitIncrement(19);				
		
		vw.setIShowUebersetzung((x,y)->showUebersetzung(x.fetchVokabeln(y)));		
		vw.setHandleUebersetungsRequest(x->Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(x), null));
		
		vw.setHandleCommonPointSaveRequest((x,y,z)->saveCommonPoint(x,y,z));
	}
	
	private void loadAppl() {
		System.out.println("Load IniFile");
		iniFile = IniFileProvider.createIniFile(EbAppContext.getJavaDataFilename("Reader/EbReader.ini"));
		System.out.println("Loaded IniFile");
	}

	private void saveCommonPoint(int x, int y, boolean add) {
		bookSession.saveCommonPoint(getChapterNr(),x,y, add);
	}

	private void showUebersetzung(List<Vokabel> fetchVokabeln) {		
		
		StringBuilder strb = new StringBuilder();
		int wordLength = EbStringUtil.calcMaxWordLength(fetchVokabeln, v->v.getWort()) + 2;
			
			
		for (Vokabel vokabel : fetchVokabeln) {
			strb.append(vokabel.toStringWithBedeutungen(wordLength)+"\n");
		}			
		TooltipFrame.show(strb.toString());
	}

	private void decorateToolbar() {
		GuiDecorator decorator = new GuiDecorator(getView().getFrame(), getIniFile(), "Einstellungen");
		
		decorator.addContainer("Upper", getView().getFrame().getTbUpper());
		
		decorator.addTopMostButton("Upper", getView().getFrame());
		decorator.addEditIniFileButton("Upper", getIniFile().getFileName());
		decorator.addToolbarButton("Upper", "Edit Booksession", IC.TABLE, x->editSessionFile());
		decorator.addToolbarButton("Upper", "Test Zipper", IC.HELP_BLUE, x->ShowHelp());
		
		
		decorator.addToolbarButton("Upper", "Reload", IC.REFRESHPAGE, x->getView() .resetText());
		decorator.addToolbarButton("Upper", "Speichern", IC.SAVE_ADD, x->saveChanges());
		
		decorator.addToolbarButton("Upper", "Vorheriges Kapitel", IC.MB_REVERSE, x->nextChapter(-1));
		
		decorator.addToolbarButton("Upper", "Test Zipper", IC.Dictionary_HW, x->testZipper());
		
		decorator.addToolbarButton("Upper", "Nächstes Kapitel", IC.MB_PLAY, x->nextChapter(1));
		
		decorator.addToolbarButton("Upper", "Seite zurück", IC.MB_DOWN_BLUE, x->getView().nextPage(-1));
		decorator.addToolbarButton("Upper", "Seite vor", IC.MB_UP_BLUE, x->getView().nextPage(1));		
		
		decorator.addToolbarSeparator("Upper");
		
		decorator.addToolbarButton("Upper", "Analyzer starten", IC.TOOLS, x->startAnalyzer());
		
		decorator.addToolbarSeparator("Upper");
		
		cbBuecher = decorator.addToolbarComboBox("Upper", "Bücher", iniFile.getSectionValues("BOOKS"), x->bookChanged(x));
		
		
		
		decorator.addToolbarButton("Upper", "Start eclipse", IC.BOX_FLOW, x->startEclipse());		
		decorator.addToolbarButton("Upper", "Test Zipper", IC.Calendar, x->testZipper());
	}
	
	private Object startAnalyzer() {
		//Analyzer analyzer = new Analyzer(view.getTextLeft(), view.getTextRight());
		return null;
	}

	private void startEclipse() {
		FileUtil.start("c:\\apps\\eclipse\\eclipse.exe");
	}

	private void editSessionFile() {
		String inifileName = bookSession.getInifileName();
		FileUtil.open(inifileName);
	}

	private void bookChanged(String x) {
		if (ignoreBookselection)
			return;
		
		saveBookStatus();
			
		iniFile.setSectionValue("Einstellungen", "LastBook", x);
		
		
		loadLastBook();  								
		iniFile.Write(); 
	}
	
	int oldCount = 2;
	
	Map<String, BookSession> sessionsDict = new HashMap<>();
	public void loadLastBook()
	{		
		WoerterbuchManager.setWoerterbuchPfad(EbAppContext.getJavaDataFilename("Woerterbuch/"));
		sessionName = iniFile.getSectionValue("Einstellungen","LastBook",iniFile.getSectionValues("Books").get(0));
		bookSession = sessionsDict.computeIfAbsent(sessionName, x-> BookReader.readBookSession(x));
		
		int count = bookSession.getBookControllers().size();
		
		if (oldCount!=count)
		{
			getView().getFrame().setVisible(false);
			setView(new EbReaderView(this,  count));
			getView().getFrame().setVisible(true);
			oldCount = count;
			
			loadAppl();							
			decorateToolbar();		
			initViewEvents();
		}
			
		
		//getView().createColumns(count);
		
		bookSession.setBookViews(getView().getFrame().getBookViews());
		
		getView().setBookControllers(bookSession.getBookControllers());		
		getView().SetKeyListeners();
		
		
		
		ignoreBookselection = true;
		
		cbBuecher.setSelectedItem(sessionName);
		transferModelToView(true);
		ignoreBookselection = false;
	}
		
	private void saveBookStatus() {
		BookReader.store(bookSession);
	}

	private void saveChanges() {		
		
		bookSession.saveChanges();
		
	
		saveBookStatus();
		// sessionsDict.remove(sessionName);
		// loadLastBook();
	}
			
	private Object nextChapter(int i) {
		setChapterNr(getChapterNr() + i);
		transferModelToView(false);
		return null;
	}	

	
	private void transferModelToView(boolean b) {		
		
		
		int chapterNr = getChapterNr();
		
		for (BookCtrl bc : bookSession.getBookControllers()) {
			bc.setChapterText(chapterNr);
			
		}
		
		getView().setCommonPoints(bookSession.getCommonPoints(chapterNr));
		
		EventQueue.invokeLater(()-> {			
				try {
					if (bookSession!=null)
					{
						for (BookCtrl bc : bookSession.getBookControllers()) {
							bc.getView().getPane().getVerticalScrollBar().setValue(bc.getScroll());						
						}						
					}
				} catch (Exception e) {
					e.printStackTrace();
				}});
	}

	@SuppressWarnings("unchecked")
	private void testZipper() {
		try {
			ZipFile file = new ZipFile("~/Calibre-Bibliothek/Alexandre Dumas/Monte Kristo Kontu (173)/Monte Kristo Kontu - Alexandre Dumas.epub");
			 for (Enumeration<ZipEntry> e = (Enumeration<ZipEntry>) file.entries(); e.hasMoreElements();) {
				 ZipEntry o = e.nextElement();
				 o.toString();
			 }
			 file.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private void addFrameWindowListener() {
		view.getFrame().addWindowListener(new WindowListener() {
						
			@Override
			public void windowOpened(WindowEvent e) {					
				// loadLastBook();				
			} 
			
			@Override
			public void windowClosed(WindowEvent e) {
										
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				saveBookStatus();
			}			

			@Override
			public void windowIconified(WindowEvent e) {}
			
			@Override
			public void windowDeiconified(WindowEvent e) {}
			
			@Override
			public void windowDeactivated(WindowEvent e) {}				
						
			@Override
			public void windowActivated(WindowEvent e) {}
		});
	}

	public IniFile getIniFile() {
		return iniFile;
	}

	public EbReaderView getView() {
		return view;
	}

	public void setView(EbReaderView view) {
		this.view = view;
	}

	public int getChapterNr() {
		if (bookSession!=null)
			return bookSession.getChapterNr();
		return 0;
	}

	public void setChapterNr(int chapterNr) {
		if (bookSession!=null)
			bookSession.setChapterNr(chapterNr);
	}
	public void ShowHelp() {
		FileUtil.open(BookReader.getReaderFilename("EbReaderInfo.txt"));
		
	}
}
