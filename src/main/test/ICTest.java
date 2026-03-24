package main.test;

import javax.swing.ImageIcon;

import com.eb.woerterbuch.gui.WbPanelView;
import org.junit.jupiter.api.Test;

public class ICTest {

	@Test
	public void test() {
		for (IC ic : IC.values())
		{
			try {
				new ImageIcon(WbPanelView.class.getResource("/gr24/" + ic.N() + ".gif"));
			} catch (Exception e) {
				System.out.println (ic.N() + " existiert nicht");
			}
			
			 
			
			
		}
	}

}
