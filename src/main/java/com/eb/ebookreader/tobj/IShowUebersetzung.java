package com.eb.ebookreader.tobj;

import com.eb.woerterbuch.gobj.WoerterbuchSession;

@FunctionalInterface
public interface IShowUebersetzung {
	void showUebersetzung(WoerterbuchSession session, String word);
}
