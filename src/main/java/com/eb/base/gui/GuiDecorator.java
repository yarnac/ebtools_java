package com.eb.base.gui;

import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;


public class GuiDecorator implements GuiDecoratorToolBarsMethods, GuiDecoratorMenuBarsMethods {
	private final IniFile iniFile;
	@Setter
    @Getter
    private JFrame frame;

	@Getter
	public GuiDecoratorMenuBars guiDecoratorMenuBars;

	@Getter
	public final GuiDecoratorToolBars toolBarDecorator;

	private final List<Runnable> closeActions = new ArrayList<>();

	public GuiDecorator() {
		toolBarDecorator = new GuiDecoratorToolBars();
		guiDecoratorMenuBars = new GuiDecoratorMenuBars(null);
		iniFile = null;
	}

	public GuiDecorator(JFrame frame) {
		toolBarDecorator = new GuiDecoratorToolBars(frame);
		guiDecoratorMenuBars = new GuiDecoratorMenuBars(frame);
		iniFile = null;
	}

	public GuiDecorator(JPanel panel) {
		toolBarDecorator = new GuiDecoratorToolBars(panel);
		guiDecoratorMenuBars = new GuiDecoratorMenuBars(null);
		iniFile = null;
	}

	public GuiDecorator(JFrame frame, IniFile newIniFile, String einstellungen) {
		this.frame = frame;
		iniFile = newIniFile;

		toolBarDecorator = new GuiDecoratorToolBars(frame);
		guiDecoratorMenuBars = new GuiDecoratorMenuBars(frame);

		GuiPersister.registerAndLoadStatus(frame, newIniFile,einstellungen);
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