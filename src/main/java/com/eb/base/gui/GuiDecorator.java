package com.eb.base.gui;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;


public class GuiDecorator {
	private final IniFile iniFile;
	private JFrame frame;
	private JMenuBar menuBar;
	private JFrame Container;
	private Dictionary<String, JMenu> nameToMenuBarDictionary = new Hashtable<>();
	private Dictionary<String, Container> nameToContainerDictionary = new Hashtable<>();
	private String currentMenuName;

	public GuiDecorator() {
		iniFile = null;
	}

	public GuiDecorator(JFrame frame, IniFile newIniFile, String einstellungen) {
		GuiPersister.registerAndLoadStatus(frame, newIniFile,einstellungen);
		iniFile = newIniFile;
	}

	public JMenu getMenu(String label)
	{
		JMenu jMenu = nameToMenuBarDictionary.get(label);
		if (jMenu==null)
		{
			jMenu = new JMenu(label);
			getMenuBar().add(jMenu);	
			nameToMenuBarDictionary.put(label,  jMenu);
		}
		return jMenu;				
	}
	
	public void addMenuItem(String menuString, String label, Runnable object) {
		JMenu menu = getMenu(menuString);
		
		JMenuItem item = new JMenuItem(label);
		item.addActionListener(x->object.run());
		menu.add(item);
	}

	public JButton addToolbarButton(String toolbarName, String tooltipText, IC ic, ActionListener listener) {
		
		Container toolbar = fetchContainer(toolbarName);
		
		JButton btn = new JButton();

        try {
			btn.setIcon(ic.getImageIcon());
        } catch (Exception e) {
			System.out.println("Image nicht gefunden: " + ic);
        }
        btn.setToolTipText(tooltipText);
		toolbar.add(btn);
		btn.addActionListener(listener);

		if (!(toolbar instanceof JToolBar)) {
			toolbar.revalidate();
			toolbar.repaint();
		}

		return btn;		
	}
	
	
	public JButton addToolbarButton(String toolbarName, String tooltipText, String image, ActionListener listener) {
		
	
		Container toolbar = fetchContainer(toolbarName);
		
		JButton btn = new JButton("");
		btn.setIcon(new ImageIcon(GuiDecorator.class.getResource("/gr24/" + image + ".gif")));
		btn.setToolTipText(tooltipText);
		toolbar.add(btn);
		btn.addActionListener(listener);
		return btn;
	}

	private Container fetchContainer(String toolbarName) {
		Container toolbar = nameToContainerDictionary.get(toolbarName);
		if (toolbar!=null)
			return toolbar;
		
		toolbar = (JToolBar) GuiUtil.findComponent(getFrame(), x->x instanceof JToolBar);
		nameToContainerDictionary.put(toolbarName, toolbar);
		
				
			
		return toolbar;
	}
	
	public JMenuBar getMenuBar() {
		if (menuBar!=null)
			return menuBar;
		if (getFrame()==null)
			return null;
		if (getFrame().getJMenuBar()==null)
		{
			menuBar = new JMenuBar();
			getFrame().setJMenuBar(menuBar);
		}
		else
			menuBar = getFrame().getJMenuBar();
			
		return menuBar;
	}

	public void setMenuBar(JMenuBar menu) {
		this.menuBar = menu;
	}

	public void addContainer(String string, Container toolbarVokabeln) {
		nameToContainerDictionary.put(string, toolbarVokabeln);
	}

	public void addMenuSeparator(String string) {
		JMenu menu = getMenu(string);
		menu.add(new JSeparator());
		
		
	}

	public void setCurrentMenu(String string) {
		currentMenuName = string;
		// TODO Auto-generated method stub
		
	}

	public void addMenuItem(String string, Runnable object) {
		addMenuItem(currentMenuName, string, object);
		
	}

	public void addMenuSeparator() {
		addMenuSeparator(currentMenuName);
		
	}

	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public JToggleButton addToggleButton(String toolbarName, String tooltipText, IC pinOrange, IC pinRed,
			Consumer<Boolean> listener) {

		Container toolbar = nameToContainerDictionary.get(toolbarName);
		if (toolbar==null)
			return null;

		Image image = pinOrange.getCachedImage();
		Image selectedImage = pinRed.getCachedImage();

		JToggleButton btn = new JToggleButton("");

		btn.setIcon(new ImageIcon(image));
		btn.setSelectedIcon(new ImageIcon(selectedImage));

		btn.setToolTipText(tooltipText);
		toolbar.add(btn);
		btn.addActionListener(x->listener.accept(Boolean.valueOf(btn.isSelected())));
		return btn;
	}

	public void addToolbarSeparator(String toolbarName) {
		Container toolbar = fetchContainer(toolbarName);

		/*
		JSeparator seo = new JSeparator();
		seo.setMaximumSize(new Dimension(20,200));
		seo.setOpaque(true);
		seo.setSize(202,20);				
		seo.getInsets().left = 4;
		toolbar.add(seo);
		*/					
	}
	
	@SuppressWarnings("unchecked")
	public  <T> JComboBox<T> addToolbarComboBox(String toolbarName, String tooltipText, List<T>elements, Consumer<T> consumer)
	{
		JComboBox<T> comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Arial", Font.PLAIN, 20));
		DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>();
		elements.stream().forEach(x->model.addElement(x));
		comboBox.setModel(model);
		fetchContainer(toolbarName).add(comboBox);
		comboBox.addActionListener(x->consumer.accept((T)comboBox.getSelectedItem()));
		return comboBox;
	}
	
	@SuppressWarnings("unchecked")
	public  <T> JComboBox<T> addToolbarComboBox(String toolbarName, String tooltipText, T[] elements, Consumer<T> consumer)
	{
		JComboBox<T> comboBox = new JComboBox<>();
		comboBox.setFont(new Font("Arial", Font.PLAIN, 10));
		DefaultComboBoxModel<T> model = new DefaultComboBoxModel<T>(elements);		
		comboBox.setModel(model);
		fetchContainer(toolbarName).add(comboBox);
		comboBox.addActionListener(x->consumer.accept((T)comboBox.getSelectedItem()));
		return comboBox;
	}

	public void addTopMostButton(String toolBarName, JFrame jFrame) {
		addToggleButton(toolBarName,  "Topmost", IC.PIN_ORANGE, IC.PIN_RED, x->switchTopMost(jFrame));
	}

	public void addWatchClipboardButton(String toolBarName, JFrame jFrame, Consumer<Boolean> handleWatchClipboardChanged) {
		addToggleButton(toolBarName,  "Watch Clipboard", IC.CLIPBOARD_DEL, IC.CLIPBOARD_UP, x->switchTopMost(jFrame));
	}

	private void switchTopMost(JFrame jFrame) {
		jFrame.setAlwaysOnTop(!jFrame.isAlwaysOnTop());
	}

	public void addEditIniFileButton(String toolBarName, String fileName) {
		addToolbarButton(toolBarName, "Edit Inifile", IC.EDITDOC, x->OpenFile(fileName));
		
	}

	public void addEditIniFileButton(String toolBarName) {
		if (iniFile==null)
			throw (new RuntimeException("Ini File not set."));
		addToolbarButton(toolBarName, "Edit Inifile", IC.EDITDOC, x->OpenFile(iniFile.getFileName()));

	}

	private void OpenFile(String fileName) {
		FileUtil.open(fileName);
	}

	public Image getImage(IC ic) {
		return ic.getCachedImage();
	}

	public void addMouseListener(Component list, Runnable action) {
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					action.run();
				}
			}
		});
	}

	public void addKeyHandler(JComponent list, String key, Runnable action) {
		InputMap inputMap = list.getInputMap(JComponent.WHEN_FOCUSED);
		ActionMap actionMap = list.getActionMap();
		String upperCaseKey = key.toUpperCase();
		String actionMapKey = "pressed" + upperCaseKey;

		// Taste registrieren
		inputMap.put(KeyStroke.getKeyStroke(upperCaseKey), actionMapKey);


		actionMap.put(actionMapKey, new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				action.run();
			}
		});
	}
}
