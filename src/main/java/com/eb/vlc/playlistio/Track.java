package com.eb.vlc.playlistio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Track {

    @JacksonXmlProperty(localName = "title")
    public String title;

    @JacksonXmlProperty(localName = "location")
    public String location;

    @JacksonXmlProperty(localName = "creator")
    public String creator;

    @JacksonXmlProperty(localName = "album")
    public String album;

    @JacksonXmlProperty(localName = "trackNum")
    public String trackNr;

    @JacksonXmlProperty(localName = "annotation")
    public String annotation;

    @JacksonXmlProperty(localName = "duration")
    public int duration;

    @JacksonXmlProperty(localName = "extension")
    public TrackExtension extension;

}
