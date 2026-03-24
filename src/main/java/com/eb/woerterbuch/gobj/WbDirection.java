package com.eb.woerterbuch.gobj;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum WbDirection {
	A_Nach_B,
	B_Nach_A,
	Beide;
	
	@Override
	public String toString()
	{
		if (this == A_Nach_B)
			return "A";
		if (this == B_Nach_A)
			return "B";

		return "AB";
	}
	
	public static WbDirection getFromString(String string)
	{
		if (string.equals("A"))
			return A_Nach_B;
		if (string.equals("B"))
			return B_Nach_A;
		
		return Beide;
	}

	public static List<String> getStrings() {
		return Arrays.stream(values()).map(x->x.toString()).collect(Collectors.toList());
	}

	public WbDirection getOpposite() {
		if (this==WbDirection.A_Nach_B)
			return WbDirection.B_Nach_A;
		else
			return WbDirection.A_Nach_B;
	}
}
