package com.eb.vlc.playlistio;

import com.fasterxml.jackson.dataformat.xml.annotation.*;

import java.util.List;

@JacksonXmlRootElement(localName = "playlist")
public class Playlist {
    @JacksonXmlProperty(
            isAttribute = true,
            localName = "version")
    public String version;

    @JacksonXmlProperty(localName = "title")
    public String title;



    @JacksonXmlProperty(localName = "trackList")
    public TrackList trackList;

    @JacksonXmlProperty(localName = "extension")
    public PlaylistExtension extension;
}

