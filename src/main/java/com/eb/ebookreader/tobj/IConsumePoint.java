package com.eb.ebookreader.tobj;
@FunctionalInterface
public interface IConsumePoint {
	void accept(int x, int y, boolean addPoint);
}
