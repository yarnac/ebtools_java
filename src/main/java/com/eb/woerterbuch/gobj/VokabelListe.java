package com.eb.woerterbuch.gobj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class VokabelListe {
	private List<Vokabel> vokabeln = new ArrayList<>();
	private Dictionary<String, Vokabel> dict = new Hashtable<>();

	public void AddVokabel(String word, String... bedeutungen) {
		Vokabel v = new Vokabel(word, Arrays.asList(bedeutungen));
		addVokabel(v);		
	}

	public void addAdditionalVokabel(Vokabel v, boolean replace) {
		Vokabel vokabel = dict.get(v.getWort());
		if (vokabel!=null)
		{
			if (!replace)
				vokabel.addBedeutungen(v.getBedeutungen());
		}
		else
		{
			addVokabel(v);
		}		
	}
	
	public void addVokabel(Vokabel v) {
		dict.put(v.getWort(), v);
		getVokabeln().add(v);		
	}
	

	public Vokabel findVokabel(Vokabel v) {
			
		return dict.get(v.getWort());
	}
	
	
	public boolean isSameVokabel(Vokabel x, Vokabel v) {
		if (x==null)
			this.toString();
		if (x.getUnifiedWort()==null)
			this.toString();
		if (v.getUnifiedWort()==null)
			this.toString();
		return x.getUnifiedWort().equals(v.getUnifiedWort());
	}

	public List<Vokabel> getVokabeln() {
		return vokabeln;
	}

	public void setVokabeln(List<Vokabel> vokabeln) {
		this.vokabeln = vokabeln;
	}

	public Vokabel getVokabel(String wort) {
		return dict.get(wort);
	}

	public void AddVokabelBedeutungen(Vokabel v) {
		v.getReverse().forEach(x->addVokabel(x));
	}

	public void deleteVokabel(Vokabel vokabel) {
		dict.remove(vokabel.getWort());
		getVokabeln().remove(vokabel);
		
	}

	public void deleteVokabelBedeutungen(Vokabel vokabel) {
		for (String s : vokabel.getBedeutungen()) {
			Vokabel v = getVokabel(s);
			if (v!=null && v.getBedeutungen().contains(vokabel.getWort()))
				v.removeBedeutung(vokabel.getWort());
		}
		
	}

}




