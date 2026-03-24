package com.eb.ebookreader.tobj;

@FunctionalInterface
public interface IToStringFunction<T> {
	String apply(T t);
	
	public default int strlen(T t) {
		return apply(t).length();	
	}
}
