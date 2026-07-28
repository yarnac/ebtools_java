package com.eb.ebookreader.gobj;

import com.eb.base.io.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LandmarkBookConfig {
    @JsonIgnore
    String fileName;
    String sprache;
    List<LandmarkBookItem> landmarks = new ArrayList<LandmarkBookItem>();
    @JsonIgnore
    int actChapterNr;
    @JsonIgnore
    int actLandmarkNr;



    public void write() throws IOException {
        StringBuilder strb = new StringBuilder();
        strb.append(sprache);
        strb.append("\r\n");
        ObjectMapper mapper = new ObjectMapper();
        for (LandmarkBookItem item : landmarks) {
            strb.append(mapper.writeValueAsString(item));
            strb.append("\r\n");
        }
        FileUtil.WriteText(fileName, strb.toString());
    }

    public static LandmarkBookConfig read(String fileName) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        List<String> lines = FileUtil.readLines("UTF8", fileName);
        LandmarkBookConfig config = new LandmarkBookConfig();
        config.setSprache(lines.get(0));
        for (int i = 1; i < lines.size(); i++) {
            config.landmarks.add(mapper.readValue(lines.get(i), LandmarkBookItem.class));
        }
        return config;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getSprache() {
        return sprache;
    }

    public void setSprache(String sprache) {
        this.sprache = sprache;
    }

    public List<LandmarkBookItem> getLandmarks() {
        return landmarks;
    }

    public void setLandmarks(List<LandmarkBookItem> landmarks) {
        this.landmarks = landmarks;
    }

    public int getActChapterNr() {
        return actChapterNr;
    }

    public void setActChapterNr(int actChapterNr) {
        this.actChapterNr = actChapterNr;
    }

    public int getActLandmarkNr() {
        return actLandmarkNr;
    }

    public void setActLandmarkNr(int actLandmarkNr) {
        this.actLandmarkNr = actLandmarkNr;
    }

    public List<LandmarkBookItem> getLandmarks(int chapter) {
        return landmarks.stream().filter(x->x.getChapterNr()==chapter).toList();
    }
}
