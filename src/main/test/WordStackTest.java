package main.test;


import com.eb.system.WordStack;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WordStackTest {
	
	String[] woerter = new String[]{"Word1", "Word2", "Word3"};

	@Test
	public void test() {
		WordStack stack = new WordStack();
		
		assertTrue(!stack.hasPrev() && !stack.hasNext());
		
		stack.addWord(woerter[0]);
		assertFalse(stack.hasPrev());
		assertFalse(stack.hasNext());
		
			
		stack.addWord(woerter[1]);
		assertTrue(stack.hasPrev());
		assertFalse(stack.hasNext());
		
		
		assertEquals(woerter[0], stack.getPrev());
		assertFalse(stack.hasPrev());
		assertTrue(stack.hasNext());
		
		assertEquals(woerter[0], stack.getPrev());
		assertFalse(stack.hasPrev());
		assertTrue(stack.hasNext());
		
		assertEquals(woerter[1], stack.getNext());
		assertTrue(stack.hasPrev());
		assertFalse(stack.hasNext());
		
		stack.addWord(woerter[2]);
		assertTrue(stack.hasPrev());
		assertFalse(stack.hasNext());
		
		assertEquals(woerter[1], stack.getPrev());
		assertTrue(stack.hasPrev());
		assertTrue(stack.hasNext());
		
		assertEquals(woerter[0], stack.getPrev());
		assertFalse(stack.hasPrev());
		assertTrue(stack.hasNext());
	}

}
