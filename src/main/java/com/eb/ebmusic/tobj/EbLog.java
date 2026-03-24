package com.eb.ebmusic.tobj;

import com.eb.base.EbAppContext;
import com.eb.base.io.FileUtil;

public class EbLog {
	
	public static void log (String message)
	{
		try {
			FileUtil.appendLine(EbAppContext.getJavaDataFilename("musiclog.txt"), message);
		}
		catch (Exception e)
		{
			e.toString();
		}
	}
	

}
