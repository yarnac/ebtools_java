package com.eb.ebookreader.tobj;

import com.eb.ebwoerterbuch.gobj.WoerterbuchSession;

@FunctionalInterface
public interface IShowUebersetzung {
	void showUebersetzung(WoerterbuchSession session, String word);
}
