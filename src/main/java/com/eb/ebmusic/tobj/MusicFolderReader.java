package com.eb.ebmusic.tobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.FileUtil;
import com.eb.ebmusic.gobj.MusicFolder;
import com.eb.ebmusic.gobj.MusicFolderLineRepresenter;


public class MusicFolderReader {
	private static MusicFolderLineRepresenter getRepresenter() {
		return new MusicFolderLineRepresenter();
	}
	
	public static MusicFolder readFromDatendatei(String filename)
	{
		List<MusicFolder> folders = readFoldersFromDatendatei(filename);			
		buildFolderTree(folders);		
		return folders.get(0);		
	}

	private static void buildFolderTree(List<MusicFolder> folders) {
		Map<Integer, MusicFolder> map = new HashMap<>();
		
		for (MusicFolder folder : folders) {
			map.put(folder.getId(), folder);
			if (map.containsKey(folder.getIdParent()))
				map.get(folder.getIdParent()).addEntry(folder);
		}
	}

	private static List<MusicFolder> readFoldersFromDatendatei(String filename) {
		MusicFolderLineRepresenter representer = getRepresenter();		
		IniFile file = IniFileProvider.createIniFile(filename);
		file.Read();
		List<String> sectionValues = file.getSectionValues("Bibliothek");
		List<MusicFolder> folders 
			= sectionValues
				.stream()
				.map(f->representer.fillFromLine(new MusicFolder(), f))
				.collect(Collectors.toList());
		
		return folders;
	}
	
	public static void writeToDatendatei(String filename, MusicFolder folder)
	{
		MusicFolderLineRepresenter representer = getRepresenter();
		
		List<String> resultList = new ArrayList<>();		
		folder.allFoldersDo(f -> resultList.add(representer.getString(f)));
		IniFile file = IniFileProvider.createIniFile(filename);
		file.Read();
		file.setSectionValues("Bibliothek", resultList);	
		file.Write();
	}
	
	public static MusicFolder readFromLibraryFolder(String fullPath) {
		MusicFolder folder = new MusicFolder();
		folder.setPath(fullPath);		
		read(folder);
		folder.initializeIds(0, 1);
		return folder;
	}

	private static void read(MusicFolder folder) {
		folder.determineType();
		folder.setEntries(new ArrayList<>());
		
		for (String fileSystemEntry : FileUtil.getEntries(folder.fetchFullPath())) {
			MusicFolder childEntry = new MusicFolder();
			childEntry.setPath(FileUtil.getFileName(fileSystemEntry));
			folder.addEntry(childEntry);			
			read(childEntry);
			
		}					
	}
}
