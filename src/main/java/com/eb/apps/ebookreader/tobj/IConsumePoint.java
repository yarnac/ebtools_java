package com.eb.apps.ebookreader.tobj;
@FunctionalInterface
public interface IConsumePoint {
	void accept(int x, int y, boolean addPoint);
}
