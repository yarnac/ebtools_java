package com.eb.ebbodytool;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

import com.eb.base.io.FileUtil;

public class BodyTool {

	

	private static final String RESULT_CSV_FILE = "d:/Data/Tanita/5/Tanita/Graphv1/Data/Result1.csv";
	private static final String TANITA_CARD_FILES = "DATA1.CSV";
	private static final String LOCAL_DATA_DIR = "d:/Data/Tanita/5/Tanita/Graphv1/Data/";
	private static final String TANITA_CARD_DIR = "z:/PortableSSD/Data/Tanita/5/GRAPHV1/DATA";

	
	public static void main(String[] args) {
		try {
			String sourceDir = TANITA_CARD_DIR;
			String targetDir = LOCAL_DATA_DIR;
			String files = TANITA_CARD_FILES;
			String resultFileName = RESULT_CSV_FILE;
			/*
			for (String file : files.split(","))
			{
				String sourceFile = sourceDir + file;
				String targetFile = targetDir + file;
				
				if (new File(sourceFile).exists())
				{
					File target = new File(targetFile);
					if (target.exists())
						Files.delete( target.toPath());
					FileUtil.copy(sourceFile, targetDir);
				}
			}
			*/
			
			List<LinkedHashMap<String, String>> messungen = new ArrayList<>();
			messungen.addAll(readMessungen(args[0]));
			
		
			StringBuilder strb = new StringBuilder();
			Map<String, String> bezMap = getBezeichnungen();
			bezMap.forEach((x,y)->strb.append(bezMap.get(x) + '\t'));
			strb.append("\r\n");

			boolean filterMorgens = args.length > 2 && args[2].equals("morgens");
			boolean filterAbends  = args.length > 2 && args[2].equals("abends");
			boolean filter = filterMorgens || filterAbends;
			
			for (LinkedHashMap<String, String> map : messungen) {
				if (filter) {

					String datum = map.get("DT");
					if (datum.substring(6).compareTo("2026")<0)
						continue;


					Time time = Time.valueOf(map.get("Ti"));

					boolean isMorgens = timeIsBetween(time, "04:00:00","12:00:00");
					if (filterMorgens && !isMorgens)
						continue;

					if (filterAbends && timeIsBetween(time, "04:00:00","20:00:00"))
						continue;

				}

 				if (map.size() < bezMap.size())
					continue;


				map.put("DT", map.get("DT").replace(".2026",""));
				bezMap.forEach((x,y)->strb.append(map.get(x) + '\t'));
				strb.append("\r\n");
			}
			String output = strb.toString();
			System.out.println(output);			
			FileUtil.WriteText(args[1], output);
			// FileUtil.open(resultFileName);
			
			messungen.toString();
		} catch (IOException e) {	
			e.printStackTrace();
		}
	}

	private static boolean timeIsBetween(Time time, String timeStringAb, String timeStringBis) {
		Time ab =  Time.valueOf(timeStringAb);
		Time bis = Time.valueOf(timeStringBis);

		return !time.before(ab)
				&& time.before(bis);
	}


	private static List<LinkedHashMap<String, String>> readMessungen(String fileName)
			throws FileNotFoundException, UnsupportedEncodingException, IOException {
		List<String> readLines = FileUtil.readLines("UTF8", fileName);
		List<LinkedHashMap<String, String>> messungen = new ArrayList<>();
				
		for (String string : readLines) {
			String[] split = string.split(",");
			LinkedHashMap<String, String> map = new LinkedHashMap<String,String>();
			int i=12;
			while(i<split.length)
			{
				String str = split[i++];
				String value = split[i++];
				if (str.equals("DT"))
				{
					String date = value.replace('/', '.').replaceAll("\"","");
					String newValue = date + " " + getDayOfWeek(date) ;
					map.put(str, newValue);
				}
				else
					map.put(str, value.replaceAll("\\.", ","));


			}
			map.replace("Ti",map.get("Ti").replaceAll("\"", ""));
			
			messungen.add(map);
			map.toString();
						
		}
		return messungen;
	}

	private static String getDayOfWeek(String dateStr) {
		DateTimeFormatter formatter =
				DateTimeFormatter.ofPattern("dd.MM.yyyy");

		String weekday = LocalDate.parse(dateStr, formatter)
				.getDayOfWeek()
				.getDisplayName(TextStyle.SHORT, Locale.GERMAN);

		return weekday;

	}
	
	public static Map<String, String> getBezeichnungen()
	{
		Map<String, String> map = new LinkedHashMap<>();		
		map.put("DT","Datum");
		map.put("Ti","Zeit");
		map.put("Wk","Gewicht");
		map.put("MI","BMI");
		map.put("FW","% Fett");
		map.put("mW","kg M");
		
		map.put("bW","kg Knochen");
		map.put("IF","Viskulaeres Fett");
		map.put("rD","kcal Grundumsatz");
		map.put("rA","Stoffwechselalter");
		map.put("ww","% Wasser");
		
		
		map.put("Fr","% Fett r. A");
		map.put("Fl","% Fett l. A");
		map.put("FR","% Fett r. B");
		map.put("FL","% Fett l. B");
		map.put("FT","% Fett Rumpf");
		
		map.put("mr","kg M r.A.");
		map.put("ml","kg M l.A.");
		map.put("mR","kg M r.B");
		map.put("mL","kg M l.B");
		map.put("mT","kg Muskeln Rumpf");
		
		map.put("Bt","?");
		map.put("GE","Geschlecht");
		map.put("AG","Alter");
		map.put("Hm","Groesse");
		map.put("AL","Aktivitaetslevel");
		map.put("CS","Checksumme?");
			
		return map ;
	}
	

	
}
