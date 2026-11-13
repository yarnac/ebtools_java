package com.eb.apps.ebtools.gui;

import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.base.gui.ICF;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;


public class EbToolsView extends JFrame {

	private JMenuBar menuBar;	
	private JToolBar toolBar;	
	private Dictionary<String, JMenu> menuDict = new Hashtable<>();
	private JPanel tvPanel;

	public JPanel getTvPanel() {
		return tvPanel;
	}

	private JMenu addMenu(String string) {
		JMenu menu = new JMenu(string);
		getJMenuBar().add(menu);
		return menu;
	}


	public void addMenuItem(String menuLabel, String label, ActionListener actionListener) {
		JMenu menu = menuDict.get(menuLabel);
		if (menu==null)
		{
			menu = addMenu(menuLabel);
			menuDict.put(menuLabel, menu);
		}
			
		JMenuItem jMenuItem = new JMenuItem(label);
		jMenuItem.addActionListener(actionListener);
		menu.add(jMenuItem);
	}
   
	

	/**
	 * Create the application.
	 */
	public EbToolsView() {
		//java.awt.Taskbar.getTaskbar().setIconImage(IC.TOOLS_HR.getImage32());
		initialize();


		//java.awt.Desktop.getDesktop().setDefaultMenuBar(menuBar);		
	}
	

	private Image getImage(String image)
	{
		return Toolkit.getDefaultToolkit().getImage(EbToolsView.class.getResource("/gr32/" + image + ".gif"));
	}
	

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setTitle("Tools");
		Image image = getImage("Tools");
		setIconImage(image);
		setBounds(100, 100, 450, 75);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setJMenuBar(new JMenuBar());

		add(getToolBar(),BorderLayout.NORTH);

		{
			tvPanel = new JPanel();
			getContentPane().add(tvPanel, BorderLayout.CENTER);
		}
		
	}

	public JToolBar getToolBar() {
		if (toolBar == null) {
			toolBar = new JToolBar();
			toolBar.setFloatable(false);
		}
		return toolBar;
	}

	public void setToolBar(JToolBar toolBar) {
		this.toolBar = toolBar;
	}

}
