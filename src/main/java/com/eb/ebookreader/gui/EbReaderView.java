package com.eb.ebookreader.gui;

import com.eb.base.gui.GuiUtil;
import com.eb.ebookreader.app.EBReaderViewController;
import com.eb.ebookreader.tobj.IConsumePoint;
import com.eb.ebookreader.tobj.IShowUebersetzung;
import com.eb.ebookreader.tobj.StringConsumer;
import com.eb.ebookreader.tobj.StringUtil;
import com.eb.woerterbuch.gobj.WoerterbuchSession;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class EbReaderView {
	
	private EbReaderViewFrame frame;		
	private List<BookCtrl> bookControllers = new ArrayList<>();
	
	private IShowUebersetzung iShowUebersetzung;
	private StringConsumer handleUebersetungsRequest;
	
	private IConsumePoint handleCommonPointSaveRequest;
	private List<Point> commonPoints;
	
	private EBReaderViewController controller;
	

	
	public EbReaderView (EBReaderViewController ebReaderViewController, int n)
	{
		createNewFrame(n);				
		controller = ebReaderViewController;			
	}

	private void createNewFrame(int n) {
		setFrame(new EbReaderViewFrame(n));
	}
	
	public void createColumns(int columns)
	{				
		getFrame().createViewColumns(columns);
	}
			
	public void nextPage(int i) {
		for (BookCtrl bookCtrl : getBookControllers()) {
			nextPage(bookCtrl.getView().getPane(), i);
			
		}
	}
	
	private void nextPage(JScrollPane pane, int i) {
		JScrollBar bar = pane.getVerticalScrollBar();
		int amount = bar.getVisibleAmount();
		int newValue = bar.getValue() + i * (amount - 100);
		if (newValue<0)
			newValue = 0;
		
		bar.setValue(newValue);
		
	}

	
	public void setScrollUnitIncrement(int i) {
		for (BookCtrl bookCtrl : getBookControllers()) {
			bookCtrl.getView().getPane().getVerticalScrollBar().setUnitIncrement(i);
		}
	}

	public void SetKeyListeners() {
		for (BookCtrl bookCtrl : bookControllers) {
			BookView view = bookCtrl.getView();
			view.getEdit().addKeyListener(getEditKeyListener(bookCtrl));
		}
	}	
	
	private KeyListener getEditKeyListener(BookCtrl bookCtrl) {
		return new KeyListener() {
								
			@Override
			public void keyPressed(KeyEvent e) {
				
				if (handlePageUpDpwnRequested(e))				
					return;

				
				if (e.getKeyCode()==KeyEvent.VK_F1)
				{
					controller.ShowHelp();
					return;
				}
				
				if (e.getKeyCode()==KeyEvent.VK_F4)
				{
					 int position = bookCtrl.getView().getEdit().getCaretPosition();
					 String wordAtCaret = GuiUtil.getWordAtCaret(bookCtrl.getView().getEdit());
					 
					 GuiUtil.setFirstLineToCaretPosition(bookCtrl.getView().getEdit(), bookCtrl.getView().getPane(), position);
					return;
				}
					
				
				
				if (handleCommonPointRequest(e))
					return;
								
				if (handleUebersetzungsKeyPressed(bookCtrl, e))
					return;
			}
			
			private boolean handleUebersetzungsKeyPressed(BookCtrl bc, KeyEvent e) {
				
				if (e.getKeyCode()!=KeyEvent.VK_F2  && e.getKeyCode()!=KeyEvent.VK_F3)
					return false;
				
				String word = GuiUtil.getWordAtCaret((JTextPane) e.getComponent());						
				word = StringUtil.substringBeforeFirst(word, "-", false);
				
				final String w = word;
				
				WoerterbuchSession session = bookCtrl.getWbSession();
				
				if (e.getKeyCode()==KeyEvent.VK_F2)
				{					
					iShowUebersetzung.showUebersetzung(session, w);		
					
				}
				
				if (e.getKeyCode()==KeyEvent.VK_F3)
				{
					if (getHandleUebersetungsRequest()!=null)
					{
						String t = w + "::" + session.getSprache();
						getHandleUebersetungsRequest().accept(t);
					}
				}
				return true;
			}

			private boolean handleCommonPointRequest(KeyEvent e) {
															
				int direction = 0;
				if (e.getKeyCode()==KeyEvent.VK_F7)				
					direction = -1;
				if (e.getKeyCode()==KeyEvent.VK_F8)				
					direction = 1;
				
				if (direction==0)
					return false;
				
				
				int position = bookCtrl.getView().getEdit().getCaretPosition();
				int newPositionIndex = bookCtrl.getNextPositionIndex(position);
				if (direction==-1)
					newPositionIndex -= 2;
					
				if (newPositionIndex>=0)
				{
						for (BookCtrl bookCtrl : bookControllers) {
							setPositionIndex(bookCtrl, newPositionIndex);						
						}
				}																		
				return true;							
			}

			private void setPositionIndex(BookCtrl bookCtrl, int newPositionIndex) {
				GuiUtil.setFirstLineToCaretPosition(bookCtrl.getView().getEdit(), bookCtrl.getView().getPane(), bookCtrl.getPosition(newPositionIndex));
			}

			
			private boolean handlePageUpDpwnRequested(KeyEvent e) {
				
				if (!(e.getKeyCode()==KeyEvent.VK_PAGE_DOWN || e.getKeyCode()==KeyEvent.VK_PAGE_UP))
					return false;
				
				e.consume();
						
				if (e.getKeyCode()==33)					
					nextPage( -1);											
				else if (e.getKeyCode()==34)					
					nextPage(+1);															
						
				return true;
			}
			
			@Override
			public void keyTyped(KeyEvent e) {}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			

			/**
			 * Setzt die beiden Textpanes auf den naechsten oder vorherigen gemeinsamen Punkt. Ausgangspunkt ist die caretPosition
			 * von <code>actEdit</code>
			 * @param edRight
			 * @param edLeft
			 * @param actEdit
			 * @param forward
			 */
			private void moveToNextCommonPoint(JTextPane edRight, JTextPane edLeft, JTextPane actEdit, boolean forward) {
				
				int caretPosition = getVisibleCaretPosition(actEdit);							
				int sign = forward ? 1 : -1;
				Point newPoint = null;
								
				if (actEdit==edLeft)
				{
					
					for (Point point : commonPoints) {
						if (point.x * sign > caretPosition*sign)
						{
							if (newPoint==null)
								newPoint = point;
							else if (point.x * sign < newPoint.x * sign)
								newPoint = point;
						}
					}						
				}
				else{							
					for (Point point : commonPoints) {
						if (point.y * sign > caretPosition*sign)
						{
							if (newPoint==null)
								newPoint = point;
							else if (point.y * sign < newPoint.y * sign)
								newPoint = point;
						}
					}						
				}
				
				if (newPoint==null && !forward)
					newPoint = new Point(0,0);
				
				if (newPoint!=null)
				{
					setVisibleCaretPoint(newPoint);
				}
			}
			
			private void nextLine(JScrollPane pane, int i) {
				int y = pane.getVerticalScrollBar().getUnitIncrement() * i + pane.getVerticalScrollBar().getValue();
				pane.getVerticalScrollBar().setValue(y);
			}
		};
	}
		
	public IShowUebersetzung getIShowUebersetzung() {
		return iShowUebersetzung;
	}

	public void setIShowUebersetzung(IShowUebersetzung iShowUebersetzung) {
		this.iShowUebersetzung = iShowUebersetzung;
	}
	
	public StringConsumer getHandleUebersetungsRequest() {
		return handleUebersetungsRequest;
	}

	public void setHandleUebersetungsRequest(StringConsumer uebersetzungsRequestHandler) {
		this.handleUebersetungsRequest = uebersetzungsRequestHandler;
	}
	
	public IConsumePoint getHandleCommonPointSaveRequest() {
		return handleCommonPointSaveRequest;
	}

	public void setHandleCommonPointSaveRequest(IConsumePoint handleCommonPoint) {
		this.handleCommonPointSaveRequest = handleCommonPoint;
	}

	public void setCommonPoints(List<Point> newCommonPoints) {
		commonPoints = newCommonPoints;
		
	}

	public void resetText() {
		Point visibleCaretPoint = getVisibleCaretPoint();
		for (BookCtrl bookCtrl : getBookControllers()) {
			bookCtrl.resetText();
		}		
		EventQueue.invokeLater(()->setVisibleCaretPoint(visibleCaretPoint));		
	}
	
	private void setVisibleCaretPoint(Point newPoint) {
		/*
		setFirstLineToCaretPosition(getFrame().getEdLeft(), newPoint.x);;
		setFirstLineToCaretPosition(getFrame().getEdRight(), newPoint.y);;
		*/		
	}

	private Point getVisibleCaretPoint() {
		/*
		int x = getVisibleCaretPosition(getFrame().getEdLeft());
		int y = getVisibleCaretPosition(getFrame().getEdRight());*/			
		return new Point(0,0);
		
	}
	
	private int getVisibleCaretPosition(JTextPane jTextPane) {
		
		return GuiUtil.getVisibleCarePosition(jTextPane);
	}

	private void setFirstLineToCaretPosition(JTextPane actEdit, int caretPosition) {
	
		//JScrollPane actFrame = (actEdit == getFrame().getEdLeft() ? getFrame().getPaneLeft() : getFrame().getPaneRight());
		//GuiUtil.setFirstLineToCaretPosition(actEdit, actFrame, caretPosition);
	}

	public EbReaderViewFrame getFrame() {
		return frame;
	}

	public void setFrame(EbReaderViewFrame frame) {
		this.frame = frame;
	}

	public List<BookCtrl> getBookControllers() {
		return bookControllers;
	}

	public void setBookControllers(List<BookCtrl> bookControllers) {
		this.bookControllers = bookControllers;
	}


	


}
