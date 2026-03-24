package com.eb.vlc.playlistio;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TrackExtension {
    @JacksonXmlProperty(isAttribute = true, localName = "application")
    public String application;

    @JacksonXmlProperty(
            isAttribute = true,
            localName = "id",
            namespace = "http://www.videolan.org/vlc/playlist/ns/0/"
    )
    public String id;


    @JacksonXmlProperty(
            isAttribute = true,
            localName = "option",
            namespace = "http://www.videolan.org/vlc/playlist/ns/0/"
    )

    public String options;
}
