package com.eb.ebookreader.gobj;

import com.eb.base.EbAppContext;

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
		return EbAppContext.getJavaDataFilename("Reader/" + filename);
	}


	public static String getSessionFilename(String sessionName) {
		return getReaderFilename(sessionName+"_session.ini");
	}

	public static String getReaderBookFilename(String filename) {
		return getReaderFilename("Books/" + filename);
	}

}
