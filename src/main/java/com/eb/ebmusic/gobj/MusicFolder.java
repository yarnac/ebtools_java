package com.eb.ebmusic.gobj;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.eb.base.io.FileUtil;

public class MusicFolder {
	private int id;
	private int idParent;	
	private List<MusicFolder> entries;	
	private String path;
	private MusicFolder parent;
	private EntryType type;
	private Object tag;

    public Object getTag() {
        return tag;
    }

    public void setTag(Object tag) {
        this.tag = tag;
    }

    enum EntryType {
		UNDEFINED,
		FILE,
		DIRECTORY
	}
	
	public void allFoldersDo(Consumer<MusicFolder> aConsumer)
	{
		aConsumer.accept(this);
		if (isDirectory())
			for (MusicFolder musicFolder : entries) {
				musicFolder.allFoldersDo(aConsumer);				
			}
	}
	
	public MusicFolder filter(Predicate<MusicFolder> filter)
	{
		MusicFolder copy = this.copy();
		return privateFilter(copy, filter);		
	}
	
	private MusicFolder privateFilter(MusicFolder copy, Predicate<MusicFolder> filter) {
		if (filter.test(this))
			return this;
		
		if (isFile())
			return null;
		
		List<MusicFolder>childsToRemove = new ArrayList<>();
		for (MusicFolder musicFolder : entries) {
			MusicFolder mf = musicFolder.privateFilter(musicFolder, filter);
			if (mf==null)
				childsToRemove.add(musicFolder);
		}
		for (MusicFolder musicFolder : childsToRemove) {
			entries.remove(musicFolder);
		}
		if (entries.size()>0)
			return this;
		
		return null;
	}

	@Override
	public String toString() {
		return FileUtil.getFileName(path);
	}
	
	private boolean isFile() {
		return type==EntryType.FILE;
	}
	
	
	

	public int initializeIds(int parentId, int actId) {
		setIdParent(parentId);
		setId(actId++);
		
		if (!isDirectory())
			return actId;
		
		for (MusicFolder entry : entries) {
			actId = entry.initializeIds(getId(), actId);
		}
		return actId;
	}
	
	public void addEntry(MusicFolder childEntry) {
		if (childEntry.getParent()!=null)
			childEntry.getParent().removeEntry(childEntry);
		childEntry.setParent(this);
		if (getEntries()==null)
			setEntries(new ArrayList<>());
		getEntries().add(childEntry);	
	}
	
	public MusicFolder copy()
	{
		MusicFolder f = new MusicFolder();				
		f.path = path;
		f.type = type;			
		if (entries!=null)
		for (MusicFolder musicFolder : entries) {
			f.addEntry(musicFolder.copy());
		}
		return f;
	}

	private void removeEntry(MusicFolder childEntry) {
		getEntries().remove(childEntry);
		
	}

	public boolean isDirectory() {
		return getType() == EntryType.DIRECTORY;
	}
	
	public void determineType() {
		File file = new File(fetchFullPath());
		if (file.isFile())
			setType(EntryType.FILE);
		else if (file.isDirectory())
			setType(EntryType.DIRECTORY);
	}	

	public String fetchFullPath() {
		if (getParent()!=null)
			return getParent().fetchFullPath()+"/"+getPath();
		return getPath();
	}
	
	public List<MusicFolder> fetchFiles() {
		return getEntries().stream().filter(x->x.isFile()).collect(Collectors.toList());
	}
	
	public List<MusicFolder> fetchDirectories() {
		return getEntries().stream().filter(x->x.isDirectory()).collect(Collectors.toList());
	}




	public List<MusicFolder> getEntries() {		
		return entries;
	}
	public void setEntries(List<MusicFolder> newEntries) {
		this.entries = newEntries;
	}
		
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public MusicFolder getParent() {
		return parent;
	}
	public void setParent(MusicFolder parent) {
		this.parent = parent;
	}
	
	public EntryType getType() {
		return type;
	}	
	public void setType(EntryType type) {
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdParent() {
		return idParent;
	}

	public void setIdParent(int idParent) {
		this.idParent = idParent;
	}
	
}
