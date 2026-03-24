package com.eb.base.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

public class GuiUtil {
	
	public static Component findComponent(Component c, Predicate<Component> predicate) {
		List<Component> findComponents = findComponents(c, predicate, true);
		if (findComponents.isEmpty())
			return null;
		
		return findComponents.get(0);
	}
	
	public static List<Component> findComponents(Component c, Predicate<Component> predicate) {
		{
			return findComponents(c,predicate,false);
		}
	}
	
	private static List<Component> findComponents(Component c, Predicate<Component> predicate, boolean isReturnFirst) {
		List<Component> result = new ArrayList<>();
		
		addMatchingComponents(result, c, predicate, false);
		
		return result ;
	}

	private static void addMatchingComponents(List<Component> result, Component c, Predicate<Component> predicate,
			boolean isReturnFirst) {
		
		if (predicate.test(c))
		{
			result.add(c);
			if (isReturnFirst)
				return;
		}
		
		if (c instanceof Container)
		{
			Container cc = (Container)c;
			for (Component ccc : cc.getComponents()) {
				addMatchingComponents(result, ccc, predicate, isReturnFirst);
				if (!result.isEmpty() && isReturnFirst)
					return ;
			}
		}				
	}

	public static int getVisibleCarePosition(JTextPane jTextPane) {
		Rectangle visibleRect = jTextPane.getVisibleRect();
		int caretPosition = jTextPane.viewToModel(visibleRect.getLocation());
		return caretPosition;
	}

	public static void setFirstLineToCaretPosition(JTextPane jTextPanet, JScrollPane jScrollFrame, int caretPosition) {
	jScrollFrame.getVerticalScrollBar().setValue(0);
		
		try {							
			
			Rectangle2D modelToView2D = jTextPanet.modelToView(caretPosition);
			Rectangle rect = new Rectangle((int) modelToView2D.getX(), 
					(int) modelToView2D.getY(), 
					jTextPanet.getWidth(),
					jTextPanet.getHeight()
					);													
			jTextPanet.scrollRectToVisible(rect);							
		} catch (BadLocationException e1) { 
			e1.printStackTrace();
		}
		
	}

	public static String getWordAtCaret(JTextPane jTextPane) {
		try {
	          int caretPosition = jTextPane.getCaretPosition();
	          int start = Utilities.getWordStart(jTextPane, caretPosition);
	          int end = Utilities.getWordEnd(jTextPane, caretPosition);
	          return jTextPane.getText(start, end - start);
	      } catch (BadLocationException e) {
	          System.err.println(e);
	      }

	      return null;
	}

}
