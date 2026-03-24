package com.eb.system;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

}
