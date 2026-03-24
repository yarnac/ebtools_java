package com.eb.woerterbuch.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class WbGoogleEinstPanel
	extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JLabel lblNewLabel = new JLabel("Websuche");
	private final JTextField edGoogle = new JTextField();
	private final JLabel lblBilder = new JLabel("Bilder");
	private final JLabel lblSprache = new JLabel("Sprache");
	private final JTextField edBilder = new JTextField();
	private final JTextField edSprache = new JTextField();
	private final JPanel panel = new JPanel();
	private final JTextField edVerbix = new JTextField();
	private final JLabel lblVerbix = new JLabel("Verbix");

	/**
	 * Create the panel.
	 */
	public WbGoogleEinstPanel() {
		edGoogle.setColumns(10);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);
		
		GridBagConstraints gbc_edGoogle = new GridBagConstraints();
		gbc_edGoogle.insets = new Insets(0, 0, 5, 0);
		gbc_edGoogle.fill = GridBagConstraints.HORIZONTAL;
		gbc_edGoogle.gridx = 1;
		gbc_edGoogle.gridy = 0;
		add(edGoogle, gbc_edGoogle);
		
		GridBagConstraints gbc_lblBilder = new GridBagConstraints();
		gbc_lblBilder.anchor = GridBagConstraints.EAST;
		gbc_lblBilder.insets = new Insets(0, 0, 5, 5);
		gbc_lblBilder.gridx = 0;
		gbc_lblBilder.gridy = 1;
		add(lblBilder, gbc_lblBilder);
		
		GridBagConstraints gbc_edBilder = new GridBagConstraints();
		gbc_edBilder.insets = new Insets(0, 0, 5, 0);
		gbc_edBilder.fill = GridBagConstraints.HORIZONTAL;
		gbc_edBilder.gridx = 1;
		gbc_edBilder.gridy = 1;
		edBilder.setColumns(10);
		add(edBilder, gbc_edBilder);
		
		GridBagConstraints gbc_lblSprache = new GridBagConstraints();
		gbc_lblSprache.anchor = GridBagConstraints.EAST;
		gbc_lblSprache.insets = new Insets(0, 0, 5, 5);
		gbc_lblSprache.gridx = 0;
		gbc_lblSprache.gridy = 2;
		add(lblSprache, gbc_lblSprache);
		
		GridBagConstraints gbc_edSprache = new GridBagConstraints();
		gbc_edSprache.insets = new Insets(0, 0, 5, 0);
		gbc_edSprache.fill = GridBagConstraints.HORIZONTAL;
		gbc_edSprache.gridx = 1;
		gbc_edSprache.gridy = 2;
		edSprache.setColumns(10);
		add(edSprache, gbc_edSprache);
		
		GridBagConstraints gbc_lblVerbix = new GridBagConstraints();
		gbc_lblVerbix.insets = new Insets(0, 0, 5, 5);
		gbc_lblVerbix.anchor = GridBagConstraints.EAST;
		gbc_lblVerbix.gridx = 0;
		gbc_lblVerbix.gridy = 3;
		add(lblVerbix, gbc_lblVerbix);
		
		GridBagConstraints gbc_edVerbix = new GridBagConstraints();
		gbc_edVerbix.insets = new Insets(0, 0, 5, 0);
		gbc_edVerbix.fill = GridBagConstraints.HORIZONTAL;
		gbc_edVerbix.gridx = 1;
		gbc_edVerbix.gridy = 3;
		edVerbix.setColumns(10);
		add(edVerbix, gbc_edVerbix);
		
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 4;
		add(panel, gbc_panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

	}

	public void setGoogleWeb(String googleWeb) {
		edGoogle.setText(googleWeb);		
	}
	public void setGoogleSprache(String googleWeb) {
		edSprache.setText(googleWeb);		
	}
	public void setGoogleBilder(String googleWeb) {
		edBilder.setText(googleWeb);		
	}
	public void setVerbix(String googleWeb) {
		edVerbix.setText(googleWeb);		
	}
	
	public String getGoogleWeb() {
		return edGoogle.getText();		
	}
	
	public String getGoogleSprache() {
		return edSprache.getText();		
	}
	public String getGoogleBilder() {
		return edBilder.getText();		
	}
	public String getVerbix() {
		return edVerbix.getText();		
	}
}
