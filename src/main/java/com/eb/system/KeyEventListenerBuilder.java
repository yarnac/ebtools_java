package com.eb.system;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


public class KeyEventListenerBuilder {
	
	private List<KeyEventDataItem> itemsTyped = new ArrayList<>();
	private List<KeyEventDataItem> itemsPressed = new ArrayList<>();
	private List<KeyEventDataItem> itemsReleased = new ArrayList<>();
	
	public interface IKeyEventHandler {
		void handle(KeyEvent e);
	}
	
	public KeyEventListenerBuilder addKeyTyped(Predicate<KeyEvent> predicate, IKeyEventHandler r)
	{
		return addKeyEvent(predicate, r, itemsTyped);
	}
	

	public KeyEventListenerBuilder addKeyReleased(Predicate<KeyEvent> predicate, IKeyEventHandler r)
	{
		return addKeyEvent(predicate, r, itemsReleased);
	}
	
	public KeyEventListenerBuilder addKeyPressedd(Predicate<KeyEvent> predicate, int code, IKeyEventHandler r)
	{
		return addKeyEvent(predicate, r, itemsPressed);
	}


	private KeyEventListenerBuilder addKeyEvent(Predicate<KeyEvent> predicate, IKeyEventHandler r,
			List<KeyEventDataItem> items) {
			items.add(new KeyEventDataItem(predicate, r));
		return this;
	}
	
		
	public KeyListener build()
	{
		return new KeyListener() {
		
			@Override public void keyTyped(KeyEvent e) {handleKeyEvent(itemsTyped, e);}			
			@Override public void keyReleased(KeyEvent e) {handleKeyEvent(itemsReleased, e);}
			@Override public void keyPressed(KeyEvent e) {handleKeyEvent(itemsPressed, e);}

			private void handleKeyEvent(List<KeyEventDataItem> items, KeyEvent e) {
				for (KeyEventDataItem item : items) {
					if (!item.getKeyEventPredicate().test(e))						
						continue;
					item.getKeyEventHandler().handle(e);
					if (item.isComsuming())
						break;
					
				}			
			}
		};
				
	}
	
	
	private class KeyEventDataItem
	{		
		private Predicate<KeyEvent> keyEventPredicate;
		private IKeyEventHandler keyEventHandler;

		private KeyEventDataItem(Predicate<KeyEvent> predicate, IKeyEventHandler handler) {			
			setKeyEventPredicate(predicate);
			setKeyEventHandler(handler);
		}

		private boolean isComsuming() {
			return true;
		}

		private Predicate<KeyEvent> getKeyEventPredicate() {
			return keyEventPredicate;
		}

		private void setKeyEventPredicate(Predicate<KeyEvent> keyEventPredicate) {
			this.keyEventPredicate = keyEventPredicate;
		}

		private IKeyEventHandler getKeyEventHandler() {
			return keyEventHandler;
		}

		private void setKeyEventHandler(IKeyEventHandler keyEventHandler) {
			this.keyEventHandler = keyEventHandler;
		}
	}

}
