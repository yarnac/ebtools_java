package com.eb.doubletten;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.io.FileUtil;

public class DoubleFinderFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JPanel panelLeft;
	

	private JPanel panelCenter;
	private JPanel panelRight;
	private JPanel panelSouth;
	private JToolBar mainToolBar;
	
	private DoubleFinderPanel doubleFinderPanel;
	private JList lstOtherDoubletten;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DoubleFinderFrame frame = new DoubleFinderFrame();
					frame.initializeView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	protected void initializeView() {
		DoubleFinderPanelCtrl ctrl = new DoubleFinderPanelCtrl(this, getDoubleFinderPanel());		
		ctrl.setLstOtherDoubles(getLstOtherDoubletten());
		
		List<String> strings = FileUtil.getDirectories("~/Data/Medien/Bilder");
		strings.add("~/Data/Medien/Bilder/");

		strings.clear();
		strings.add("~/Data/Medien/Porno/dwhelper");
		strings.add("d:\\Medien\\dwhelper2\\dwhelper");
		ctrl.setDirectories(strings.toArray(new String[0]));
				
		GuiDecorator decorator = new GuiDecorator();
		decorator.addContainer("main", getMainToolBar());
		decorator.addTopMostButton("main", this);					
	}
	
	
		/**
	 * Create the frame.
	 */
	public DoubleFinderFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		mainToolBar = new JToolBar();
		contentPane.add(mainToolBar, BorderLayout.NORTH);
		
		panelLeft = new JPanel();
		contentPane.add(panelLeft, BorderLayout.WEST);
		
		panelCenter = new JPanel();
		contentPane.add(panelCenter, BorderLayout.CENTER);
		panelCenter.setLayout(new BorderLayout(0, 0));
		
		panelRight = new JPanel();
		panelRight.setLayout(new GridBagLayout());
		contentPane.add(panelRight, BorderLayout.EAST);
		
		setLstOtherDoubletten(new JList());
		DefaultListModel defaultListModel = new DefaultListModel();
		defaultListModel.addElement("Medien");
		defaultListModel.addElement("Test");
		lstOtherDoubletten.setModel(defaultListModel);
		panelRight.add(getLstOtherDoubletten());
		
		panelSouth = new JPanel();
		contentPane.add(panelSouth, BorderLayout.SOUTH);
		panelSouth.setLayout(new BorderLayout(0, 0));
		
		setDoubleFinderPanel(new DoubleFinderPanel());
		panelCenter.add(getDoubleFinderPanel());
	}

	public JPanel getPanelLeft() {
		return panelLeft;
	}

	public void setPanelLeft(JPanel panelLeft) {
		this.panelLeft = panelLeft;
	}

	public JPanel getPanelCenter() {
		return panelCenter;
	}

	public void setPanelCenter(JPanel panelCenter) {
		this.panelCenter = panelCenter;
	}

	public JPanel getPanelRight() {
		return panelRight;
	}

	public void setPanelRight(JPanel panelRight) {
		this.panelRight = panelRight;
	}

	public JPanel getPanelSouth() {
		return panelSouth;
	}

	public void setPanelSouth(JPanel panelSouth) {
		this.panelSouth = panelSouth;
	}

	public JToolBar getMainToolBar() {
		return mainToolBar;
	}

	public void setMainToolBar(JToolBar mainToolBar) {
		this.mainToolBar = mainToolBar;
	}

	public DoubleFinderPanel getDoubleFinderPanel() {
		return doubleFinderPanel;
	}

	public void setDoubleFinderPanel(DoubleFinderPanel doubleFinderPanel) {
		this.doubleFinderPanel = doubleFinderPanel;
		//doubleFinderPanel.getToolBar().setBounds(6, 6, 408, 40);
		//doubleFinderPanel.getComboBox().setBounds(1, 42, 413, 27);
		//doubleFinderPanel.getLstDoubletten().setBounds(1, 70, 413, 186);
	}

	public JList getLstOtherDoubletten() {
		return lstOtherDoubletten;
	}

	public void setLstOtherDoubletten(JList lstOtherDoubletten) {
		this.lstOtherDoubletten = lstOtherDoubletten;
	}
}
