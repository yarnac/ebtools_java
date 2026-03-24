package com.eb.woerterbuch.gobj.formatter;

import java.util.List;

public interface IVokabelListFormatter <T>{
	
	public void setMaxVokabeln(int n);
	public String getString(List<T> vokabelListe);

}
