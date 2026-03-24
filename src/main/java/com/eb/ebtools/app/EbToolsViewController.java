package com.eb.ebtools.app;

import com.eb.base.EbAppContext;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.io.FileUtil;
import com.eb.ebtools.tobj.*;
import com.eb.ebtools.gui.EbToolsView;
import com.eb.ebtools.tvplayer.api.TvPlayerCtrl;


public class EbToolsViewController {
	private final TvPlayerCtrl tvPlayerCtrl;
	private EbToolsView view;
	private EbToolsManager manager;
	private GuiDecorator decorator;

	public EbToolsViewController(EbToolsView newApp) {
		view = newApp;
		manager = new EbToolsManager();

		IC.Size = 24;
		decorator = new GuiDecorator(view.getFrame(), manager.getIniFile(),"Einstellungen");

		
		addToolbarButtons();
		addMenus();
		registerEvents();

		tvPlayerCtrl = initTvPlayerCtrl();
	}

	private TvPlayerCtrl  initTvPlayerCtrl() {

		TvPlayerCtrl controller = new TvPlayerCtrl(
				manager.getIniFile(),
				view.getTvPanel());

		return controller;
	}

	private void registerEvents() {
	}

	private void addMenus() {		
		view.addMenuItem("Aktionen", "Inidatei bearbeiten", x->manager.iniFileBearbeiten());
		view.addMenuItem("Aktionen", "Santanderbank", x->this.toString());
	}


	private void addToolbarButtons() {
		decorator.addContainer("main", view.getToolBar());
		decorator.addTopMostButton("main", view.getFrame());
		decorator.addToolbarButton("main", "Clean up downloads", IC.CONTRAST_ADJUST, x->new DownloadsManager(manager.getIniFile()).cleanUpDownloads());
		decorator.addToolbarButton("main", "OpenIniFile", IC.EDITDOC, x->manager.iniFileBearbeiten());
		decorator.addToolbarButton("main", "Open Java", IC.BOX_FLOW, x->manager.openJava());
		decorator.addToolbarButton("main", "Kombiniere Santander Csvs", IC.COINS, x->manager.mergeSantanderFiles());
		decorator.addToggleButton("main", "Awake Sound", IC.CLOCK_PLAY, IC.CLOCK_STOP, x->manager.holdSoundAwake());
		decorator.addToolbarButton("main", "Wörterbuch", IC.BOOKS_RED, x->openOsCmdFile("Woerterbuch"));
		decorator.addToolbarButton("main", "eBook Reader", IC.BookOpen,x->openOsCmdFile("EbReader"));
		decorator.addToolbarButton("main", "Musicplayer", IC.PLAY,x->openOsCmdFile("EbMusicPlayer"));
		decorator.addToolbarButton("main", "Musicplayer", IC.MusicLibraryPlay,x->openOsCmdFile("EbMusicPlayer"));
		decorator.addToolbarButton("main", "Doubletten", IC.ObjectMirrorHorizontal,x->openOsCmdFile("EbDoubleFinder"));
		decorator.addToolbarButton("main", "Doubletten", IC.HELP_BLUE,x->openOsCmdFile("Crypt"));
	}

	private void openOsCmdFile(String ebDoubleFinder) {

		// Util.startProcess("C:\\Users\\ekkart\\.jdks\\jbr-17.0.12\\bin\\java.exe", "-cp EbTools-0.0.1-SNAPSHOT.jar com.eb.ebmusic.EbMusicPlayerApp", false);


		if (EbAppContext.isWindows())
			Util.startProcess(String.format("c:\\Users\\ekkart\\EbToolsDaten\\Jars\\%s.cmd", ebDoubleFinder), "", false);
			// openCmdFile(String.format("Jars/%s.cmd", ebDoubleFinder));
		else
			Util.startProcess(String.format("/Users/ekkart/EbToolsDaten/Jars/%s.command", ebDoubleFinder), "", false);
	}

	private void openCmdFile(String fileName) {
		System.out.println(EbAppContext.getEbToolsDatenDir(fileName));

		FileUtil.open(EbAppContext.getEbToolsDatenDir(fileName));
	}
}
