package com.eb.woerterbuch.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.eb.woerterbuch.gobj.WbEinstellungen;

public class WbGoogleEinstDlg extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private WbGoogleEinstPanel editWoerterbuchPanel;
	private WbEinstellungen einstellungen;
	private ActionListener onSavedListener;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			WbGoogleEinstDlg dialog = new WbGoogleEinstDlg();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public WbGoogleEinstDlg() {
		setBounds(100, 100, 450, 252);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
				okButton.addActionListener(x->okButtonClicked());
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(x->cancelButtonClicked());
				buttonPane.add(cancelButton);
				
			
			}
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
			editWoerterbuchPanel = new WbGoogleEinstPanel();			
			contentPanel.add(editWoerterbuchPanel);
		}
		
		initializeView();
	}
	
	private void cancelButtonClicked() {
		dispose();
	}

	private void okButtonClicked() {
		transferViewToModel(getEinstellungen());
		if (onSavedListener!=null)
			onSavedListener.actionPerformed(new ActionEvent(this, 1, "Haeh?"));
		dispose();		
	}

	private void initializeView() {
		registerEvents();
	}

	private void registerEvents() {
		
	}

	private void transferModelToView(WbEinstellungen data)
	{
		editWoerterbuchPanel.setGoogleWeb(data.getGoogleWeb());
		editWoerterbuchPanel.setGoogleBilder(data.getGoogleBilder());
		editWoerterbuchPanel.setGoogleSprache(data.getGoogleSprache());
		editWoerterbuchPanel.setVerbix(data.getVerbix());
		
		
	}
	
	private void transferViewToModel(WbEinstellungen data)
	{
		data.setGoogleWeb(editWoerterbuchPanel.getGoogleWeb());
		data.setGoogleBilder(editWoerterbuchPanel.getGoogleBilder());
		data.setGoogleSprache(editWoerterbuchPanel.getGoogleSprache());
		data.setVerbix(editWoerterbuchPanel.getVerbix());
		
	}

	public WbEinstellungen getEinstellungen() {
		return einstellungen;
	}

	public void setEinstellungen(WbEinstellungen einstellungen) {
		transferModelToView(einstellungen);
		this.einstellungen = einstellungen;
	}

	public void addOnSavedListener(ActionListener actionListener) {
		onSavedListener = actionListener;
	}

}
