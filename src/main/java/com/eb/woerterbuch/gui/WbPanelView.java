package com.eb.woerterbuch.gui;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.event.KeyListener;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import com.eb.base.gui.GuiDecorator;
import com.eb.system.TextChangedHandlerUtil;
import com.eb.woerterbuch.gobj.Vokabel;
import com.eb.woerterbuch.gobj.formatter.FlatListFormatter;
import com.eb.woerterbuch.gobj.formatter.IVokabelListFormatter;
import com.eb.woerterbuch.gobj.formatter.ItemWithListAdapter;


@SuppressWarnings("nls")
public class WbPanelView extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 10001L;
	private JTextField edWort;	
	private JScrollPane jScrollPaneVokabeltext;
	private JTextArea jTextAreaVokabeltext;
	private JToolBar toolbarMain;
	private JToolBar toolbarVokabeln;
	private JTextArea edText2;
	
	private JMenuBar menuBar;
	private GuiDecorator decorator = new GuiDecorator();
	
	ItemWithListAdapter<Vokabel> adapter = new ItemWithListAdapter<Vokabel>(v->v.getWort(), v->v.getBedeutungen());
	IVokabelListFormatter<Vokabel> formatter;
	
	private JToolBar toolBarWord;
	private JTree tree;
	private HashSet<Vokabel> vokabelnSet;
	private Consumer<TreeNode> nodeSelectedListener;


	/**
	 * Create the panel.
	 * @param menuBar2 
	 */
	public WbPanelView(JMenuBar newMenuBar) {		
		initializePanal();

		JScrollBar vertical = jScrollPaneVokabeltext.getVerticalScrollBar();

		InputMap im = vertical.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		im.put(KeyStroke.getKeyStroke("DOWN"), "unitScrollDown");
		im.put(KeyStroke.getKeyStroke("UP"), "unitScrollUp");
		
		setMenuBar(newMenuBar);
		initializeDecorator();
		
		SwingUtilities.invokeLater( ()->edWort.requestFocus() );		
		formatter = new FlatListFormatter<>(10.0,  adapter);			
		getTree().addTreeSelectionListener(new MyTreeSelectionListener());
		
	}
	
	class MyTreeSelectionListener implements TreeSelectionListener
	{

		@Override
		public void valueChanged(TreeSelectionEvent e) {
			if (getTree().getSelectionPath()==null)
				return;
			
			Object lastPathComponent = getTree().getSelectionPath().getLastPathComponent();
			if (lastPathComponent==null)
				return;
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) lastPathComponent;
			if (getNodeSelectedListener()!=null)
				getNodeSelectedListener().accept(node);
		}
		
	}
	public String getWort()
	{
		return edWort.getText();
	}

	public void showVokabeln(List<Vokabel> vokabelListe) {							
		showVokabelModel(vokabelListe);		
	}

	private void showVokabelModel(List<Vokabel> vokabelListe) {
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(getWort());
											
		appendVokabelToNode(vokabelListe, root);
		
		DefaultTreeModel model = new DefaultTreeModel(root);
		getTree().setRowHeight(-1);
		getTree().setModel(model);
		getTree().setRootVisible(false);
		getTree().setAutoscrolls(true);
		expandAll();
	}


	void expandAll() {
		for (int i=0; i < getTree().getRowCount(); i++)
			getTree().expandRow(i);
		
	}
	
	void expandAll(TreeNode node) {
		if (node.isLeaf())
			return;
		
		int n = getLine((TreeNode) getTree().getModel().getRoot(), node)-1;		
		getTree().expandRow(n);
		
		int m = getChildCount(node);
		
		for (int i=0; i < m; i++)
			getTree().expandRow(++n);
				
		
	}
	
	private int getChildCount(TreeNode node) {
		if (node.isLeaf())
			return 0;
		int res = 0;
		Enumeration<? extends TreeNode> children = node.children();
		while(children.hasMoreElements())
		{
			TreeNode child = children.nextElement();
			res = res + 1 + getChildCount(child);					
		}
		return res;
	}

	class Temp
	{
		int totalChildCount;
		int result;
	}
	


	private int getLine(TreeNode root, TreeNode node) {
		Temp temp = new Temp();
		temp.totalChildCount=-1;
		temp.result=-1;
		
		getLine(root, node, temp);
		return temp.result;		
	}


	private void getLine(TreeNode root, TreeNode node, Temp temp) {
		temp.totalChildCount++;
		if (root.equals(node))
		{
			temp.result = temp.totalChildCount;
			return;
		}
		Enumeration <? extends TreeNode>children = root.children();
		while (children.hasMoreElements())
		{
			TreeNode n = children.nextElement();
			getLine(n,node,temp);
			if (temp.result>=0)
				return;
		}
	}


	public void appendVokabelToNode(List<Vokabel> vokabelListe, DefaultMutableTreeNode theNode) {
		for (Vokabel vokabel : vokabelListe) {
			
			if (getVokabelnSet().contains(vokabel))
				continue;
			
			getVokabelnSet().add(vokabel);
			
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(vokabel.getWort());
			node.setUserObject(vokabel);
			theNode.add(node);
			node.getPath();
			StringBuilder strb = new StringBuilder();
			strb.append("<html>");
			int i=0;
			for (String bed : vokabel.getBedeutungen()) {
				node.add(new DefaultMutableTreeNode(bed));
				if (i++>0)
					strb.append(", ");
				strb.append(bed);
			}
			strb.append("</html>");

			// root.add(new DefaultMutableTreeNode(strb.toString()));
			
		}
		
		expandAll(theNode);
		//getTree().updateUI();		
		
	}

			
	public void setWort(String str) {
		// TODO eb 5.5 2017 Auto-generated method stub
		edWort.setText(str);
	}
	
	public void addWortChangedHandler(Runnable runnable) {
		TextChangedHandlerUtil.addTextChangedHandler(edWort, runnable);	
	}
	
	public void addTextChangedHandler(Runnable runnable)
	{
		TextChangedHandlerUtil.addTextChangedHandler(jTextAreaVokabeltext, runnable);		
	}

	
		
	@Override
	public void addKeyListener(KeyListener listener)
	{		
		edWort.addKeyListener(listener);
		getToolbarMain().addKeyListener(listener);
	}


	private void initializeDecorator() {
		decorator = new GuiDecorator();
		decorator.addContainer("Main", getToolbarMain());
		decorator.addContainer("Vokabeln", getToolbarVokabeln());
		decorator.addContainer("Word", getToolBarWord());
		
		decorator.setMenuBar(getMenuBar());
	}
	
	private void initializePanal() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {450};
		gridBagLayout.rowHeights = new int[] {34, 20, 143, 78};
		gridBagLayout.columnWeights = new double[]{0.0};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0};
		setLayout(gridBagLayout);
		
		setToolbarMain(new JToolBar());
		getToolbarMain().setRollover(true);
		GridBagConstraints gbc_toolBar = new GridBagConstraints();
		gbc_toolBar.fill = GridBagConstraints.VERTICAL;
		gbc_toolBar.anchor = GridBagConstraints.NORTHWEST;
		gbc_toolBar.insets = new Insets(0, 0, 0, 6);
		gbc_toolBar.gridx = 0;
		gbc_toolBar.gridy = 0;
		add(getToolbarMain(), gbc_toolBar);
		
		Panel wordPanel = new Panel();
		wordPanel.setLayout(new BorderLayout(0, 0));
		GridBagConstraints gbc_edWort = new GridBagConstraints();
		gbc_edWort.anchor = GridBagConstraints.NORTH;
		gbc_edWort.fill = GridBagConstraints.HORIZONTAL;
		gbc_edWort.gridx = 0;
		gbc_edWort.gridy = 1;
		add(wordPanel, gbc_edWort);
		
		setToolBarWord(new JToolBar());
		wordPanel.add(getToolBarWord(), BorderLayout.CENTER);
		
		edWort = new JTextField();
		toolBarWord.add(edWort);
		edWort.setColumns(10);
		
		jTextAreaVokabeltext = new JTextArea();
		jTextAreaVokabeltext.setFont(new Font("Courier New", Font.PLAIN, 13));
		GridBagConstraints gbc_edText = new GridBagConstraints();
		gbc_edText.anchor = GridBagConstraints.SOUTHWEST;
		gbc_edText.insets = new Insets(0, 0, 5, 5);
		gbc_edText.gridx = 0;
		gbc_edText.gridy = 2;
		
		
		GridBagConstraints gbc_tree = new GridBagConstraints();
		gbc_tree.fill = GridBagConstraints.BOTH;
		gbc_tree.weighty = 0.1;
		gbc_tree.weightx = 0.3;
		gbc_tree.gridx = 0;
		gbc_tree.gridy = 0;
		
		
		setTree(new JTree());
		getTree().setModel(new DefaultTreeModel(new DefaultMutableTreeNode("huhu")));
		getTree().setRootVisible(false);
		
		
		
		
		//jScrollPaneVokabeltext = new JScrollPane(jTextAreaVokabeltext);
		jScrollPaneVokabeltext = new JScrollPane(getTree());
		GridBagConstraints gbc_panelText = new GridBagConstraints();
		gbc_panelText.weighty = 10.0;
		gbc_panelText.weightx = 2.0;
		gbc_panelText.fill = GridBagConstraints	.BOTH;
		gbc_panelText.insets = new Insets(0, 0, 5, 5);
		gbc_panelText.gridx = 0;
		gbc_panelText.gridy = 2;		
		add(jScrollPaneVokabeltext, gbc_panelText);
		
		
		Panel panelSpeichern = new Panel();
		
		GridBagConstraints gbc_Panelspeichern = new GridBagConstraints();
		gbc_Panelspeichern.fill = GridBagConstraints.HORIZONTAL;
		gbc_Panelspeichern.weighty = 0.1;
		gbc_Panelspeichern.weightx = 1.0;
		gbc_Panelspeichern.anchor = GridBagConstraints.SOUTH;
		gbc_Panelspeichern.gridx = 0;
		gbc_Panelspeichern.gridy = 3;	
		add(panelSpeichern, gbc_Panelspeichern);
		
		
		GridBagLayout gbl_panelSpeichern = new GridBagLayout();
		gbl_panelSpeichern.columnWidths = new int[] {420, 30};
		gbl_panelSpeichern.rowHeights = new int[] {70, 0};
		gbl_panelSpeichern.columnWeights = new double[]{0.0, 0.0};
		gbl_panelSpeichern.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panelSpeichern.setLayout(gbl_panelSpeichern);
		
		
		ScrollPane panelText2 = new ScrollPane();
		GridBagConstraints gbc_panelText2 = new GridBagConstraints();
		gbc_panelText2.fill = GridBagConstraints.BOTH;
		gbc_panelText2.weighty = 0.1;
		gbc_panelText2.weightx = 0.3;
		gbc_panelText2.gridx = 0;
		gbc_panelText2.gridy = 0;
						
		panelSpeichern .add(panelText2, gbc_panelText2);
		
		edText2 = new JTextArea();
		edText2.setRows(5);
		edText2.setTabSize(4);
		
		panelText2.add(edText2);
		
		setToolbarVokabeln(new JToolBar());
		getToolbarVokabeln().setOrientation(SwingConstants.VERTICAL);
		GridBagConstraints gbc_toolBar_1 = new GridBagConstraints();
		gbc_toolBar_1.anchor = GridBagConstraints.NORTH	;
		gbc_toolBar_1.gridx = 1;
		gbc_toolBar_1.gridy = 0;
		panelSpeichern.add(getToolbarVokabeln(), gbc_toolBar_1);
	}
	
	public String getDefinition() {		
		return edText2.getText();
	}
	
	public void setDefinition(String string) {
		edText2.setText(string);		
	}
	
	public String getText() {
		return jTextAreaVokabeltext.getText();
	}

	public JMenuBar getMenuBar() {
		return menuBar;
	}

	public void setMenuBar(JMenuBar menuBar) {
		this.menuBar = menuBar;
	}

	public JToolBar getToolbarMain() {
		return toolbarMain;
	}

	public void setToolbarMain(JToolBar toolbarMain) {
		this.toolbarMain = toolbarMain;
	}

	public JToolBar getToolbarVokabeln() {
		return toolbarVokabeln;
	}

	public void setToolbarVokabeln(JToolBar toolbarVokabeln) {
		this.toolbarVokabeln = toolbarVokabeln;
	}

	public GuiDecorator getDecorator() {
		return decorator;
	}

	public void setDecorator(GuiDecorator decorator) {
		this.decorator = decorator;
	}


	public JToolBar getToolBarWord() {
		return toolBarWord;
	}


	public void setToolBarWord(JToolBar toolBarWord) {
		this.toolBarWord = toolBarWord;
	}


	public JTree getTree() {
		return tree;
	}


	public void setTree(JTree tree) {
		this.tree = tree;
	}


	public HashSet<Vokabel> getVokabelnSet() {
		return vokabelnSet;
	}


	public void setVokabelnSet(HashSet<Vokabel> vokabelnSet) {
		this.vokabelnSet = vokabelnSet;
	}


	public void addNodeSelectedListener(Consumer<TreeNode> handleStringSelected) {
		setNodeSelectedListener(handleStringSelected);
		
	}

	public Consumer<TreeNode> getNodeSelectedListener() {
		return nodeSelectedListener;
	}
	
	
	public void setNodeSelectedListener(Consumer<TreeNode> handleStringSelected) {
		this.nodeSelectedListener = handleStringSelected;
	}


	
}
