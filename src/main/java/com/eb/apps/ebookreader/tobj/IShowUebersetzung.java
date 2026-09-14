package com.eb.apps.ebookreader.tobj;

import com.eb.apps.ebwoerterbuch.gobj.WoerterbuchSession;

@FunctionalInterface
public interface IShowUebersetzung {
	void showUebersetzung(WoerterbuchSession session, String word);
}
