package com.eb.base.inifile.implementation;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;


class IniFileImpl implements IniFile {

	static void saveSectionValues(String fileName, String sectionName, List<String> strings) {
		IniFile file = new IniFileImpl(fileName);
		file.Read();
		file.setSectionValues(sectionName, strings);
		file.Write();
	}

	static List<String> readSectionValues(String fileName, String sectionName) {
		IniFile file = new IniFileImpl(fileName);
		file.Read();
		return file.getSectionValues(sectionName);
	}
	
	IniFileImpl(String string) {
		fileName = string;
	}
	
	@Override
	public String toString() {
		return "" +fileName + " " + iniFileSections.size();
	}
	
	@Override
	public void sectionValuesDo(String sectionName, BiConsumer<String, String> consumer)
	{
		List<String> sectionValues = getSectionValues(sectionName);
		for (String string : sectionValues) {
			String[] split = string.split("=");
			consumer.accept(split[0],  split.length==1 ? "" : split[1]);
		}
		
	}
	
	@Override
	public void Write() {
		StringBuilder strb = new StringBuilder();
		strb.append("\r\n");
		for (IniFileSection iniFileSection : iniFileSections) {
			iniFileSection.WriteTo(strb);
		}
		
		FileUtil.WriteText(fileName, strb.toString());
	}

	@Override
	public void Read() {
		iniFileSections.clear();
		try {
			if (!FileUtil.existsFile(fileName))
			{
				throw new FileNotFoundException(fileName);
			}

			String encoding = FileUtil.guessEncoding(fileName);
			List<String> lines = FileUtil.readLines(encoding, fileName);

 			IniFileSection s = null;
			
			for (String l1 : lines) {
				String l = l1.trim();
				if (l1.startsWith("["))
				{
					s = new IniFileSection(l);
					iniFileSections.add(s);
				}
				else if (s != null)
					s.AddLine(l);
			}
			
	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	List<IniFileSection> iniFileSections = new ArrayList<>();
	private String fileName;
	

	 public IniFileSection getSection(String p, boolean create)
     {
         String search = p.trim().toUpperCase();
         if (!search.startsWith("["))
             return getSection("["+ p + "]", create);

         for (IniFileSection sec : iniFileSections)
         {
             if (sec.getName().toUpperCase().equals(search))
                 return sec;
         }
         if (!create)
             return null;

         IniFileSection newSec = new IniFileSection(p);

         iniFileSections.add(newSec);
         return newSec;
     }
	
	 @Override
	 public String getSectionValue(String section, String value, String defaultString)
     {
         IniFileSection sec = getSection(section, false);
         if (sec == null)
             return defaultString;
         String str = sec.getValue(value, defaultString);
         return str;
     }

	@Override
	public List<String> getSectionValues(String string) {
		
		IniFileSection section = getSection(string, false);
		if (section==null)
			return new ArrayList();
		
		return new ArrayList<String>(section.getValues());
	}

	@Override
	public List<String> getSectionValues(String string, boolean createSection) {
		return getSection(string, createSection).getValues();
	}

	@Override
	public void setSectionValues(String string, List<String> list) {
		IniFileSection section = getSection(string, true);
		section.getValues().clear();
		section.getValues().addAll(list);
	}

	@Override
	public void setSectionValue(String sectionName, String topic, String defaultValue) {
		IniFileSection section = getSection(sectionName, true);
		section.setValue(topic, defaultValue);		
	}

	@Override
	public Integer getSectionValueAsInteger(String section, String name, Integer def) {
		String sectionValue = getSectionValue(section, name, def == null ? null : def.toString());
		if (sectionValue==null)
			return null;
		return Integer.valueOf(Integer.parseInt(sectionValue));		
	}

	@Override
	public void setSectionValue(String sectionName, String topic, int x) {
		setSectionValue(sectionName, topic, "" + x);
		
	}

	@Override
	public boolean hasSection(String string) {
		IniFileSection section = getSection(string, false);
		return section!=null;
	}

	@Override
	public String getFileName() {
		return fileName;
	}
}