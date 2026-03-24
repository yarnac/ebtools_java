package com.eb.ebmusic.gobj;

import java.util.ArrayList;

public class MusicFolderLineRepresenter {

	public String getString(MusicFolder f)
	{
		return String.format("%d#%d#%s#%s", f.getId(), f.getIdParent(), f.isDirectory()?"Dir":"File", f.getPath().replaceAll("#","§§"));
	}
	
	public MusicFolder fillFromLine(MusicFolder f, String string) {
		String parts[] = string.split("#");
		f.setId(Integer.parseInt(parts[0]));
		f.setIdParent(Integer.parseInt(parts[1]));
		f.setPath(parts[3].replaceAll("§§", "#"));
		f.setType(parts[2].equals("Dir")?MusicFolder.EntryType.DIRECTORY : MusicFolder.EntryType.FILE);
		
		if (f.isDirectory())			
			f.setEntries(new ArrayList<>());
		
		return f;
	}
}
