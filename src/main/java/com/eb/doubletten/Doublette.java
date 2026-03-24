package com.eb.doubletten;

import java.util.ArrayList;
import java.util.List;

import com.eb.base.io.FileUtil;

public class Doublette {
	
	private String name;
	private List<String> files = new ArrayList<>();
	
	public void addDouble(String str)
	{
		if (!getName().contains("/Tumblr/") && str.contains("/Tumblr/"))
		{
			getFiles().add(getName());
			setName(str);			
		}
		else
			getFiles().add(str);
	}
	
	@Override
	public String toString() {
		return "" + (getFiles().size()+1) + " * " + FileUtil.getFileName(getName()) ;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getFiles() {
		return files;
	}
	public void setFiles(List<String> files) {
		this.files = files;
	}

}
