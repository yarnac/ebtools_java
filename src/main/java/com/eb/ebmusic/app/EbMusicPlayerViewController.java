package com.eb.ebmusic.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.JToolBar;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.eb.base.EbAppContext;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.GuiPersister;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;

import com.eb.ebmusic.tobj.EbLog;
import com.eb.ebmusic.gui.EbMusicPlayerView;
import com.eb.ebmusic.tobj.MusicTreeNode;
import com.eb.misc.EbUtils;
import com.eb.ebmusic.gobj.EbMusicLib;
import com.eb.ebmusic.gobj.MusicFolder;
import com.eb.ebmusic.tobj.MusicPlayer;

public class EbMusicPlayerViewController {
	
	private MusicTreeNode selectedNode;
	private MusicPlayer musicPlayer;
	private IniFile inifile;
	
	private EbMusicLib aktuelleBibliothek;
	private List<EbMusicLib> bibliotheken;
	private String filterString;
	private EbMusicPlayerView view;
	private MusicTreeNode rootNode;
	

	
	public EbMusicPlayerViewController(EbMusicPlayerView window)
	{
		EbLog.log("Init Music Player()");
		try {
			initializeView(window);
			initMusicPlayer();
			initIniFile();
			initializeBibliotheken();
			GuiPersister.registerAndLoadStatus(window.getFrame(), inifile,"Einstellungen");
		}
		catch(Exception e)
		{

			EbLog.log(e.getLocalizedMessage());
			EbLog.log(e.getStackTrace()[0].getClassName());
			EbLog.log(e.getStackTrace()[0].getMethodName());
		}
		
	}

	private void initIniFile() {
		String iniFilename = EbAppContext.getEbToolsDatenDir("EbMusicPlayer/EbMusicPlayer.ini");
		setInifile(IniFileProvider.createIniFile(iniFilename));
	}

	private void initMusicPlayer() {
		musicPlayer = new MusicPlayer();
		musicPlayer.setShowActTitle(x->ShowTitle(x));
	}

	private void handleBibliothekChanged(EbMusicLib ebMusLib) {
		setAktuelleBibliothek(ebMusLib);
		transferBibliothekToView();		
	}

	private void transferBibliothekToView() {
		setRootNode(new MusicTreeNode(getAktuelleBibliothek().getRootNode()));
		transferModelToView();		
	}

	private void initializeBibliotheken() {
		setBibliotheken(inifile.getSectionValues("Bibliotheken")
			.stream()
			.map(x->new EbMusicLib(x))
			.collect(Collectors.toList()));
		getView().setBibliotheken(getBibliotheken());

		selectBibliothek(inifile.getSectionValue("Einstellungen", "Bibliothek", ""));
		
	}
	
	private void selectBibliothek(String name) {
		
		Optional<EbMusicLib> first = getBibliotheken().stream().filter(x->x.getName().equals(name)).findFirst();
		if (first.isPresent())
		{
			String sectionValue = getInifile().getSectionValue("Selection", "SelectedNode", "");
			getView().selectBibliothek(first.get());			
		}
	}

	
	private void decorateToolStrip(JToolBar toolBar) {
		GuiDecorator decorator = new GuiDecorator();
		decorator.addContainer("main", getView().getToolBar());
		decorator.addTopMostButton("main", getView().getFrame());
		decorator.addToolbarButton("main", "Edit Inifile", IC.EDITDOC, (x)->editIniFile());
		decorator.addToolbarButton("main", "Reload IniFile", IC.REFRESHPAGE, (x)->transferModelToView());
		decorator.addToolbarButton("main", "Play Selection", IC.PLAY,  (x)->playSelection());
		decorator.addToolbarButton("main", "Previous Track", IC.PREV, (x)->previousTrack());
		decorator.addToolbarButton("main", "Pause Track", IC.PAUSE, (x)->pauseTrack());
		decorator.addToolbarButton("main", "Next Track", IC.NEXT, (x)->nextTrack());
		decorator.addToolbarButton("main", "Stop Music", IC.STOP, (x)->stopMusic());
		decorator.addToolbarButton("main", "Reload Bibliothek", IC.FROM_DB, (x)->reloadBibliothek());
	}
	
	public List<String> getFileNames(MusicTreeNode node)
	{
		List<String> filenamesToPlay = new ArrayList<>();
		if (node.isLeaf())
		{
			filenamesToPlay.add(node.getFolder().fetchFullPath());
			return filenamesToPlay;
		}
		
		for (int i=0; i<node.getChildCount(); i++) {
			MusicTreeNode child = (MusicTreeNode) node.getChildAt(i);
			filenamesToPlay.addAll(getFileNames(child));
		}

		return filenamesToPlay;
	}
	

	private void playSelection() {
		MusicTreeNode actNode = getSelectedNode();
		if (actNode==null)
			return;
		
		saveSelectedNodeToIniFile(actNode);
		
		TreeNode parent;
		int index;
		List<String> filenamesToPlay = new ArrayList<>();
		
		if (actNode.isLeaf())
		{
			parent = actNode.getParent();
			index = parent.getIndex(actNode);
			for (int i=index; i<parent.getChildCount(); i++)
			{
				MusicTreeNode child = (MusicTreeNode) parent.getChildAt(i);
				filenamesToPlay.add(child.getFolder().fetchFullPath());
			}			
		}			
		else
		{
			filenamesToPlay.addAll(getFileNames(actNode));			
		}

		musicPlayer.play(filenamesToPlay);										
	}

	private void pauseTrack() {
		musicPlayer.pause();
	}

	private void reloadBibliothek() {
		if (getAktuelleBibliothek()!=null)
		{
			getAktuelleBibliothek().updateContents();
			transferBibliothekToView();
		}
	}

	private void editIniFile() {
		getInifile().Write();
		FileUtil.open(getInifile().getFileName());
		FileUtil.open(getAktuelleBibliothek().getInifile().getFileName());
		FileUtil.open(getAktuelleBibliothek().fetchDatafilename());
		
	}

	private void previousTrack() {
		musicPlayer.playPreviousTrack();
	}

	private void nextTrack() {
		musicPlayer.playNextTrack();
	}

	private void stopMusic() {
		musicPlayer.stop();
	}

	private void ShowTitle(String x) {
		getView().showTitle(x);
	}

	public void transferModelToView()
	{
		filterString = EbUtils.getTrimmedString(getView().getFilterString());

		MusicTreeNode rn = filterString.length()==0
				? getRootNode()
				: new MusicTreeNode(getAktuelleBibliothek().getRootNode().filter(x->isFilter(x, filterString)));

		getView().getTree().setModel(new DefaultTreeModel(rn));
		
		String sectionValue = inifile.getSectionValue("Einstellungen", "LastNode", "");
		if (sectionValue.length()>0)
		{
			getView().findNode(sectionValue);
		}
	}
	
	private boolean isFilter(MusicFolder x, String filter) {
		return x.getPath().toLowerCase().contains(filter.toLowerCase());
	}

	public void initializeView(EbMusicPlayerView window)
	{					
		setView(window);
		window.setHandleBibliothekChanged(ebMusLib->handleBibliothekChanged(ebMusLib));

		getView().setHandleTreeDoubleClicked(()->handleTreeDoubleClicked());
		getView().setHandleSelectedMusicTreeNodeChanged(x->handleMusicTreeNodeChanged(x));
		getView().setHandleWindowClosed(x->SaveSelectedPath(x));
		decorateToolStrip(getView().getToolBar());
	}
	
	
	private void SaveSelectedPath(String x) {
	}

	private void handleMusicTreeNodeChanged(MusicTreeNode x) {
		setSelectedNode(x);
	}

	private void handleTreeDoubleClicked() {
		if (getSelectedNode()==null || !getSelectedNode().isLeaf())
			return;
		
		saveSelectedNodeToIniFile(getSelectedNode());
		
		TreeNode parent = getSelectedNode().getParent();
		int index = parent.getIndex(getSelectedNode());
		List<String> filenamesToPlay = new ArrayList<>();
		for (int i=index; i<parent.getChildCount(); i++)
		{
			MusicTreeNode child = (MusicTreeNode) parent.getChildAt(i);
			filenamesToPlay.add(child.getFolder().fetchFullPath());
		}
		musicPlayer.play(filenamesToPlay);					
	}

	private void saveSelectedNodeToIniFile(MusicTreeNode selectedNode2) {
		getInifile().setSectionValue("Einstellungen", "LastNode", selectedNode2.getFolder().fetchFullPath());
		getInifile().Write();
	}

	public MusicTreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(MusicTreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public MusicTreeNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(MusicTreeNode treeNode) {
		this.rootNode = treeNode;
	}

	public EbMusicPlayerView getView() {
		return view;
	}

	public void setView(EbMusicPlayerView view) {
		this.view = view;
	}

	public IniFile getInifile() {
		return inifile;
	}

	public void setInifile(IniFile inifile) {
		this.inifile = inifile;
	}	

	public List<EbMusicLib> getBibliotheken() {
		return bibliotheken;
	}

	public void setBibliotheken(List<EbMusicLib> bibliotheken) {
		this.bibliotheken = bibliotheken;
	}

	public EbMusicLib getAktuelleBibliothek() {
		return aktuelleBibliothek;
	}

	public void setAktuelleBibliothek(EbMusicLib aktuelleBibliothek) {
		this.aktuelleBibliothek = aktuelleBibliothek;
	}

}
