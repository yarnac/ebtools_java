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


public class GuiDecorator implements GuiDecoratorToolBarMethods, GuiDecoratorMenuBarMethods {
	private final IniFile iniFile;
	@Setter
    @Getter
    private JFrame frame;

	@Getter
	public GuiDecoratorMenuBars guiDecoratorMenuBars = new GuiDecoratorMenuBars(getFrame());

	@Getter
	public final GuiDecoratorToolBars toolBarDecorator;

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