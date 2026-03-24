package com.eb.vlc.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
public class VlcItem {

    private final String id;
    private final String locationXml;
    private final String title;
    private final String titleXml;
    private final String creator;
    private final String album;
    private final String trackNr;
    private final String annotation;
    private final int duration;
    Art art;

    public VlcItem(String title, String title1, String location, String title2, String s, String id, String creator, String album, String trackNr, String annotation, int duration, Art art ) {
        this.title = title;
        this.titleXml = title1;
        this.locationXml = location;
        this.id = id;
        this.creator = creator;
        this.album = album;
        this.trackNr = trackNr;
        this.annotation = annotation;
        this.duration = duration;
        this.art = art;
    }


    public enum Art {
        Privat,
        Oeffentlich,
        News,
        Drittes,
        Ausland,
        Sport,
        Dokumentation,
        Musik
    }

}
