package com.eb.base;

import java.io.File;

public class EbAppContext {
	
	private static EbAppContext current;
	
	private String javaRootDirectory;
	
	public EbAppContext()
	{		
		File file;
		String userDir = System.getProperty("user.home");
		setEbToolsHome(userDir+"/EbTools");

	}

	public static String getEbTempDatenDir()
	{
		String appData = System.getProperty("java.io.tmpdir");
		return appData;
	}

	public static String getEbTempDatenDir(String localName)
	{
		return getEbTempDatenDir() + localName;
	}

	public static String getEbDownloadsDatenDir(String localName)
	{
		String appData = System.getProperty("user.home") + "/Downloads/" + localName;
		return appData;
	}
	
	public static String getEbToolsDatenDir()
	{
		String appData = System.getProperty("user.home") + "/EbToolsDaten";
		return appData;
	}

	public static String getEbToolsDatenDir(String localName)
	{
		return getEbToolsDatenDir()+"/"+localName;
	}
	
	
	public static EbAppContext getCurrent() {
		if (current==null)
			current = new EbAppContext();
		return current;
	}

    public static boolean isOSX() {
		return System.getProperty("user.home").startsWith("/Users");
    }

	public static boolean isWindows() {
		return !System.getProperty("user.home").startsWith("/Users");
	}

    public String getJavaRootDirectory() {
		return javaRootDirectory;
	}

	public void setEbToolsHome(String javaRootDirectory) {
		this.javaRootDirectory = javaRootDirectory;
	}

	public static String getJavaDataFilename(String string) {
		return getEbToolsDatenDir()+"/" + string;
	}
}
