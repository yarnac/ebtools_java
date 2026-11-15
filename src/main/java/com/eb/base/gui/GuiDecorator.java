package com.eb.base.gui;

import com.eb.base.ai_service.llm_client.api.LlmRequest;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.System.out;


public class GuiDecorator implements GuiDecoratorToolBarMethods {
	private final IniFile iniFile;
	@Setter
    @Getter
    private JFrame frame;
	@Setter
    private JMenuBar menuBar;

	@Getter
	public final GuiDecoratorToolBars toolBarDecorator;

	private final Dictionary<String, JMenu> nameToMenuBarDictionary = new Hashtable<>();
	private String currentMenuName;
	private final List<Runnable> closeActions = new ArrayList<>();

	public GuiDecorator() {
		toolBarDecorator = new GuiDecoratorToolBars();
		iniFile = null;
	}



	public GuiDecorator(JFrame frame) {
		toolBarDecorator = new GuiDecoratorToolBars(frame);
		iniFile = null;
	}

	public GuiDecorator(JPanel panel) {
		toolBarDecorator = new GuiDecoratorToolBars(panel);
		iniFile = null;
	}





	public GuiDecorator(JFrame frame, IniFile newIniFile, String einstellungen) {
		this.frame = frame;
		iniFile = newIniFile;

		GuiPersister.registerAndLoadStatus(frame, newIniFile,einstellungen);
		toolBarDecorator = new GuiDecoratorToolBars(frame);

		WindowAdapter adapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for(Runnable action : closeActions) {
					action.run();
				}
				iniFile.Write();
			}
		};

		this.frame.addWindowListener(adapter);
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
	
	public JMenuItem addMenuItem(String menuString, String label, Runnable object) {
		JMenu menu = getMenu(menuString);
		
		JMenuItem item = new JMenuItem(label);

		item.addActionListener(x->object.run());
		menu.add(item);
		return item;
	}


	public JCheckBoxMenuItem addJCheckBoxMenuItem(String menuString, String label, Runnable object) {
		JMenu menu = getMenu(menuString);

		JCheckBoxMenuItem item = new JCheckBoxMenuItem(label);

		item.addActionListener(x->object.run());
		menu.add(item);
		return item;
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

	public JMenuItem addMenuItem(String string, Runnable object, int key, int mask) {
		JMenuItem item = addMenuItem(currentMenuName, string, object);
		item.setAccelerator( KeyStroke.getKeyStroke(key, mask) );
		return item;
	}

	public JCheckBoxMenuItem addJCheckBoxMenuItem(String string, Runnable object, int key, int mask) {
		JCheckBoxMenuItem item = addJCheckBoxMenuItem(currentMenuName, string, object);
		item.setAccelerator( KeyStroke.getKeyStroke(key, mask) );
		return item;
	}

	public void addMenuSeparator() {
		addMenuSeparator(currentMenuName);
		
	}

	/*
	public void addWatchClipboardButton(String toolBarName, JFrame jFrame, Consumer<Boolean> handleWatchClipboardChanged) {
		addToggleButton(toolBarName,  "Watch Clipboard", IC.CLIPBOARD_DEL, IC.CLIPBOARD_UP, x->switchTopMost(jFrame));
	}
	*/

	public void addEditIniFileButton(String ... fileNames) {

		if (iniFile==null)
			throw (new RuntimeException("Ini File not set."));
		if (fileNames==null || fileNames.length==0)
			addToolbarButton("Edit Inifile", IC.EDITDOC, x->OpenFile(iniFile.getFileName()));
		else
			addToolbarButton("Edit Inifile", IC.EDITDOC, x->OpenFiles(fileNames));

	}

	private void OpenFiles(String[] fileNames) {
		for (String fileName : fileNames) {
			OpenFile(fileName);
		}
		OpenFile(iniFile.getFileName());
	}

	private void OpenFile(String fileName) {
		FileUtil.open(fileName);
	}

	public Image getImage(IC ic) {
		return ic.getCachedImage();
	}

	public Image getImage(IC ic, int size) {
		return ic.getImage(size);
	}

	public void addMouseDoubleClickAction(JComponent list, Runnable action) {
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					action.run();
				}
			}
		});
	}

	public void addKeyPressAction(JComponent list, String key, Runnable action) {
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

	public void addCloseAction(Runnable action) {
		closeActions.add(action);
	}

	public void addOnEnterOrReturnPressed(JComponent component, Runnable runnable) {
		component.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					System.out.println("Return oder Enter wurde gedrückt!");
					runnable.run();
				}
			}
		});
	}


	static class ReturnEnterKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				System.out.println("Return oder Enter wurde gedrückt!");
			}
		}
	}
}