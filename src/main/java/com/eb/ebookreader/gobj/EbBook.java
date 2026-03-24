package com.eb.ebookreader.gobj;

import java.io.IOException;
import java.util.ArrayList;

import com.eb.base.io.FileUtil;
import com.eb.woerterbuch.gobj.WoerterbuchSession;

public class EbBook {
	
	private String text;
	private ArrayList<Chapter> chapters;
	private String fileName;
	private WoerterbuchSession woerterbuchSession;
	
	public class Chapter
	{
		private String title;
		private int start;
		private int ende;
		private String fullText;
		
		@Override
		public String toString() {
			return getTitle();
		}
		
		public String getText()
		{
			int n = fullText.length();
			String out = fullText.substring(start, ende);
			if (out.startsWith("<"))
				out = out.substring(out.indexOf(">")+2);
			return out;
		}
		
		public Chapter(String string, String theText, int theStart, int theEnde) {
			fullText = theText;
			start = theStart;
			ende = theEnde;
			title = string;
		}
		public int getStart() {
			return start;
		}
		public void setStart(int start) {
			this.start = start;
		}
		public int getEnde() {
			return ende;
		}
		public void setEnde(int ende) {
			this.ende = ende;
		}
		public String getTitle() {
			return title;
		}
		public void setTitle(String title) {
			this.title = title;
		}

		public void addOffset(int lenDiff) {
			start += lenDiff;
			ende += lenDiff;			
		}
	}
	
	public String getChapter(int index)
	{
		if (index<0)
			return getChapter(0);
		if (index>=chapters.size())
			return "";
		return chapters.get(index).getText();
	}
	
	
	public EbBook(String filename)
	{
		
		setFileName( BookReader.getReaderBookFilename(filename));
		try {
			text = FileUtil.readText("UTF8",getFileName());
						
			chapters = new ArrayList<Chapter>();
			Chapter oldChapter = new Chapter("Anfang", text,  0,0);
			chapters.add(oldChapter);
			
			int index=0;
			while ((index = text.indexOf("<Paragraph", index + 1)) >= 0)
			{
				String title = "Paragraph";				
				Chapter newChapter = new Chapter(title, text, index,0);
				chapters.add(newChapter);
				oldChapter.setEnde(index);
				oldChapter = newChapter;
			}
			oldChapter.setEnde(text.length());
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void saveChapterText(int chapterNr, String text2) {
		Chapter chapter = chapters.get(chapterNr);
		String newChapterText = "<Paragraph>\r" + text2;		
		String newText = text.substring(0, chapter.start) + newChapterText + text.substring(chapter.getEnde());
		
		if (text.equals(newText))
			return;
		
		int lenDiff = newText.length() - text.length();		
		chapter.fullText = newText;
		this.text = newText;
		chapters.get(chapterNr).setEnde(chapters.get(chapterNr).getEnde() + lenDiff);
		
		for (int i=chapterNr+1; i<chapters.size(); i++)
		{
			Chapter aktChapter = chapters.get(i);
			aktChapter.addOffset(lenDiff);
			aktChapter.fullText = newText;
		}
		FileUtil.WriteText(getFileName(), newText);
		
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	
	
	
}
