package com.eb.ebtools.tobj;

import com.eb.base.EbAppContext;
import com.eb.base.MainGlobals;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.CsvFile;
import com.eb.base.io.FileUtil;

import java.awt.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class EbToolsManager {
	
	String iniFileName;
	
	private IniFile iniFile;
	
	public EbToolsManager()
	{
		iniFileName = EbAppContext.getJavaDataFilename("EbToolsIniFile.txt");
		FileUtil.createDirectory(FileUtil.getParentdirectory(iniFileName));
		iniFile = IniFileProvider.createIniFile(iniFileName);
		iniFile.Write();
	}

	public IniFile getIniFile() {
		return iniFile;
	}

	public void setIniFile(IniFile iniFile) {
		this.iniFile = iniFile;
	}

	public void iniFileBearbeiten() {
		FileUtil.open(iniFileName);
	}

	public void openJava() {
		FileUtil.start("open /Applications/Eclipse2.app");
	}

	public void mergeSantanderFiles() {
		List<String> fileNames = FileUtil.getFileNames("~/Documents/Finanzen/Santander/CSV");		
		CsvFile mergedFile = new CsvFile("~/Documents/Finanzen/Santander/Merged.csv", ";");
		for (String fileName : fileNames) {
			if (fileName.toLowerCase().endsWith(".csv"))
			{
				CsvFile file = new CsvFile(fileName,";");		
				file.Read();
				mergedFile.merge(file);	
			}					
		}
		
		mergedFile.Write("Windows-1252","~/Documents/Finanzen/Santander/Merged_Rest.csv", new String[]{"-ORTMANNS","-Weicker","-HOLST"});
		mergedFile.Write("Windows-1252","~/Documents/Finanzen/Santander/Merged_Stefan.csv", new String[]{"+Stefan"});
		mergedFile.Write("Windows-1252","~/Documents/Finanzen/Santander/Merged_Bolten.csv", new String[]{"+Bolten"});
		mergedFile.Write("Windows-1252","~/Documents/Finanzen/Santander/Merged_Weicker.csv", new String[]{"+Weicker"});
		
		mergedFile.SortByDate("Buchungsdatum");
		mergedFile.Write();
		mergedFile.JoinColumns("Verwendungszweck 1","Verwendungszweck 2","Verwendungszweck x");
		mergedFile.JoinColumns("Verwendungszweck 7","Verwendungszweck 8","Verwendungszweck");		
		mergedFile.UseColumns("Buchungsdatum","Verwendungszweck", "Betrag (EUR)");	
		
			
		mergedFile.Write("Windows-1252","~/Documents/Finanzen/Santander/Merged_Ortmanns.csv", new String[]{"+ORTMANNS","+HOLST"});
		
		
		
		
		
		
		
		fileNames.toString();
	}

	Timer timer;
	int icall=0;
	public void holdSoundAwake() {
		if (timer==null)
		{
			int period = 190000;
			System.out.println("" + period);
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					beep();					
				}
			},1000, period);
			
			
			
		}
		else 
		{
			timer.cancel();
			timer=null;
		}
	}
	
	public void beep()
	{
		Toolkit.getDefaultToolkit().beep();
	}
}
