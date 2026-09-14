package com.eb.base.io;


import com.eb.base.Logger;
import com.eb.base.extensions.FileExtensions;
import com.eb.doubletten.Doublette;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.System.out;


public class FileUtil {
	
	public static String getRealFileName(String filename)
	{
		return filename.replace("~", System.getProperty("user.home") );
	}

	public static String guessEncoding(String filename) {
		return EncodingUtil.guessEncoding(filename);
	}

	public static List<String> readLines(String encoding, String fname)
			throws IOException {
		String name = getRealFileName(fname);
		FileInputStream is = new FileInputStream(name);
		InputStreamReader isr = new InputStreamReader(is,encoding);
		BufferedReader r = new BufferedReader(isr);
		List<String> lines = new ArrayList<>();
		while (r.ready())
		{			
			String theLine = r.readLine();
			int i=0;						
			lines.add(theLine.substring(i));
		}
		
		// List<String> lines = r.lines().collect(Collectors.toList());
		
		r.close();
		return lines;
	}
	
	public static String readText(String encoding, String fname)
			throws IOException {
		String name = getRealFileName(fname);
		File file = new File(fname);
		if (!file.isFile())
			return "";
		
		StringBuilder strb = new StringBuilder();
		try {
			FileInputStream is = new FileInputStream(name);
			InputStreamReader isr = new InputStreamReader(is,encoding);
			BufferedReader r = new BufferedReader(isr);
			char [] buffer = new char[1024*1024];
			
			while (r.ready())
			{			
				int n = r.read(buffer);
				strb.append(buffer,0,n);			
			}
			
			// List<String> lines = r.lines().collect(Collectors.toList());
			
			r.close();
		}
		catch (Exception e)
		{
			e.toString();
		}
		return strb.toString();
	}
	
	

	public static void WriteText(String fileName, String str) {
		WriteText("UTF8",fileName, str);
	}
	
	public static void WriteText(String encoding, String fname, String str) {
		String fileName = getRealFileName(fname);
		try {
			if (!FileExtensions.ebFileExists(fileName)) {
				new File(fileName).createNewFile();
			}
			FileOutputStream is = new FileOutputStream(fileName,true);
			OutputStreamWriter isr = new OutputStreamWriter(is,encoding);
			BufferedWriter r = new BufferedWriter(isr);
			r.write(str);
			r.close();
		}
		catch (IOException e) {
			Logger.logError(e);
		}
	}

	public static void WriteLines(String encoding, String fileName, List<String> lines) {
		StringBuilder strb = new StringBuilder();
		
		for (String str : lines) {
			if (!strb.isEmpty())
				strb.append("\r\n"); 
			strb.append(str);
		}
		WriteText(encoding, fileName, strb.toString());
	}

	
	
	public static void open(String fname) {
		String address = getRealFileName(fname);
		try {
			Desktop.getDesktop().open(new File(address)); 			
		}
		catch (IOException e) {
			// TODO eb 5.5 2017 eventuell cleanup
			out.println(e.getLocalizedMessage());
		}		
	}
	
	public static void start(String address) {
		try {
			String name = getRealFileName(address);
			Runtime.getRuntime().exec(name); 			
		}
		catch (IOException e) {
			// TODO eb 5.5 2017 eventuell cleanup
			out.println(e.getLocalizedMessage());
		}		
	}

	public static String encodeForUrl(String wort) {
        return URLEncoder.encode(wort, StandardCharsets.UTF_8);
    }
	
	public static void showWebseite(String address) {
		try {
			Desktop.getDesktop().browse(new URI(address.replace(" ", "+"))); 			
		}
		catch (IOException e) {
			// TODO eb 5.5 2017 eventuell cleanup
			out.println(e.getLocalizedMessage());
		} 
		catch (URISyntaxException e) {
			
			out.println(e.getLocalizedMessage());
		}
	}
	
	
	
	public static List<String> getFileNames(String dName)
	{
		String dirName = getRealFileName(dName);
		List<String> res = new ArrayList<>();
		File directory = new File(dirName);
		if (!directory.isDirectory())
			return res;

		File[] files = directory.listFiles();
		if (files != null) {
			for (File file : files) {
				res.add(file.getAbsolutePath());
			}
		}
		return res; 
	}

	public static List<String> getFileNamesAll(String dName)
	{
		String dirName = getRealFileName(dName);
		List<String> res = new ArrayList<>();
		File directory = new File(dirName);
		if (!directory.isDirectory())
			return res;
		File[] files = directory.listFiles();
		if (files != null) {
		for (File file : files) {
			if (file.isDirectory()) {
				List<String> temp = getFileNamesAll(file.getAbsolutePath());
				res.addAll(temp);
			} else
				res.add(file.getAbsolutePath());
		}
		}
		return res;
	}

	
	public static boolean move(String srcFile, String targetDir) {
						
		String sourceFile = getRealFileName(srcFile);
		String targetDirectory = getRealFileName(targetDir);
		Path source = Paths.get(sourceFile);		
		Path target = Paths.get(FileUtil.getFileName(getFileName(sourceFile), targetDirectory));
		
		try {
			Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
			return true;
		} catch (IOException e) {
			Logger.logError(e.getMessage());
		}
		return false;		
	}
	
	public static boolean copy(String sourceFile, String targetDirectory) {
		
		
		
		Path source = Paths.get(sourceFile);		
		Path target = Paths.get(FileUtil.getFileName(getFileName(sourceFile), targetDirectory));
		//target = Paths.get(targetDirectory);
		try {
			
			Files.copy(source, target);
			return true;
		} catch (IOException e) {
			Logger.logError(e.getMessage());
		}
		return false;		
	}
	
	

	public static boolean existsFile(String target) {
		File file = new File(getRealFileName(target));
		return file.exists();
	}
	
	public static String getFileName(String fileName) {
		File file = new File(getRealFileName(fileName));
		return file.getName();
	}
	
	public static String getFileName(String fileName, String directoryName)
	{
		if (!directoryName.endsWith("/"))
			return getFileName(fileName,directoryName+"/");
		
		
		File file = new File(getRealFileName(directoryName) + getRealFileName(fileName));
		return file.getAbsolutePath();
	}
	
	public static String[] getFiles(String dirName)
	{
		
		File file = new File(getRealFileName(dirName));
		File[] listFiles = file.listFiles();
		if (listFiles == null)
			return new String[0];
		return Arrays.stream(listFiles)
			.filter(File::isFile)
			.map(File::getAbsolutePath)
			.toArray(String[]::new);
	}
	

	public static List<String> getFiles(String dirName, Predicate<String> filters)
	{
		
		File file = new File(getRealFileName(dirName));
		
		FilenameFilter filter = (arg0, arg1) -> filters.test(arg1);
		
		File[] listFiles = file.listFiles(filter);
		if (listFiles == null)
			return new ArrayList<>();
		return Arrays.stream(listFiles).map(File::getAbsolutePath).collect(Collectors.toList());
	}

	public static HashSet<String> getVideoExtensions() {
		return new HashSet<>(Arrays.asList(".mp4",".flv",".mov"));
	}
	public static HashSet<String> getImageExtensions() {
		return new HashSet<>(Arrays.asList(".jpg",".png",".gif"));
	}

	public static HashSet<String> videoExtensions = getVideoExtensions();
	public static HashSet<String> imageExtensions = getImageExtensions();

	public static boolean isVideo(String fileName)
	{
		String lowerCase = getExtension(fileName).toLowerCase();
		return videoExtensions.contains(lowerCase);
	}

	public static boolean isImage(String fileName)
	{
		String lowerCase = getExtension(fileName).toLowerCase();
		return imageExtensions.contains(lowerCase);
	}
	
	private static String getExtension(String fileName) {
		int n = fileName.lastIndexOf('.');
		return n >= 0 ? fileName.substring(n) : fileName;
	}

	public static List<String> getFilesWithExtension(String downloads, String string) {
		return getFiles(downloads, x->x.toLowerCase().endsWith(string.toLowerCase()));
		
	}

	public static String getFileNameWithoutExtension(String fileName) {
		
		int n = fileName.lastIndexOf('.');
		return n > 0 ? fileName.substring(0,n) : fileName;
	}

	public static String getLocalFileName(String fileName) {
		int n=fileName.lastIndexOf('/');
		return n > 0 ? fileName.substring(n+1) : fileName;
	}
	
	public static String getParentdirectory(String fileName) {
		int n=fileName.lastIndexOf('/');
		return n > 0 ? fileName.substring(0,n) : "";
	}

	public static void createDirectory(String string) {
		
		Path pathToDir = Paths.get(getRealFileName(string));
		try {
			Files.createDirectories(pathToDir);
		} catch (IOException e) {
			Logger.logError(e.getMessage());
		}	
	}

	public static void moveFiles(String src, String targetDirectory, Predicate<String> filter) {
		List<String> files = getFiles(src, filter);
		
		for (String string : files) {
			move(string,  targetDirectory);
		}
	}

	public static void moveFilesWithExtensionsTo(String sourcedir, String newTDir, String... dmg) {
		createDirectory(newTDir);
		for (String string : dmg) {
			moveFiles(sourcedir, newTDir, x -> getFileName(x).endsWith("." + string));	
		}
	}

	public static List<Doublette> getDoubles(String dirname, long compSize, boolean recursiv, int minDoubles) {
		HashMap<Long, List<String>> map;
		map = getFileLengthFilesMap(dirname, recursiv);
		
		List<Doublette> res = new ArrayList<>();		
		map.forEach((x,y)->{
			try {
				handleDoublesIn(x, y, res, compSize, minDoubles);
			} catch (IOException e) {
				Logger.logError(e.getMessage());
			}
		});
		
		map.toString();
		
		return res;				
	}

	private static void handleDoublesIn(Long x, List<String> y, List<Doublette> res, long compSize, int minDoubles) throws IOException {
		while(!y.isEmpty())
		{
			String originalName = y.get(0);
			y.remove(originalName);
			
			int relevantLength = x.intValue();
			if (relevantLength>compSize)
				relevantLength = (int) compSize;
			byte[] originalBytes = null;
			FileInputStream is ;
			try {
				is = new FileInputStream(originalName);
				originalBytes = new byte[relevantLength];
				is.read(originalBytes);
				is.close();
			}
			catch(Exception e)
			{
				Logger.logError(e.getMessage());
			}
			Doublette current = new Doublette();
			current.setName(originalName);
			res.add(current);
			
			List<String> deleted = new ArrayList<>();
			
			for (String fn : y) {
				is = new FileInputStream(fn);
				byte[] copyBytes = new byte[relevantLength];				
				is.read(copyBytes);
				is.close();
				
				
				if ( Arrays.equals(originalBytes, copyBytes))
				{
					deleted.add(fn);					
					current.addDouble(fn);
				}												
			}
			
			deleted.forEach(y::remove);
		}
	}

	private static HashMap<Long, List<String>> getFileLengthFilesMap(String dirname, boolean b) {
		HashMap<Long, List<String>> map;
		map = new HashMap<>();
		addFilesToMap(dirname, map, b);
		return map;
	}

	private static void addFilesToMap(String dirname, HashMap<Long, List<String>> map, boolean b) {
		List<String> fileNames = getFileNames(dirname);
		for (String fileName : fileNames) {
			File file = new File(fileName);
			if (!file.isFile() && b)
			{
				addFilesToMap(fileName, map, true);
				continue;
			}
				
			Long length = file.length();
			List<String> fileNamesWithSameSize = map.computeIfAbsent(length, x->new ArrayList<>());
			fileNamesWithSameSize.add(fileName);
		}
	}


	public static List<String> getEntries(String fullPath) {
		return getEntries(fullPath, x->true);
	}

	public static List<String> getEntries(String fullPath, Predicate<File> filter) {
		File file = new File(getRealFileName(fullPath));
		List<String> res = new  ArrayList<>();
		if (!file.isDirectory())
			return res;
		File[] files = file.listFiles();
		if (files == null)
			return res;
		for (File fileEntry : files) {
			if (filter.test(fileEntry))
				res.add(fileEntry.getAbsolutePath());
		}
		return res;
	}


	public static List<String> getDirectories(String string) {

		return getEntries(string, File::isDirectory);
	}

	public static void appendLine(String string, String message) {
		
		try {
			FileWriter writer = new FileWriter(getRealFileName(string), true);
			writer.write(message+"\n");
			writer.close();
		} catch (IOException e) {
			Logger.logError(e.getMessage());
		}
		
		
	}

	


}
