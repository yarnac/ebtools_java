package com.eb.ebookreader.gui;

import com.eb.base.inifile.api.IniFile;
import com.eb.ebookreader.gobj.EbBook;
import com.eb.woerterbuch.gobj.WoerterbuchSession;

import java.util.ArrayList;
import java.util.List;


public class BookCtrl {
	
		private WoerterbuchSession wbSession;
		private String text;
		private BookView view;
		private EbBook book;
		private int scroll;
		private String section;
		private List<Integer> positions;
		private int chapter;
		
		
		public void store() {
			book.saveChapterText(chapter, getView().getEdit().getText());				
		}
		
		
		public String getText() {
			return text;
		}
		
		public void setText(String text) {
			this.text = text;
			readPositions(text);
		}
		
		private List<Integer> readPositions(String text2) {
			int n=0;
			positions = new ArrayList<>();
			while ( (n = text2.indexOf("<>",n)) >= 0)
			{
				positions.add(Integer.valueOf(n));
				n++;
			}
			
			return positions;
		}
		
		
		


		public BookView getView() {
			return view;
		}
		public void setView(BookView view) {
			this.view = view;
		}
		public EbBook getBook() {
			return book;
		}
		public void setBook(EbBook book) {
			this.book = book;
		}
		public int getScroll() {
			return scroll;
		}
		public void setScroll(int scroll) {
			this.scroll = scroll;
		}
		public WoerterbuchSession getWbSession() {
			return wbSession;
		}
		public void setWbSession(WoerterbuchSession wbSession) {
			this.wbSession = wbSession;
		}


		public String getSection() {
			return section;
		}


		public void setSection(String section) {
			this.section = section;
		}


		public void readFrom(IniFile iniFile) {
			
			String bookName = iniFile.getSectionValue(getSection(), "Book","");
			String wbName = iniFile.getSectionValue(getSection(),  "Language", "");
			int scroll = iniFile.getSectionValueAsInteger(getSection(), "Scroll", 0);

			setBook(new EbBook(bookName));
			
			String[] parts = wbName.split(" ");
			setWbSession(new WoerterbuchSession(parts[0], parts[1]));			
			setScroll(scroll); 			
		}
		
		public void writeTo(IniFile iniFile) {
			setScroll(getView().getPane().getVerticalScrollBar().getValue());
			iniFile.setSectionValue(getSection(), "Scroll", getScroll());				
		}


		public void setChapterText(int chapterNr) {
			if (getBook()==null)
				return;
			chapter = chapterNr;
			String aktText = getBook().getChapter(chapterNr);
			getView().getEdit().setText(aktText);
			readPositions(aktText);
			
		}


		public void resetText() {			
			getView().getEdit().setText(getText());
			
		}


		public int getNextPosition(int position) {
			for (Integer integer : positions) {
				if (integer.intValue()>position)
					return integer.intValue();
			}
			return -1;
		}
		
		public int getNextPositionIndex(int position) {
			int pos = getNextPosition(position);
			if (pos==-1)
				return -1;
			return positions.indexOf(Integer.valueOf(pos));					
		}


		public int getPosition(int newPositionIndex) {
			if (newPositionIndex<0)
				return 0;
			return positions.get(newPositionIndex);
		}
		
			
}
