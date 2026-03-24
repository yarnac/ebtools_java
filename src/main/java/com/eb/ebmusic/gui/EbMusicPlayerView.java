package com.eb.ebmusic.gui;

import com.eb.ebmusic.tobj.MusicTreeNode;
import com.eb.ebmusic.gobj.EbMusicLib;
import com.eb.ebmusic.gobj.MusicFolder;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.List;
import java.util.function.Consumer;


public class EbMusicPlayerView {

	private JFrame frame;
	private JTree tree;

	/**
	 * Launch the application.
	 */
	
	 		
		
	private JTextPane edActTitle;
	private JPanel panel;
	private JToolBar toolBar;
	private JTextPane edFilter;
	
	private Consumer<MusicTreeNode> handleSelectedNusicTreeNodeChanged;
	private Consumer<String> handleFilterChanged;
	private Consumer<String> handleWindowClosed;
	
	private Consumer<EbMusicLib> handleBibliothekChanged;
	private Runnable handleTreeDoubleClicked;
	private JComboBox<EbMusicLib> comboBox;
	private List<EbMusicLib> bibliotheken;
	
	
	
	
	public void addBibliothek(EbMusicLib string) {
		comboBox.addItem(string);
	}
	
	

	/**
	 * Create the application.
	 */
	public EbMusicPlayerView() {
		//java.awt.Taskbar.getTaskbar().setIconImage(IC.MusicLibrary_HR.getImage32());
		initialize();		
		registerEvents();
		
		
		
	}
	
	private TreePath find(DefaultMutableTreeNode root, String s) {
	    @SuppressWarnings("unchecked")
		Enumeration<TreeNode> e = root.depthFirstEnumeration();
	    while (e.hasMoreElements()) {
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
	        if (node.toString().equalsIgnoreCase(s)) {
	            return new TreePath(node.getPath());
	        }
	    }
	    return null;
	}
	
	private void registerEvents() {
		registerEdFilterEvents();
		registerMouseEvents();
		registerTreeSelectionEvents();
		
		frame.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				TreePath selectionPath = tree.getSelectionPath();
				if (selectionPath!=null && getHandleWindowClosed()!=null)
				{
					getHandleWindowClosed().accept(selectionPath.toString());
				}
					
			}

			@Override
			public void windowClosed(WindowEvent e) {
				
			}

			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		comboBox.addActionListener( (e)->selectedItemChanged()) ;	
	}
	
	private void selectedItemChanged() {			
		if (comboBox.getSelectedItem()!=null && getHandleBibliothekChanged()!=null)
			getHandleBibliothekChanged().accept((EbMusicLib) comboBox.getSelectedItem());
	}

	private void registerTreeSelectionEvents() {
		getTree().addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent e) {
				if (getHandleSelectedNusicTreeNodeChanged()!=null)
					getHandleSelectedNusicTreeNodeChanged().accept((MusicTreeNode) e.getPath().getLastPathComponent());;
				
			}
		});
		
	}

	private void registerMouseEvents() {
		
			getTree().addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
							
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.isAltDown())
				{
					if (getHandleTreeDoubleClicked()!=null)
						getHandleTreeDoubleClicked().run();					
				}		
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
								
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount()==2)
				{
					if (getHandleTreeDoubleClicked()!=null)
						getHandleTreeDoubleClicked().run();					
				}				
			}					
		});		
		
	}

	private void registerEdFilterEvents() {
		edFilter.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				
				if (getHandleFilterChanged()==null)
					return;
				
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
								
			}
			
			@Override
			public void keyPressed(KeyEvent e) {			
				
			}
		});
	}

	public void showTitle(String text)
	{
		getEdActTitle().setText(text);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		setFrame(new JFrame());
		getFrame().setBounds(100, 100, 450, 300);
		getFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
		setTree(new JTree());
		
		
		JScrollPane scrollPane = new JScrollPane(tree);
		getFrame().getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		setEdActTitle(new JTextPane());
		getFrame().getContentPane().add(getEdActTitle(), BorderLayout.SOUTH);
		
		panel = new JPanel();
		getFrame().getContentPane().add(panel, BorderLayout.NORTH);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{500,0};
		gbl_panel.rowHeights = new int[]{20, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE, 0.0};
		panel.setLayout(gbl_panel);
		
		setToolBar(new JToolBar());
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.insets = new Insets(0, 0, 5, 0);
		gbc_toolBar.fill = GridBagConstraints.BOTH;
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		panel.add(getToolBar(), gbc_toolBar);
		{
			comboBox = new JComboBox<>();
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.insets = new Insets(0, 0, 5, 0);
			gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
			gbc_comboBox.gridx = 0;
			gbc_comboBox.gridy = 1;
			panel.add(comboBox, gbc_comboBox);
		}
		
		JPanel panel2 = new JPanel();
		panel2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel2.setBackground(Color.WHITE);
		GridBagConstraints gbc_Pane_2 = new GridBagConstraints();
		gbc_Pane_2.insets = new Insets(0, 0, 5, 0);
		gbc_Pane_2.fill = GridBagConstraints.BOTH;
		gbc_Pane_2.gridx = 0;
		gbc_Pane_2.gridy = 2;
		panel.add(panel2, gbc_Pane_2);
		
		/*
		GridBagLayout gbl_panel2 = new GridBagLayout();
		gbl_panel2.columnWidths = new int[]{1, 0};
		gbl_panel2.rowHeights = new int[]{16, 0};
		gbl_panel2.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gbl_panel2.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel2.setLayout(gbl_panel2);
		*/
		edFilter = new JTextPane();
		
		GridBagConstraints gbc_textPane_1 = new GridBagConstraints();
		gbc_textPane_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_textPane_1.fill = GridBagConstraints.BOTH;
		gbc_textPane_1.gridx = 0;
		gbc_textPane_1.gridy = 2;
		
		
		
		
		panel.add(edFilter, gbc_textPane_1);
	}

	public JTree getTree() {
		return tree;
	}

	public void setTree(JTree tree) {
		this.tree = tree;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	private JTextPane getEdActTitle() {
		return edActTitle;
	}

	private void setEdActTitle(JTextPane edActTitle) {
		this.edActTitle = edActTitle;
	}

	public Consumer<String> getHandleFilterChanged() {
		return handleFilterChanged;
	}

	public void setHandleFilterChanged(Consumer<String> handleFilterChanged) {
		this.handleFilterChanged = handleFilterChanged;
	}

	public Runnable getHandleTreeDoubleClicked() {
		return handleTreeDoubleClicked;
	}

	public void setHandleTreeDoubleClicked(Runnable handleTreeDoubleClicked) {
		this.handleTreeDoubleClicked = handleTreeDoubleClicked;
	}

	public Consumer<MusicTreeNode> getHandleSelectedNusicTreeNodeChanged() {
		return handleSelectedNusicTreeNodeChanged;
	}

	public void setHandleSelectedMusicTreeNodeChanged(Consumer<MusicTreeNode> handleSelectedNusicTreeNodeChanged) {
		this.handleSelectedNusicTreeNodeChanged = handleSelectedNusicTreeNodeChanged;
	}

	public JToolBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(JToolBar toolBar) {
		this.toolBar = toolBar;
	}

	public List<EbMusicLib> getBibliotheken() {
		return bibliotheken;
	}

	public void setBibliotheken(List<EbMusicLib> bibliotheken) {
		comboBox.removeAllItems();
		this.bibliotheken = bibliotheken;
		
		if (bibliotheken!=null)
			bibliotheken.stream().forEach(x -> comboBox.addItem(x));
	}

	public void selectBibliothek(EbMusicLib ebMusicLib) {
		comboBox.setSelectedItem(ebMusicLib);		
	}
	public Consumer<EbMusicLib> getHandleBibliothekChanged() {
		return handleBibliothekChanged;
	}

	public void setHandleBibliothekChanged(Consumer<EbMusicLib> handleBibliothekChanged) {
		this.handleBibliothekChanged = handleBibliothekChanged;
	}

	public String getFilterString() {
		return edFilter.getText();
	}

	public Consumer<String> getHandleWindowClosed() {
		return handleWindowClosed;
	}

	public void setHandleWindowClosed(Consumer<String> handleWindowClosed) {
		this.handleWindowClosed = handleWindowClosed;
	}

	public void findNode(String sectionValue) {
		// TODO Auto-generated method stub
		
		MusicTreeNode node = (MusicTreeNode) getTree().getModel().getRoot();
		MusicFolder folder = node.getFolder();
	}	

	
}
