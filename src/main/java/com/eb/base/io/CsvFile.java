package com.eb.base.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CsvFile {
	private List<CsvLine> lines;
	private List<String> header;
	private String separator;
	private String fileName;
	private String headerString;
	
	public CsvFile(String fileName, String separator)
	{
		this.fileName = fileName;
		this.setSeparator(separator);		
	}
	
	
	
	public void Read()
	{
		try {
			List<String> linesInFile = FileUtil.readLines("Windows-1252", fileName);
			if (!containsUmlaute(linesInFile))
				linesInFile = FileUtil.readLines("UTF-8", fileName);
			if (!containsUmlaute(linesInFile))
				linesInFile = FileUtil.readLines("UTF-8", fileName);
			readLines(linesInFile);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private boolean containsUmlaute(List<String> linesInFile) {
		for (String string : linesInFile) {
			if (string.contains("Ü"))
				return true;
			if (string.contains("ü"))
				return true;
		} 
		return false;
	}



	private void readLines(List<String> linesInFile) {
		int i=0;
		lines = new ArrayList<>();
		for (String line : linesInFile) {
			if (i++==0)
				readHeader(line);
			else
				lines.add(readLine(line));
		}
		
	}


	private CsvLine readLine(String newline) {
		String line = newline.replaceAll(" EUR","").replaceAll("\\+","");
		String[] split = line.split(getSeparator());
		CsvLine csvLine = new CsvLine();
		csvLine.setLine(line);
		csvLine.setFile(this);
		HashMap<String, String> valueDict = new HashMap<String, String>();
		int i=0;
		for (String string : split) {
			valueDict.put(getHeader().get(i), string);
			i++;
		}
		csvLine.setValues(valueDict);
		return csvLine;
	}


	private void readHeader(String line) {
		String[] split = line.split(getSeparator());
		header = Arrays.asList(split);		
		headerString = line;
	}


	public List<CsvLine> getLines() {
		return lines;
	}
	public void setLines(List<CsvLine> lines) {
		this.lines = lines;
	}
	public List<String> getHeader() {
		return header;
	}
	public void setHeader(List<String> header) {
		this.header = header;
	}


	public void merge(CsvFile file) {
		Set<String> loadedLines = new HashSet<>();
		if (getHeader()==null)
		{
			setHeader(file.getHeader());
			headerString = file.headerString;
			setLines(new ArrayList<>());
		}
		if (getHeader().equals(file.getHeader()))
		{
			for (CsvLine line : file.getLines()) {
				boolean found = false;
				for (CsvLine oLine : getLines()) {
					
					if (compare(line,oLine))
					{
						found = true;
						break;
					}
									
				}
				
				if (found)
					continue;
				loadedLines.add(line.getLine());
				CsvLine csvLine = new CsvLine(line);
				csvLine.setFile(this);				
				getLines().add(csvLine);				
			}						
		}
	}


	private boolean compare(CsvLine line, CsvLine oLine) {
		if (!compare(line, oLine,"Buchungsdatum"))
			return false;
		if (!compare(line, oLine,"Betrag (EUR)"))
			return false;
		if (!compare(line, oLine,"Saldo (EUR)"))
			return false;
		
		return true;
		
	}

	private boolean compare(CsvLine line, CsvLine oLine, String string) {
		return line.getValue(string).equals(oLine.getValue(string));
	}

	public void SortByDate(String string) {
		getLines().sort((x,y)->compareLines(string, x,y));		
	}

	private int compareLines(String fieldName, CsvLine x, CsvLine y) {
		Date dateX = x.getDate(fieldName);
		Date dateY = y.getDate(fieldName);
		return dateX.compareTo(dateY);
	}

	public void Write(String encoding, String fileName, String[] args) {		
		List<CsvLine> result = filterLines(args);							
		Write(encoding, fileName, result);			
	}
	
	public void Write()
	{
		Write("Windows-1252", fileName, getLines());
	}
	
	public void Write(String encoding, String fileName, List<CsvLine> linesToWrite)
	{
		List<String> result = new ArrayList<>();
		result.add(getHeaderString());
		for (CsvLine line : linesToWrite) {
			result.add(line.getFilteredLine());
		}
		FileUtil.WriteLines(encoding, fileName, result);
	}
	

	private String getHeaderString() {
		int i=0;
		StringBuilder strb = new StringBuilder();
		
		for (String string : header) {
			if (i++>0)
				strb.append(getSeparator());
			
			strb.append(string);
		}
		
		return strb.toString();
	}



	private List<CsvLine> filterLines(String[] args) {
		List<CsvLine> result = new ArrayList<>();

		List<String> positive = new ArrayList<>();
		List<String> negative = new ArrayList<>();
		for (String string : args) {
			if (string.startsWith("-"))
				negative.add(string.substring(1).toLowerCase());
			else if (string.startsWith("+"))
				positive.add(string.substring(1).toLowerCase());
			else				
				positive.add(string.toLowerCase());			
		}
		
		for (CsvLine line : getLines()) {
			boolean pos=false;
			boolean neg=false;
			String str = line.getValue("Verwendungszweck 1") + line.getValue("Verwendungszweck 2") + line.getValue("Verwendungszweck 3") + line.getValue("Verwendungszweck 4");
			str = str.toLowerCase();
			
			if (positive.size()==0)
				pos = true;
			else
			{
				for (String string : positive) {
					if (str.contains(string))
					{
						pos = true;
						break;
					}
				}									
			}
			if (!pos)
				continue;
			
			if (negative.size()==0)
				neg = false;
			else
			{
				for (String string : negative) {
					if (str.contains(string))
					{
						neg = true;
						break;
					}
				}									
			}
			if (neg)
				continue;
						
			result.add(line);
		}
		return result;		
	}



	public void RemoveColumn(String string) {
		List<String> collect = getHeader()
			.stream()
			.filter(x->!x.equals(string))
			.collect(Collectors.toList());
		setHeader(collect);				
	}



	public String getSeparator() {
		return separator;
	}



	public void setSeparator(String separator) {
		this.separator = separator;
	}



	public void JoinColumns(String string, String string2, String result) {
		for (CsvLine line : getLines()) {
			Map<String, String> d = line.getValues();
			d.put(result, (line.getValue(string).trim() + " " + line.getValue(string2)).trim());			
		}		
		RemoveColumn(string2);
		if (!result.equals(string2))
		{
			int index = header.indexOf(string);
			header.set(index, result);
		}
	}



	public void UseColumns(String... args) {
		header = Arrays.asList(args);				
	}
		

}
