package com.eb.ebtools.tobj;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;

import java.io.File;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class DownloadsManager {
	private static final String CLEAN_UP_DOWNLOADS = "CleanUpDownloads";
	private static final String CLEAN_UP_DOWNLOAD_EXTENSIONS = "CleanUpDownloadsExtensions";
	private static final String CLEAN_UP = "CleanUp";
	
	String downloads = "~/Downloads";
	private IniFile iniFile;
	
	
	
	public DownloadsManager(IniFile _iniFile) {
		iniFile = _iniFile;
	}

	public void cleanUpDownloads() {
				
		handleZipFiles();
		
		BiConsumer<String , String> consumer = (name, wert) -> cleanUpSection(name, wert);
		
		iniFile.sectionValuesDo(CLEAN_UP, consumer);
			
	}
	
	private void cleanUpSection(String name, String dirName) {
		iniFile.Read();


		List<String> values = iniFile.getSectionValues(CLEAN_UP + " " + name, false);
		for (String string : values) {
			String[] split = string.split("=");
			cleanUpSectionEntry(dirName, split[0], split[1]);
		}
		//iniFile.sectionValuesDo(CLEAN_UP + " " + name, (entryName,wert)->cleanUpSectionEntry(dirName, entryName, wert));
		
	}

	private void cleanUpSectionEntry(String sourcedir, String entryName, String wert) {
		String[] split = wert.split("#");
		String targetDir = split[0];
		String[] patterns = split[1].split(",");
		
		if (!new File(targetDir).isDirectory())
			targetDir = downloads + "/" + targetDir;
		if (!new File(targetDir).isDirectory())
			return;
		for (String pattern : patterns) {
			Tester t = new Tester(pattern);
			FileUtil.moveFiles(sourcedir, targetDir, t);
		}
	}
	
	public class Tester implements Predicate<String> {
		String pattern;
		Tester(String p)
		{
			pattern = p;
		}
		
		public boolean test(String x)
		{
			if (pattern.startsWith("*."))
				return x.endsWith(pattern.substring(1));
			if (pattern.endsWith("*"))
				return x.startsWith(pattern.substring(0, pattern.length()-1));
			
			String[] split = pattern.replace("*",":").split(":");
			if (!x.startsWith(split[0]))
				return false;
			
			if (!x.endsWith(split[1]))
				return false;
			
			return true;		
		}
	}

	public boolean isAllowedFile(String x, String pattern) {
		if (pattern.startsWith("*."))
			return x.endsWith(pattern.substring(1));
		if (pattern.endsWith("*"))
			return x.startsWith(pattern.substring(0, pattern.length()-1));
		return false;
	}

	public void handleZipFiles() {
		List<String> filesWithExtension = FileUtil.getFilesWithExtension(downloads,".zip");
		for (String zipFileName : filesWithExtension) {
			handleZipFile(downloads, zipFileName);
		}
	}
	
	private void moveExtensionFiles(String dmg) {
		moveFilesWithExtensions(dmg, dmg);
	}

	private void moveFilesWithExtensions(String targetDirectory, String ... dmg) {
		
		String newTDir = downloads + "/ext_" + targetDirectory;
		FileUtil.moveFilesWithExtensionsTo(downloads, newTDir, dmg);
		
	}

	private void handleZipFile(String downloads, String zipFileName) {
		String fileRoot = FileUtil.getFileNameWithoutExtension(zipFileName);
		File file = new File(fileRoot);
		if (file.isDirectory())
		{
			String newParentDir = downloads + "/archives/" + FileUtil.getLocalFileName(file.getAbsolutePath());
			
			FileUtil.createDirectory(newParentDir );
			FileUtil.move(fileRoot,newParentDir);
			FileUtil.move(zipFileName,newParentDir);
		}
	}
}
