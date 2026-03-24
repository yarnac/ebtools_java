package com.eb.base.gui;

import javax.swing.*;
import java.awt.*;

/**
 * @author ekkart
 * 
 * Image Constants
 * 
 * Liefert die Icons, anzuschauen unter ~/Data/Develop/rsc
 * Namen identifizieren und ohne 31hi31 als neue enum Auspraegung hinterlegen
 * Dateien werden aus den Resourcen geladen nicht aus dem Verzeichnis
 *
 */


public enum IC 
{
	PIN_ORANGE("PinOrange"),
	PIN_RED("PinRed"),
		
	
	CLIPBOARD_DEL("Clipboard_Delete"),
	CLIPBOARD_UP("Clipboard_Up"),
	BOOKS("Books"),
	DELETE_RED("DeleteRed"),
	MALE_USER("MaleUserCasual1"),
	PHOTO("Photo"),
	WEB("WebGlobe"),
	TABLE("Table"),
	MB_PLAY("MediaButtonPlayBlue"),
	MB_REVERSE("MediaButtonReverseBlue"),
	
	MB_DOWN_BLUE("MediaButtonDownBlue"),
	MB_UP_BLUE("MediaButtonUpBlue"),

	CLOCK_PLAY("Clock_Play"),
	CLOCK_STOP ("Clock_Stop"),
	BOOKS_RED("BooksRed"),
	CONTRAST_ADJUST("ContrastAdjust"),
	BLANK_DOC("BlankDocument_Down"),
	SAVE_ADD("Save_Add"),
	SAVE_DEL("Save_Delete"),
	BOX_FLOW("BoxFlowHorizontalMixed"),
	BOX_FLOW_2("BoxFlowHorizontal_Add"),
	WORDS("Notebook_Filter"),
	NEXT("MediaButtonNextTrackOn"),
	PREV("MediaButtonPreviousTrackOn"),
	PAUSE("MediaButtonPauseOn"),
	STOP("MediaButtonStopOn"),
	PLAY("MediaButtonPlayOn"),
	REFRESHPAGE("RefreshPage"),
	EDITDOC("EditDocument"),
	FROM_DB("DatabaseRollBack"),
	FILTER("Filter"),
	Calendar("DeskCalendar_Info"), 
	TOOLS("Tools"), 
	MusicLibraryPlay("MusicLibrary_Play"),
	ObjectMirrorHorizontal("ObjectMirrorHorizontal"),
	BookOpen("BookOpen"), 
	CopyText("CopyText"), 
	GenericUserGroup("GenericUserGroup"),
	
	TOOLS_HR("Tools"),
	MusicLibrary_HR("HR_apple-music"),
	Dictionary_HW("dictionary"),
	eBookReader("book-reader"),
	GLASSES("WatchWindow"),
	BarChartStacked100pc("BarChartStacked100pc"),
	Triangle_Red("TriangleRed"),

// Insert before
	
	HELP_BLUE("HelpBlue"),
	COINS("Coins"),
	VIDEO_LIBRARY_SEARCH("VideoLibrary_Search");

	public static int Size = 16;

	@Override
	public String toString() {
		return N();
	}

	String fileName;
	Image cachedImage;
	ImageIcon cachedImageIcon;


	public String N(){
		return fileName;
	}
	
	private IC(String str)
	{
		fileName = str;
	}

	public java.awt.Image getCachedImage()
	{
		return getImage(Size);
	}

	private Image getImage(int size) {
		String fn = fileName;

		if (fileName.startsWith("HR_"))
			fn = fileName.substring("HR_".length());

		if (cachedImage ==null)
			cachedImage = Toolkit.getDefaultToolkit().getImage(IC.class.getResource("/gr" + size + "/" + fn + ".gif"));

		return cachedImage;
	}

	public ImageIcon getImageIcon() {
		if (cachedImageIcon ==null)
			cachedImageIcon = new ImageIcon(getCachedImage());
		return cachedImageIcon;
	}
}
