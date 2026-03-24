package com.eb.ebookreader.tobj;

import com.eb.woerterbuch.gobj.Vokabel;

import java.util.List;

public class EbStringUtil {
	public static int calcMaxWordLength(List<Vokabel> fetchVokabeln, IToStringFunction<Vokabel> iToStringFunction) {
		int n=0;
		for (Vokabel vokabel : fetchVokabeln)
		{
			int m = iToStringFunction.strlen(vokabel);
			if ( m > n )
				n = m;
		}
		return n;
	}
}
