package com.eb.woerterbuch.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.eb.base.EbAppContext;
import com.eb.base.crypt.Crypt;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.base.io.FileUtil;
import com.eb.system.StringUnifier;

public class ShowCryptedFileDlg extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String COREFILENAME = "Cores/core.dat";
	private JPanel contentPane;
	private String passwort;
	private JTextArea textArea;
	private JToolBar toolBar;
	private JTextField edFileName;
	private JTextField edSearch;
	private String coreFileName = COREFILENAME;

	private String[] coreFileNames = new String[]{
				"Cores/core.dat",
				"Cores/core_handy_aktuell.dat",
				"Cores/core_handy.dat",
				"Cores/core_tablet.dat",
				"Cores/core_mac.dat",
				"Cores/core_mac.dat"};

	private static String programText;

	private static String loadText(String fileName) {
        try {
            return FileUtil.readText("UTF-8",fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws IOException {




		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowCryptedFileDlg frame = new ShowCryptedFileDlg();
					frame.setVisible(true);
					frame.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void start() {
		setPasswort(programText);
	}

	/**
	 * Create the frame.
	 */
	public ShowCryptedFileDlg() {
		initFrame();

		IniFile file = IniFileProvider.createIniFile(EbAppContext.getEbToolsDatenDir("Reader/EbReader.ini"));
		file.Read();
		String fileName = file.getSectionValue("Einstellungen", "FirstBook","");
		String newFileName = EbAppContext.getEbDownloadsDatenDir(fileName);
		programText = loadText(newFileName);
		GuiDecorator decorator = new GuiDecorator();
		decorator.setFrame(this);
		decorator.addToolbarButton("mainToolbar", "Speichern", IC.SAVE_DEL, e->save());
	}

	private void initFrame() {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(200, 200, 850, 1000);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		setTextArea(new JTextArea());
		textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		//contentPane.add(getTextArea(), BorderLayout.CENTER);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// JScrollPane zum JFrame hinzufügen
		contentPane.add(scrollPane);

		toolBar = new JToolBar();
		toolBar.setName("mainToolbar");
		contentPane.add(toolBar, BorderLayout.NORTH);


		edFileName = new JTextField();
		toolBar.add(edFileName);
		edFileName.setColumns(10);

		edSearch = new JTextField();
		toolBar.add(edSearch);
		edSearch.setColumns(10);

		JComboBox<String> cbFileNames = new JComboBox<>();
		cbFileNames.setModel(new DefaultComboBoxModel<String>(coreFileNames));
		cbFileNames.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!originalText.equals(textArea.getText()))
				{
					edFileName.setText(coreFileName + " nicht gespeichert");
				}
				else
				{
					Object selectedItem = cbFileNames.getModel().getSelectedItem();
					if (selectedItem != null) {
						coreFileName = selectedItem.toString();
						transgerModelToView();
					}
				}

			}
		});

		toolBar.add(cbFileNames);
		//textField2.setColumns(10);
	}

	private void save() {
		// TODO Auto-generated method stub
		String fileName = EbAppContext.getJavaDataFilename(coreFileName);
		Crypt.saveEncryptedFile(fileName, passwort, getTextArea().getText());
		originalText = getTextArea().getText();
	}

	public String getPasswort() {
		return passwort;
	}

	public void setPasswort(String passwort) {				
		String pwd = StringUnifier.getUnifiedString(passwort);
		this.passwort = pwd;
        if (pwd.equals(passwort))
            this.toString();


		transgerModelToView();
	}

	String originalText = "";
	private void transgerModelToView() {
		try {
			edFileName.setText(coreFileName);
			String realCoreFileName = EbAppContext.getJavaDataFilename(coreFileName);
			getTextArea().setText(Crypt.loadEncryptedFile(realCoreFileName, this.passwort));
			originalText = getTextArea().getText();
			getTextArea().setCaretPosition(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			getTextArea().setText("nicht entschlüsselt");
		}
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public void setTextArea(JTextArea textArea) {
		this.textArea = textArea;
	}

}
