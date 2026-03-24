package com.eb.vlc.playlistio;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class VlcItemXml {

    @JacksonXmlProperty(isAttribute = true, localName = "tid")
    public String tid;
}
