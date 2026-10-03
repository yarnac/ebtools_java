package com.eb.apps.ebtools.app;

import com.eb.apps.ebtools.tobj.DownloadsManager;
import com.eb.apps.ebtools.tobj.EbToolsManager;
import com.eb.apps.ebtools.tobj.Util;
import com.eb.base.EbAppContext;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.io.FileUtil;
import com.eb.apps.ebtools.gui.EbToolsView;
import com.eb.apps.ebtools.tvplayer.api.TvPlayerCtrl;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class EbToolsViewController {
	private final TvPlayerCtrl tvPlayerCtrl;
	private final EbToolsView view;
	private final EbToolsManager manager;
	private final GuiDecorator decorator;

	// Constants for file paths
	private static final String WINDOWS_DATA_DIR = "c:\\Users\\ekkart\\EbToolsDaten\\Jars\\";
	private static final String UNIX_DATA_DIR = "/Users/ekkart/EbToolsDaten/Jars/";

	public EbToolsViewController(EbToolsView newApp) {
		if (newApp == null) {
			throw new IllegalArgumentException("EbToolsView cannot be null");
		}

		view = newApp;
		manager = new EbToolsManager();

		IC.Size = 24;
		decorator = new GuiDecorator(view.getFrame(), manager.getIniFile(), "Einstellungen");
		view.getFrame().setIconImage(decorator.getImage(IC.TOOLS));
		Taskbar.getTaskbar().setIconImage(decorator.getImage(IC.TOOLS,32));

		addToolbarButtons();
		addMenus();
		registerEvents();

		tvPlayerCtrl = initTvPlayerCtrl();
	}

	private TvPlayerCtrl initTvPlayerCtrl() {
		TvPlayerCtrl controller = new TvPlayerCtrl(
				manager.getIniFile(),
				view.getTvPanel());

		return controller;
	}

	private void registerEvents() {
		// TODO: Implement actual event handlers
		// Example: view.addActionListener(e -> handleAction(e));
	}

	private void addMenus() {
		String jarFile = "/Users/ekkart/Data/Develop/Java/ebtools/target/EbTools-0.0.1-SNAPSHOT.jar";

		view.addMenuItem("Aktionen", "Inidatei bearbeiten", x -> manager.iniFileBearbeiten());
		view.addMenuItem("Aktionen", "Ai Playground öffnen",
				x -> startJava("com.eb.apps.ebchatclient.app.AiPlaygroundApp"));
		view.addMenuItem("Aktionen", "EbMusicPlayer öffnen",
				x -> startJava("com.eb.apps.ebmusic.app.EbMusicPlayerApp"));
		view.addMenuItem("Aktionen", "EbReader  öffnen",
				x -> startJava("com.eb.apps.ebookreader.app.EbReaderApp"));
		view.addMenuItem("Aktionen", "Wörterbuch öffnen",
				x -> startJava("com.eb.apps.ebwoerterbuch.app.WbApp"));




		// Removed useless Santanderbank menu item
		// Or replace with actual functionality:
		// view.addMenuItem("Aktionen", "Santanderbank", x -> openSantanderBank());
	}

	private void startJava(String className) {
		String[] args = new String[]{
				"java",
				"-cp",
				"/Users/ekkart/Data/Develop/Java/ebtools/target/EbTools-0.0.1-SNAPSHOT.jar",
				className
		};
		try {
			Runtime.getRuntime().exec(args);
		} catch (IOException e) {
			showMissingFileError(e.getMessage());
		}
	}

	private void addToolbarButtons() {
		decorator.addContainer("main", view.getToolBar());
		decorator.addTopMostButton("main", view.getFrame());

		// Clean up downloads
		decorator.addToolbarButton("main", "Clean up downloads", IC.CONTRAST_ADJUST,
				x -> new DownloadsManager(manager.getIniFile()).cleanUpDownloads());

		// Open Ini File
		decorator.addToolbarButton("main", "OpenIniFile", IC.EDITDOC,
				x -> manager.iniFileBearbeiten());

		// Open Java
		decorator.addToolbarButton("main", "Open Java", IC.BOX_FLOW,
				x -> manager.openJava());

		// Merge Santander CSVs
		decorator.addToolbarButton("main", "Kombiniere Santander Csvs", IC.COINS,
				x -> manager.mergeSantanderFiles());

		// Awake Sound (Toggle)
		decorator.addToggleButton("main", "Awake Sound", IC.CLOCK_PLAY, IC.CLOCK_STOP,
				x -> manager.holdSoundAwake());

		// Dictionary
		decorator.addToolbarButton("main", "Wörterbuch", IC.BOOKS_RED,
				x -> openOsCmdFile("Woerterbuch"));

		// eBook Reader
		decorator.addToolbarButton("main", "eBook Reader", IC.BookOpen,
				x -> openOsCmdFile("EbReader"));

		// Musicplayer (Single button)
		decorator.addToggleButton("main", "Musicplayer", IC.PLAY, IC.MusicLibraryPlay,
				x -> manager.holdSoundAwake());

		// Doubletten Finder
		decorator.addToolbarButton("main", "Doubletten", IC.ObjectMirrorHorizontal,
				x -> openOsCmdFile("EbDoubleFinder"));

		// Crypt
		decorator.addToolbarButton("main", "Crypt", IC.HELP_BLUE,
				x -> openOsCmdFile("Crypt"));
	}

	private void openOsCmdFile(String ebDoubleFinder) {
		String commandPath;

		if (EbAppContext.isWindows()) {
			commandPath = WINDOWS_DATA_DIR + ebDoubleFinder + ".cmd";
		} else {
			commandPath = UNIX_DATA_DIR + ebDoubleFinder + ".command";
		}

		// Validate file exists before opening
		if (!FileUtil.exists(commandPath)) {
			showMissingFileError(commandPath);
			return;
		}

		Util.startProcess(commandPath, "", false);
	}

	private void openCmdFile(String fileName) {
		String filePath = EbAppContext.getEbToolsDatenDir(fileName);

		if (filePath != null && FileUtil.exists(filePath)) {
			FileUtil.open(filePath);
		} else {
			showMissingFileError(filePath);
		}
	}

	private static void showMissingFileError(String filePath) {
		String errorMessage = "File not found: " + filePath;
		JOptionPane.showMessageDialog(null, errorMessage, "Fehler", JOptionPane.ERROR_MESSAGE);
		System.err.println(errorMessage);
	}
}