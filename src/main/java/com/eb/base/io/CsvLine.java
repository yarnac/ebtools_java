package com.eb.base.io;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CsvLine {
	private String line;
	private Map<String, String> values;
	private CsvFile file;
	
	public CsvLine(CsvLine line2) {
		line = line2.line;
		values = new HashMap<String, String>(line2.getValues());
		file = line2.file;
	}
	public CsvLine() {
		// TODO Auto-generated constructor stub
	}
	
	 
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getLine();
	}
	
	public String getLine() {
		return line;
	}
	public void setLine(String line) {
		this.line = line;
	}
	
	public Map<String, String> getValues() {
		return values;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	public CsvFile getFile() {
		return file;
	}
	public void setFile(CsvFile file) {
		this.file = file;
	}
	
	public Date getDate(String fieldName)
	{
		String value = getValues().get(fieldName);
		String[] split = value.split("\\.");
		Calendar c = Calendar.getInstance();
		c.set(Integer.parseInt(split[2]), Integer.parseInt(split[1]), Integer.parseInt(split[0]),0,0);
		return c.getTime(); 	
	}
	public String getValue(String string) {
		String val = getValues().get(string);
		if (val==null)
			return "";
		return val.trim();		
	}
	public String getFilteredLine() {
		StringBuilder strb = new StringBuilder();
		int i=0;
		for (String key : getFile().getHeader()) {
			if (i++>0)
				strb.append(getFile().getSeparator());
			strb.append(getValue(key));			
		}
		return strb.toString();
	}
}
