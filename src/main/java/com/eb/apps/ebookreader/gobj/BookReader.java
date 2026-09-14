package com.eb.apps.ebookreader.gobj;

import com.eb.apps.ebchatclient.domain.chat.GlobaleEinstellungen;

public class BookReader {
	
	public static BookSession readBookSession(String sessionName)
	{				
		BookSession s = new BookSession(sessionName);
		return s;	
	}

	public static void store(BookSession bookSession) {
		if (bookSession!=null)
			bookSession.store();		
	}
	
	public static String getReaderFilename(String filename) {
		return GlobaleEinstellungen.getDataPfadShared("Reader/" + filename);
	}


	public static String getSessionFilename(String sessionName) {
		return getReaderFilename(sessionName+"_session.ini");
	}

	public static String getReaderBookFilename(String filename) {
		return getReaderFilename("Books/" + filename);
	}

	public static String getReaderBookLandmarkFilename(String bookName) {
		return getReaderFilename("Books/" + bookName.replace(".txt", "_LM.txt"));
	}
}
