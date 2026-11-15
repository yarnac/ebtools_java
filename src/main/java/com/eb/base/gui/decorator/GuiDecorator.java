package com.eb.base.gui.decorator;

import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GuiDecorator {
	private final IniFile iniFile;
	@Setter
    @Getter
    private JFrame frame;
	private final GuiDecoratorMenuBars menuBars;
	private final GuiDecoratorToolBars toolBars;

	public GuiDecoratorMenuBars menuBars() { return menuBars; }
	public GuiDecoratorToolBars toolBars() { return toolBars; }


	private final List<Runnable> closeActions = new ArrayList<>();

	public GuiDecorator() {
		toolBars = new GuiDecoratorToolBars();
		menuBars = new GuiDecoratorMenuBars(null);
		iniFile = null;
	}

	public GuiDecorator(JFrame frame) {
		toolBars = new GuiDecoratorToolBars(frame);
		menuBars = new GuiDecoratorMenuBars(frame);
		iniFile = null;
	}

	public GuiDecorator(JPanel panel) {
		toolBars = new GuiDecoratorToolBars(panel);
		menuBars = new GuiDecoratorMenuBars(null);
		iniFile = null;
	}

	public GuiDecorator(JFrame frame, IniFile newIniFile, String einstellungen) {
		this.frame = frame;
		iniFile = newIniFile;

		toolBars = new GuiDecoratorToolBars(frame);
		menuBars = new GuiDecoratorMenuBars(frame);

		GuiPersister.registerAndLoadStatus(frame, newIniFile,einstellungen);
		WindowAdapter adapter = new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for(Runnable action : closeActions) {
					action.run();
				}
				if (iniFile != null) {
					iniFile.Write();
				}
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
			toolBars().addToolbarButton("Edit Inifile", IC.EDITDOC, x-> openFile(iniFile.getFileName()));
		else
			toolBars().addToolbarButton("Edit Inifile", IC.EDITDOC, x-> openFiles(fileNames));

	}

	private void openFiles(String[] fileNames) {
		for (String fileName : fileNames) {
			openFile(fileName);
		}
		if (Arrays.stream(fileNames).noneMatch(fileName -> fileName.equals(iniFile.getFileName()))) {
			openFile(iniFile.getFileName());
		}
	}

	private void openFile(String fileName) {
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