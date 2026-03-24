package com.eb.woerterbuch.gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EmptyBorder;

public class VokabelTrainerView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private VokabelTrainerCtrl ctrl;
	private JList<String> lstVokabeln;
	private JList<String> lstBedeutungen;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					VokabelTrainerView frame = new VokabelTrainerView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public VokabelTrainerView() {
		initForm();
		
		lstVokabeln.setListData(new String[]{"Henning","Ekkart"});
		lstBedeutungen.setListData(new String[]{"Henning","Ekkart"});
		
		
		
		
	}

	private void initForm() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JToolBar toolBar = new JToolBar();
		contentPane.add(toolBar, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{0, 0, 0};
		gbl_panel.rowHeights = new int[]{0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		lstVokabeln = new JList();
		GridBagConstraints gbc_lstVokabeln = new GridBagConstraints();
		gbc_lstVokabeln.insets = new Insets(0, 0, 5, 0);
		gbc_lstVokabeln.fill = GridBagConstraints.BOTH;
		gbc_lstVokabeln.gridx = 0;
		gbc_lstVokabeln.gridy = 0;
		panel.add(lstVokabeln, gbc_lstVokabeln);
		
		lstBedeutungen = new JList();
		GridBagConstraints gbc_lstBedeutungen = new GridBagConstraints();
		gbc_lstBedeutungen.fill = GridBagConstraints.BOTH;
		gbc_lstBedeutungen.gridx = 1;
		gbc_lstBedeutungen.gridy = 0;
		panel.add(lstBedeutungen, gbc_lstBedeutungen);
	}

	public VokabelTrainerCtrl getCtrl() {
		return ctrl;
	}

	public void setCtrl(VokabelTrainerCtrl ctrl) {
		this.ctrl = ctrl;
	}

}
