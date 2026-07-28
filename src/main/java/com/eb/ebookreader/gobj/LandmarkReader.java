package com.eb.ebookreader.gobj;

import com.eb.base.io.FileUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class LandmarkReader {
    public LandmarkConfig getConfig() {
        return config;
    }

    private final LandmarkConfig config;
    @JsonIgnore
    private String fileName;
    private String fileContents;

    public static void main(String[] args) throws IOException {
        LandmarkReader reader = new LandmarkReader("c:\\data\\Landmarks.txt");


        LandmarkBookConfig configPortugiesisch = new LandmarkBookConfig();
        configPortugiesisch.setSprache("Portugiesisch");
        configPortugiesisch.setFileName("c:\\\\Users\\\\ekkart\\\\EbToolsDaten\\\\Reader\\\\Books\\\\Harry Potter e as Reliquias da Morte - J.K. Rowling_LM.txt");

        LandmarkBookConfig configFranzoesisch = new LandmarkBookConfig();
        configFranzoesisch.setSprache("Französisch");
        configFranzoesisch.setFileName("c:\\Users\\ekkart\\EbToolsDaten\\Reader\\Books\\Harry Potter et les Reliques de la Mort - J.K. Rowling_LM.txt");

        LandmarkBookConfig configTuerkisch = new LandmarkBookConfig();
        configTuerkisch.setSprache("Türkisch");
        configTuerkisch.setFileName("c:\\Users\\ekkart\\EbToolsDaten\\Reader\\Books\\Harry Potter ve Olum Yadigarlari - J.K. Rowling_LM.txt");

        for(LandmarkItem item : reader.getConfig().getItems())
        {
            configPortugiesisch.getLandmarks().add(new LandmarkBookItem(item.getParagraphNr(), item.getLandmarkNr(), item.getSearchTexte().get(0)));
            configFranzoesisch.getLandmarks().add(new LandmarkBookItem(item.getParagraphNr(), item.getLandmarkNr(), item.getSearchTexte().get(1)));
            configTuerkisch.getLandmarks().add(new LandmarkBookItem(item.getParagraphNr(), item.getLandmarkNr(), item.getSearchTexte().get(2)));
        }

        configPortugiesisch.write();
        configFranzoesisch.write();
        configTuerkisch.write();
    }

    public LandmarkReader(String fileName) throws IOException {
        this.fileName = fileName;
        this.fileContents = FileUtil.readText("UTF8", fileName);
        String[] split = fileContents.split("\n");
        config = new LandmarkConfig();

        split.toString();
        int lineNr = 0;

        if (!split[lineNr++].equals("All"))
            throw new RuntimeException("Fehler All erwartet");
        String[] sprachen = split[lineNr++].split(",");
        config.setSprachen(sprachen);
        lineNr++;

        ObjectMapper mapper = new ObjectMapper();

        while (lineNr < split.length) {
            String[] parts = split[lineNr++].split(" ");
            LandmarkItem item = new LandmarkItem();
            config.items.add(item);
            item.setParagraphNr(Integer.parseInt(parts[0]));
            item.setLandmarkNr(Integer.parseInt(parts[1]));
            for (int i=0; i<config.getSprachen().length; i++) {

                item.getSearchTexte().add(mapper.readerFor(String.class).readValue(split[lineNr++]));

            }
            lineNr++;
        }

        List<LandmarkItem> paragraphItems = config.getParagraphItems(2);
        paragraphItems.toString();
    }
}
