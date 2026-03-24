package com.eb.vlc.playlistio;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class PlaylistExtension {

    @JacksonXmlProperty(isAttribute = true, localName = "application")
    public String application;


    @JacksonXmlProperty(
            localName = "item",
            namespace = "http://www.videolan.org/vlc/playlist/ns/0/"
    )
    @JacksonXmlElementWrapper(useWrapping = false)
    public List<VlcItemXml> items;
}
