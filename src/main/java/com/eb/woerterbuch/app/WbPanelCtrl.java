package com.eb.woerterbuch.app;

import com.eb.base.SearcherPair;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.io.FileUtil;
import com.eb.ebookreader.tobj.StringUtil;
import com.eb.system.ClipboardAdapter;
import com.eb.system.KeyEventListenerBuilder;
import com.eb.system.WordStack;
import com.eb.woerterbuch.gobj.*;
import com.eb.woerterbuch.gui.WbGoogleEinstDlg;
import com.eb.woerterbuch.gui.WbPanelView;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

@SuppressWarnings("nls")
public class WbPanelCtrl {
	
	private WbPanelView view;
	private WbManager manager; 		
	ClipboardAdapter clipboardAdapter;
	private TopMostHandler topMostHandler;
	
	String oldwort;
	private ActionListener requestFocusActionListener;	
	private boolean useAllLoaded;
	private JButton btnPrev;
	private JButton btnNext;
	private boolean ignoreEvents;	
	private JComboBox<String> cbSprache;
	JComboBox<SearcherPair<Vokabel>> cbSearchStrategies;
	private WbDirection direction;
	private JComboBox<WbDirection> cbDsirection;
	private JToggleButton btnWatchClipboard;
	private StringBuilder cache;
	
	
	interface TopMostHandler
	{
		public void setTopMost(boolean value);
	}
	
	public WbPanelCtrl()
	{
		setManager(new WbManager());
		clipboardAdapter = new ClipboardAdapter(x->clipBoardChanged());
		cache = new StringBuilder();		
		
	}
	
	protected void setView(WbPanelView panel) {
		this.view = panel;
		
		GuiDecorator decorator = view.getDecorator();
		addToolbarItems(decorator);
		
		btnWatchClipboard.setSelected(true);
		
		addMenuItems(decorator);
		handleStackStatus(false, false);				
		registerViewEvents();		
		transferModelToView();
	}
	

	private void registerViewEvents() {
		view.addWortChangedHandler( ()->EventQueue.invokeLater(()->requery()));								
		view.addKeyListener(getKeyListener());
		view.addNodeSelectedListener((TreeNode node)->nodeSelected(node));			
	}
			
	private void addToolbarItems(GuiDecorator decorator) {
		
		addToolbarItemsMain(decorator);			
		addToolbarItemsWord(decorator);		
		addToolbarItemsVokabeln(decorator);
	}

	private void addToolbarItemsVokabeln(GuiDecorator decorator) {
		decorator.addToolbarButton("Vokabeln", "Cleanup", IC.BLANK_DOC, x->cleanUpVokabel());
		decorator.addToolbarButton("Vokabeln", "Save Add", IC.SAVE_ADD, x->saveVokabel());
		decorator.addToolbarButton("Vokabeln", "Save Replace", IC.SAVE_DEL, x->saveReplaceVokabel());
	}

	private void addToolbarItemsWord(GuiDecorator decorator) {
		btnPrev = decorator.addToolbarButton("Word", "Prev", IC.MB_REVERSE, x->showPreviousSuchwort());
		btnNext = decorator.addToolbarButton("Word", "Next", IC.MB_PLAY, x->showNextSuchwort());	
		
		decorator.addToolbarButton("Word", "Sprachsuche", IC.MALE_USER, x->showSprachsuche());
		decorator.addToolbarButton("Word", "Google Bilder", IC.PHOTO, x->showBildersuche());
		decorator.addToolbarButton("Word", "Google Suche", IC.WEB, x->showWebsuche());
		decorator.addToolbarButton("Word", "Verbix", IC.TABLE, x->showVerbixsuche());		
	}

	private void addToolbarItemsMain(GuiDecorator decorator) {
		decorator.addToggleButton("Main", "TopMost",IC.PIN_ORANGE, IC.PIN_RED,x->getTopMostHandler().setTopMost(x.booleanValue()));
		btnWatchClipboard = decorator.addToggleButton("Main", "Copy", IC.CLIPBOARD_DEL, IC.CLIPBOARD_UP, x->setFollowClipboard(x.booleanValue()));
		decorator.addToggleButton("Main", "Use All Woerterbücher", IC.BOOKS, IC.BOOKS_RED, x->setUseAllLoaded(x.booleanValue()));
				
		decorator.addToolbarSeparator("Main");
		decorator.addToolbarButton("Main", "Delete", IC.DELETE_RED, x->getManager().getAktWbSession().deleteVokabel(getWort()));
		
		cbSprache = decorator.addToolbarComboBox("Main", "Sprache", getManager().getSprachen(),x->{cbSpracheChanged(x);});
		decorator.addToolbarComboBox("Main", "Richtung",WbDirection.values(),x->{directionChanged(x);});
		cbSearchStrategies = decorator.addToolbarComboBox("Main", "Searcher", getManager().getAktWbSession().getSearcher(),x->searchStrategyChanged(x));
		
		
		decorator.addToolbarSeparator("Main");
		decorator.addToolbarButton("Main", "Wörter", IC.WORDS, x->getManager().openWordListDlg(getStack().getStrings()));
		decorator.addToolbarButton("Main", "Crypt", IC.GLASSES, x->getManager().openCrypter("" + cache));
		decorator.addToolbarButton("Main", "Java", IC.BOX_FLOW, x->getManager().openJava());	
		
	}	

	private void addMenuItems(GuiDecorator decorator) {
		decorator.setCurrentMenu("Datei");
		decorator.addMenuItem("Inidatei bearbeiten", ()->getManager().editIniFile());
		decorator.addMenuItem("Inidatei einlesen", ()->getManager().reloadIniFile());
		decorator.addMenuSeparator();
		decorator.addMenuItem("Stackdatei bearbeiten", ()->getManager().editStackFile());
		decorator.addMenuItem("Stackdatei einlesen", ()->getManager().reloadStackFile());
		decorator.addMenuSeparator();
		decorator.addMenuItem("Wörterbuch bearbeiten", ()->getManager().editCurrentWoerterbuch());
		decorator.addMenuSeparator();
		decorator.addMenuItem("Google Export", ()->manager.getAktWbSession().getWoerterbuch().exportForGoogle());
		
		
		decorator.addMenuItem("Einstellungen", "Einstellungen", ()->einstellungenClicked());
		
	}
	
	private KeyListener getKeyListener() {
		KeyEventListenerBuilder builder = new KeyEventListenerBuilder();
		return builder
		.addKeyReleased(e->e.getKeyChar()=='\n', e->pushWort(getWort()))
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_LEFT && e.isMetaDown(), e->showPreviousSuchwort())
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_RIGHT && e.isMetaDown(), e->showNextSuchwort())
		
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F1, e->showSprachsuche())
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F2, e->showBildersuche())
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F3, e->showWebsuche())
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F4, e->showVerbixsuche())
		
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F5, e->setSprache("Türkisch"))
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F6, e->setSprache("Französisch"))
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F7, e->setSprache("Portugiesisch"))
		.addKeyReleased(e->e.getKeyCode()==KeyEvent.VK_F8, e->setSprache("Spanisch"))
		
		.build();
	}
	

	private void nodeSelected(TreeNode node) {
		if (!node.isLeaf())
			return;
		
		Vokabel parentVokabel = getParentVokabel(node);
		cache.append(parentVokabel.toStringCrypt());
				
		List<Vokabel> res = getVokabelnForNode(node, parentVokabel);
		
		EventQueue.invokeLater(()->view.appendVokabelToNode(res, (DefaultMutableTreeNode) node));
		
	}

	private Vokabel getParentVokabel(TreeNode node) {
		DefaultMutableTreeNode parent = (DefaultMutableTreeNode) node.getParent();
		Vokabel v = (Vokabel) parent.getUserObject();
		return v;
	}

	private List<Vokabel> getVokabelnForNode(TreeNode node, Vokabel parentVokabel) {
		
		
		WbDirection newDirection = parentVokabel.getDirection().getOpposite();
		WbDirection oldDirection = getManager().getAktWbSession().getDirection();
		
		if (oldDirection!=newDirection)
			manager.getAktWbSession().setDirection(newDirection);
		
		
		List<Vokabel> res = new ArrayList<>();
		for (String str : Vokabel.getStringsFromBedeutung(node.toString())) {
			res.addAll(manager.fetchVokabeln(str, isUseAllLoaded()));
		}
				
		if (oldDirection!=newDirection)
			manager.getAktWbSession().setDirection(oldDirection);
		return res;
	}
	

	private void searchStrategyChanged(SearcherPair<Vokabel> x) {
		manager.getAktWbSession().setTheSearcher(x);
		einstellungenChanged();
	}

	
	private void directionChanged(WbDirection newDirection) {
		manager.getAktWbSession().setDirection(newDirection);
		einstellungenChanged();
	}
	
	private void  spracheChanged() {
		manager.setWbSession("" + cbSprache.getSelectedItem());
		einstellungenChanged();		
	}

	private void cbSpracheChanged(String x) {
		
		setSprache(x);
		spracheChanged() ;
	}

	private void einstellungenChanged() {
		if (ignoreEvents)
			return;
		transferModelToView();
		oldwort = "";
		ignoreEvents = false;
		requery();
	}
	
	
	private void transferModelToView() {
		ignoreEvents = true;
		
		WoerterbuchSession aktWbSession = manager.getAktWbSession();		
		setSprache(aktWbSession.getSprache());		
		setDirection(aktWbSession.getDirection());
		
		setListContents(cbSearchStrategies, getManager().getAktWbSession().getSearcher());
		cbSearchStrategies.setSelectedItem(getManager().getAktWbSession().getTheSearcher());
		setStatus();
		
		ignoreEvents = false;
	}	
	
	private <T> void  setListContents(JComboBox<T> cb, List<T> items) {
		cb.removeAllItems();
		for (T t : items) {
			cb.addItem(t);
		}
	}
	
	private interface ISaveVokabelStrategy {
		void save(String wort, Iterable<String> bedeutungen);
	}

	private void saveReplaceVokabel() {saveVokabel((wort, bed)->getManager().replaceVokabel(new Vokabel(wort, bed)));}
	private void saveVokabel() {saveVokabel((wort, bed)->getManager().addVokabel(new Vokabel(wort, bed)));}

	private void saveVokabel(ISaveVokabelStrategy strategy) { 
		String[] bedeutungen = view.getDefinition().split("\n");
		List<String> lst = new ArrayList<>();
		for (String string : bedeutungen) {
			lst.add(string);
		}
		strategy.save(getWort(), lst); ;
	}

	
	private void cleanUpVokabel() {
		String str = view.getDefinition();
		str  = str.replace("Standard Türkisch-Deutsch\n'","").trim();
		String[] split = str.split("\n");
		str = split[0];
		split = str.split(";");
		StringBuilder strb = new StringBuilder();
		Arrays.stream(split).forEach(x->{strb.append( x + "\n");});
		
		view.setDefinition(strb.toString());		
	}
	
	private void showSprachsuche() {showGoogle((wb)->wb.getGoogleStringSprache());}
	private void showVerbixsuche() {showGoogle(wb->wb.getGoogleStringVerbix());}
	private void showBildersuche() {showGoogle(wb->wb.getGoogleStringBilder());}
	private void showWebsuche() {showGoogle(wb->wb.getGoogleString());}

	private void showGoogle(Function<Woerterbuch, String> f) {
		String wort = getWort();
		if (wort==null||wort.trim().length()==0)
			return;
		
		String googleString = f.apply(manager.getAktWbSession().getWoerterbuch());
		String urlString = googleString.replace("{0}", FileUtil.encodeForUrl(wort));
		FileUtil.showWebseite(urlString);
	}
	
	public void einstellungenClicked() {
		WbGoogleEinstDlg dialog = new WbGoogleEinstDlg();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setEinstellungen(getManager().getAktWbSession().getWoerterbuch().getEinstellungen());
		dialog.setVisible(true);		
		dialog.addOnSavedListener(x->optionenSaved(dialog));
	}	
	
	private void clipBoardChanged() {
		
		if (!isFollowClipboard())
			return;
		
		String wordFromClipboard = clipboardAdapter.getText();	
		
		if (wordFromClipboard.contains("::"))
		{
			String sprache = StringUtil.substringBeforeFirst(wordFromClipboard, "::", false);
			wordFromClipboard = StringUtil.substringAfterFirst(wordFromClipboard, "::", false);
			if (!sprache.equals(cbSprache.getSelectedItem()))
			{
				ignoreEvents = true;
				cbSpracheChanged(sprache);
				ignoreEvents = false;				
			}
		}
		final String  w = wordFromClipboard;
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				pushWort(w);
				
				if (getRequestFocusActionListener() != null)
					getRequestFocusActionListener().actionPerformed(null);		
			}
		});
									
	} 
	
	
	
	private void setSprache(String newSprache) {
		
		if (newSprache==null || newSprache.equals(cbSprache.getSelectedItem()))
			return;
				
		cbSprache.setSelectedItem(newSprache);				
	}
		
	private void optionenSaved(WbGoogleEinstDlg dialog) {
		WbEinstellungen einstellungen = dialog.getEinstellungen();
		getManager().getAktWbSession().getWoerterbuch().setEinstellungen(einstellungen);
	}

			
	private void requery() {
		String wort = getWort();
		if (wort.contains("  "))
		{
			EventQueue.invokeLater(()->view.setWort(""));
			
			return;
		}
		
		cache = new StringBuilder();
		view.setVokabelnSet(new HashSet<>());
			
		if (wort.equals(oldwort))
			return;
		oldwort = wort;
		if (wort.length()<2)
		{
			view.showVokabeln(new ArrayList<Vokabel>());
			return;
		}
		
		List<Vokabel> fetchVokabeln = manager.fetchVokabeln(wort, isUseAllLoaded());
		view.showVokabeln(fetchVokabeln);
	}
	
	protected WbPanelView getView() {
		return view;
	}

	public WbManager getManager() {
		return manager;
	}


	public void setManager(WbManager manager) {
		this.manager = manager;
	}


	// erlaubt es dem Controller das Fenster in den Fordergrund zu bringen, zum Beispiel, wenn 
	// im Clipboard etwas geaendert wird. 
	public void setRequestFocusActionListener(ActionListener actionListener) {
		requestFocusActionListener = actionListener;
		
	}

	public ActionListener getRequestFocusActionListener() {
		return requestFocusActionListener;
	}

	public TopMostHandler getTopMostHandler() {
		return topMostHandler;
	}

	public void setTopMostHandler(TopMostHandler topMostHandler) {
		this.topMostHandler = topMostHandler;
	}

	public boolean isFollowClipboard() {
		return btnWatchClipboard.isSelected();
	}

	public void setFollowClipboard(boolean followClipboard) {
		btnWatchClipboard.setSelected(followClipboard);
	}

	public boolean isUseAllLoaded() {
		return useAllLoaded;
	}

	public void setUseAllLoaded(boolean useAllLoaded) {
		this.useAllLoaded = useAllLoaded;
	}
	
	private void handleStackStatus(boolean x, boolean y) {
		btnNext.setEnabled(x);
		btnPrev.setEnabled(y);
	}

	
	public void pushWort(String wordFromClipboard) {
		if (getWort()!=null && getWort().length()>0)
			getStack().addWord(wordFromClipboard);
		setWort(wordFromClipboard);
		setStatus();
	}
	
		public void showNextSuchwort() {
 		String pushWord = getStack().getNext();
 		if (pushWord!=null)
			setWort(pushWord);
		setStatus();
	}
	
	public void showPreviousSuchwort() {						
		String popWord = getStack().getPrev(getWort());		
		if (popWord!=null)
			view.setWort(popWord);
		setStatus();
	}
	
	private void setWort(String wordFromClipboard) {
		view.setWort(wordFromClipboard);
	}

	private String getWort() {
		return view.getWort();
	}
	
	private void setStatus() {
		handleStackStatus(getStack().hasNext(), getStack().hasPrev());
	}

	public WordStack getStack() {
		return getManager().getAktWbSession().getStack();
	}

	public WbDirection getDirection() {
		return direction;
	}

	public void setDirection(WbDirection direction) {
		this.direction = direction;
	}
}

