package com.eb.doubletten;

import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;

import com.eb.base.EbAppContext;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;

public class DoubleFinderPanelCtrl extends AbstrPanelCtrl {
	
	private DoubleFinderPanel panel;
	private String[] directories;
	
	private Doublette lastDoublette;
	private long compSize = 1024*10;
	List<Doublette> doubletten ;
	private JFrame frame;
	
	private JList lstOtherDoubles;
	private Doublette selectedDoublette;
	private boolean zufall;
	Random random;
	private List<Doublette> sichtbareDoubletten;
	private GuiDecorator guiDecorator;
	private IniFile iniFile;
	
	
	public DoubleFinderPanelCtrl(JFrame _frame, DoubleFinderPanel doubleFinderPanel)
	{
		panel = doubleFinderPanel;
		panel.register();
		panel.setSelectedDirectoryAction(y->selectDirectory(y));
		panel.setSelectedDoubletteAction(x->handleSelectedDoublette(x));
		panel.setKeysAction(x->handleDoublettenKey(x));						
		setFrame(_frame);

		setApplicationFrame(_frame);
		guiDecorator = new GuiDecorator();

		decorate();
	}
	
	private void decorate() {
		guiDecorator.addContainer("main", getPanel().getToolBar());
		
		guiDecorator.addToolbarButton("main", "Reload", IC.MB_PLAY, x->start(getPanel().getComboBox().getSelectedItem().toString()));
				
		guiDecorator.addToolbarButton("main", "Delete", IC.DELETE_RED, x->deleteDoubletten());
		guiDecorator.addToolbarButton("main", "Delete All", IC.SAVE_DEL, x->deleteDoublettenAll());
		guiDecorator.addToolbarButton("main", "Delete All", IC.MB_REVERSE, x->moveDuplicates());
		guiDecorator.addToolbarButton("main", "Delete All", IC.BOX_FLOW, x->removeDuplicates());
	}	
	
	
	

	@Override
	protected String getIniFileName() {		
		return EbAppContext.getJavaDataFilename("DoubleFinder.ini");
	}
	
	private void handleDoublettenKey(KeyEvent x) {
		if (x.getKeyChar()=='x')
			deleteDoublettenAll();
		if (x.getKeyChar()=='f')
			copyToFavorites(getSelectedDoublette().getName());
		if (x.getKeyChar()=='m')
			moveToUnreadable(getSelectedDoublette());
		if (x.getKeyChar()=='d')
			moveTo(getSelectedDoublette(), "~/Data/Medien/Porno/doubles");
		if (x.getKeyChar()=='z')
		{
			zufall = zufall ? false : true;
			random = new Random(new Date().getTime());
		}
		
		if (x.getKeyChar()=='r')
			deleteDoubletten();
		
		if (x.getKeyCode()==10)
			start("" + getPanel().getComboBox().getSelectedItem());
		
		
		if (x.getKeyChar()=='a')
		{
			if (getOnActivate()==null)
				setOnActivate(()->nextAfter500MS());
			else
				setOnActivate(null);
		}
			
		
		if (x.getKeyChar()=='p')
			switchAutoPlay();
	}
	
	private void nextAfter500MS() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (getOnActivate()!=null)
			next();
	}

	TimerTask timerTask;
	Timer timer;
	private void switchAutoPlay() {
		
		if (timer!=null)
		{
			timer.cancel();
			timer = null;
			System.out.println("TimerTask stopped");
			return;
		}
			
		
		timerTask = new TimerTask() {
			
			@Override
			public void run() {
				next();
				
			}
		};
        //running timer task as daemon thread
        timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, (int) (3.5*1000) );
        System.out.println("TimerTask started");
		
	}
	
	protected void next() {
		int n;
		if (zufall)
			n = random.nextInt(getSichtbareDoubletten().size());			
		else
			n = getPanel().getLstDoubletten().getSelectedIndex()+1;
		setIndex(n);
	}

	private void moveToUnreadable(Doublette d) {
		moveTo(d, "~/Data/Medien/Porno/unreadable");
	}

	private void moveTo(Doublette d, String string) {
		int index = getPanel().getLstDoubletten().getSelectedIndex();
		
		doubletten.remove(d);
		transferDoublettenToView();
		setIndex(index);
		
		FileUtil.move(d.getName(),string);
		for (String fname : d.getFiles()) {
			FileUtil.move(d.getName(),fname);
		}
		
		
	}

	private void copyToFavorites(String name) {
		FileUtil.copy(name, "~/Data/Medien/Porno/favs");
		
	}
	
	public void handleSelectedDoublette(Doublette x)
	{
		/*
		setOnActivate(null);
		if (timer!=null)
		{
			timer.cancel();
			timer = null;
		}
		*/
		selectDoublette(x);
	}

	private void selectDoublette(Doublette x) {
		setSelectedDoublette(x);
		if (x==null)
			return;
		showFile(x);
		showDoubles(x);
		int index = getPanel().getLstDoubletten().getSelectedIndex();
		getIniFile().setSectionValue("Einstellungen", "Index", ""+index);
		getIniFile().Write();
		
	}

	private void showDoubles(Doublette x) {
		if (getLstOtherDoubles()==null)
			return;
		
		if (x.getFiles().size()==0)
			return;
		
		DefaultListModel<String> model = new DefaultListModel<>();
		model.addElement(x.getName());
		x.getFiles().forEach(xx->model.addElement(xx));
		getLstOtherDoubles().setModel(model);
		
		
	}

	private void showFile(String s) {
		if (!new File(s).exists())
			return;
		FileUtil.open(s);
		try {
			//Thread.currentThread().sleep(1000);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void showFile(Doublette x) {
		showFile(x.getName());		
	}
	


	
	
	
	private void handleDoublettenFileClicked(ListSelectionEvent x) {		
		if (getLstOtherDoubles().getSelectedValue()==null)
			return;
		String s = getLstOtherDoubles().getSelectedValue().toString();
		showFile(s);
	}

	private void moveDuplicates() {
		for (Doublette doublette : doubletten) {
			if (doublette.getFiles().size()==1)
				FileUtil.move(doublette.getFiles().get(0), "~/Data/Medien/Bilder/Tumblr/Doubles");
		}
	}

	private void removeDuplicates() {
		for (Doublette doublette : doubletten) {
			for (String string : doublette.getFiles()) {
				new File(string).delete();
			}			
		}
	}
	
	private void selectDirectory(String y) {
		start(y);
	}


	
	
	public void start(String dirname)
	{
		
		doubletten = FileUtil.getDoubles(dirname, compSize, dirname.endsWith("/"),1);
		
		doubletten.sort(new Comparator<Doublette>() {

			@Override
			public int compare(Doublette o1, Doublette o2) {
				int  res = o2.getFiles().size()-o1.getFiles().size();
				if (res!=0)
					return res;
				
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		
		transferDoublettenToView();		
	}


	private void transferDoublettenToView() {
		DefaultListModel<Doublette> model = new DefaultListModel<>();
		sichtbareDoubletten = new ArrayList<Doublette>();
		
		doubletten.forEach(x->addDoublette(model,x));
		getPanel().getLstDoubletten().setModel(model);
		Integer valueAsInteger = getIniFile().getSectionValueAsInteger("Einstellungen", "Index", null);
		if (valueAsInteger!=null)
			setIndex(valueAsInteger.intValue());
	}
	

	private void addDoublette(DefaultListModel<Doublette> model, Doublette x) {
		if (!isAllowedDoublette(x))
			return;
		
		if (isForbiddenDoublette(x))
			return;
		
		model.addElement(x);
		sichtbareDoubletten.add(x);
	}



	private boolean isAllowedDoublette(Doublette x) {
		String filterPos = panel.getFilterPos().toLowerCase();
		if (filterPos.length()==0)
			return true;
		
		boolean result = false;
		
		String[] split = filterPos.split(" ");
		for (String string : split) {
			boolean mustContain = string.startsWith("+");
			String filter = mustContain ? string.substring(1) : string;
			if (x.getName().toLowerCase().contains(filter))
				result = true;
			else if (mustContain)
				return false;
		}
		return result;
	
	}

	private boolean isForbiddenDoublette(Doublette x) {
		String filterNeg = panel.getFilterNeg().toLowerCase();
		if (filterNeg.length()==0)
			return false;
		
		String[] split = filterNeg.split(" ");
		for (String string : split) {
			if (x.getName().toLowerCase().contains(string))
				return true;
		}
		return false;
	
	}




	public DoubleFinderPanel getPanel() {
		return panel;
	}

	public void setPanel(DoubleFinderPanel panel) {
		this.panel = panel;
		
	}

	public void deleteDoubletten() {
		Doublette d = (Doublette) getPanel().getLstDoubletten().getSelectedValue();
		int index = getPanel().getLstDoubletten().getSelectedIndex();
		for (String   fn : d.getFiles()) {
			File f = new File(fn);
			f.delete();			
		}
		doubletten.remove(d);
		transferDoublettenToView();
		setIndex(index);
		
		
	}

	public void deleteDoublettenAll() {
		Doublette d = (Doublette) getPanel().getLstDoubletten().getSelectedValue();
		int index = getPanel().getLstDoubletten().getSelectedIndex();
		for (String   fn : d.getFiles()) {
			File f = new File(fn);
			f.delete();			
		}
		File f = new File(d.getName());
		f.delete();
				
		doubletten.remove(d);
		transferDoublettenToView();
		setIndex(index);
		
		
	}


	public String[] getDirectories() {
		return directories;
	}


	public void setDirectories(String[] strings) {
		this.directories = strings;
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(strings);
		getPanel().getComboBox().setModel(model);
	}


	public JFrame getFrame() {
		return frame;
	}


	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JList getLstOtherDoubles() {
		return lstOtherDoubles;
	}

	public void setLstOtherDoubles(JList lstOtherDoubles) {
		this.lstOtherDoubles = lstOtherDoubles;
		getLstOtherDoubles().addListSelectionListener(x->handleDoublettenFileClicked(x));
	}

	public Doublette getSelectedDoublette() {
		return selectedDoublette;
	}

	public void setSelectedDoublette(Doublette selectedDoublette) {
		this.selectedDoublette = selectedDoublette;
	}

	private void setIndex(int n) {
		getPanel().setIndex(n);
	}



	public List<Doublette> getSichtbareDoubletten() {
		return sichtbareDoubletten;
	}



	public void setSichtbareDoubletten(List<Doublette> sichtbareDoubletten) {
		this.sichtbareDoubletten = sichtbareDoubletten;
	}	
}
