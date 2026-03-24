package com.eb.ebookreader.tobj;

import com.eb.base.io.FileUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ConverterBook  {

	private static final String LISBOA_IN = "~/Documents/LisboaD.txt";
	private static final String LISBOA_OUT = "~/Documents/LisboaD2.txt";
	private static List<String> paragraphLines;
	
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		List<String> readLines;
		List<String> writeLines = new ArrayList<>();
		int emptyLines = 0;
		
		try {
			readLines = FileUtil.readLines("UTF8",LISBOA_IN);
			readLines = new LinkedList<>(readLines);
						
			paragraphLines = new ArrayList<>();	
			markChapters(readLines);
			linkLines(readLines);
			deleteEmptyLines(readLines);
						
			// emptyLines = oldMethod(readLines, writeLines, emptyLines);
			FileUtil.WriteLines("UTF8",LISBOA_OUT, readLines);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}



	private static void deleteEmptyLines(List<String> readLines) {
		int i=0;
		boolean emptyLines = false;
		while (i<readLines.size())
		{			
			String line = readLines.get(i).trim();
			if (line.length()==0 || Character.isDigit(line.charAt(0)))
			{
				if (emptyLines)
				{
					readLines.remove(i);
					i--;
				}
				emptyLines = true;
			}
			else
				emptyLines = false;
			
			i++;
			
		}
		
	}



	private static void linkLines(List<String> readLines) {
		int i=0;
		while (i<readLines.size())
		{
			String line = readLines.get(i++);
			if (line.isEmpty())
				continue;
			if (Character.isDigit(line.charAt(0)))
				continue;
			
			int length = line.length();
			if (!Character.isDigit(line.charAt(length-1)))
				continue;
			int index = length-1;
			while (Character.isDigit(line.charAt(index-1)))
				index--;
			line = line.substring(0, index);
			
			while(readLines.get(i).isEmpty())
				readLines.remove(i);
			
			line += readLines.get(i);
			readLines.remove(i);
			i--;
			readLines.set(i, line);
			
					
					
		}
		
	}



	private static int oldMethod(List<String> readLines, List<String> writeLines, int emptyLines) {
		for (int ln = 0; ln < readLines.size(); ln++) {
			String string = readLines.get(ln);
				
			int n = string.length()-1;
			if (n>0)
			{
				int i=n;
				while (i>=0 && Character.isDigit(string.charAt(i)))
					i--;
				
				emptyLines = 0;
				
						
				
				if (i<n)
				{
					try {
						String rest = string.substring(0,i+1).trim();
						if (i<0)
						{
							// Zahl steht am Anfang
							
						}
						else
						{
						char ch = rest.charAt(i-1);
						if (ch!='.')
						{
							ln++;
							string = readLines.get(ln);
							while (string.trim().length()==0)
							{
								ln++;
								string = readLines.get(ln);
							}
							writeLines.add(rest + " " + string);
						}
						else
							writeLines.add(rest);
						}
					}
					catch (Exception e)
					{
						writeLines.add(string);
					}
					
				}
				else 
					writeLines.add(string);
			}
			else
			{
				if (emptyLines<2)
				{
					writeLines.add(string);						
				}
				emptyLines++;
				if (emptyLines==3)
					writeLines.add("<p>");
				
				
			}
			
			
			
		}
		return emptyLines;
	}



	private static void markChapters(List<String> readLines) {
		int i=0;				
		for (i=0;i<readLines.size();i++)
		{					
			checkAndRemoveChapter(readLines, i);								
		}
	}



	private static void checkAndRemoveChapter(List<String> readLines, int i) {
		if (readLines.size()-i < 11)
			return;
			
		int actI = i;
		/*
		for(int j=0;j<5;j++)
		{
			String line = readLines.get(actI++);
			
			if (line.length()!=0)
				return;
		}
		
		if (readLines.get(actI).length()==0)
			return;
			
		if (!Character.isDigit(readLines.get(actI++).charAt(0)))
			return;
		*/
		for(int j=0;j<5;j++)
		{
			String line = readLines.get(actI++);
			if (line.length()!=0)
				return;
		}
		
		actI--;
		
		while(actI>i+2)
			readLines.remove(actI--);
		String nextStart = readLines.get(i+3);
		if (nextStart.length()>10)
			nextStart = nextStart.substring(0, 20);
		String paragraph = "<Paragraph " + nextStart + " " + (i+3) +">";
		readLines.set(i+1, paragraph);
		paragraphLines.add(paragraph);
			
		
	}

	

}
