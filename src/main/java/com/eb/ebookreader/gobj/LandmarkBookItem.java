package com.eb.ebookreader.gobj;

public class LandmarkBookItem {

    private int chapterNr;
    private int landmarkNr;
    private String searchString;

    public LandmarkBookItem() {

    }

    public LandmarkBookItem(int paragraphNr, int landmarkNr, String s) {
        this.chapterNr = paragraphNr;
        this.landmarkNr = landmarkNr;
        this.searchString = s;
    }

    public int getChapterNr() {
        return chapterNr;
    }

    public void setChapterNr(int chapterNr) {
        this.chapterNr = chapterNr;
    }

    public int getLandmarkNr() {
        return landmarkNr;
    }

    public void setLandmarkNr(int landmarkNr) {
        this.landmarkNr = landmarkNr;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
