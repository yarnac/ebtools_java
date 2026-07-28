package com.eb.ebookreader.gobj;

import java.util.ArrayList;
import java.util.List;

public class LandmarkItem {
    int paragraphNr;
    int landmarkNr;
    List<String> searchTexte = new ArrayList<String>();

    public int getParagraphNr() {
        return paragraphNr;
    }

    public void setParagraphNr(int paragraphNr) {
        this.paragraphNr = paragraphNr;
    }

    public int getLandmarkNr() {
        return landmarkNr;
    }

    public void setLandmarkNr(int landmarkNrM) {
        this.landmarkNr = landmarkNrM;
    }

    public List<String> getSearchTexte() {
        return searchTexte;
    }

    public void setSearchTexte(List<String> searchTexte) {
        this.searchTexte = searchTexte;
    }
}
