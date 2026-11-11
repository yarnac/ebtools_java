package com.eb.apps.ebvlc.app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VlcResponse {

    public boolean fullscreen;
    public Stats stats;
    public String aspectratio;
    public double seek_sec;
    public int apiversion;
    public int currentplid;
    public int time;
    public int volume;
    public int length;
    public boolean random;
    public AudioFilters audiofilters;
    public Information information;
    public double rate;
    public VideoEffects videoeffects;
    public String state;
    public boolean loop;
    public String version;
    public double position;
    public int audiodelay;
    public boolean repeat;
    public int subtitledelay;
    public List<Double> equalizer;

    // --------------------------------------------------
    // Statistics
    // --------------------------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Stats {
        public double inputbitrate;
        public long sentbytes;
        public long lostabuffers;
        public double averagedemuxbitrate;
        public long readpackets;
        public long demuxreadpackets;
        public long lostpictures;
        public long displayedpictures;
        public long sentpackets;
        public long demuxreadbytes;
        public double demuxbitrate;
        public long playedabuffers;
        public long demuxdiscontinuity;
        public long decodedaudio;
        public double sendbitrate;
        public long readbytes;
        public double averageinputbitrate;
        public long demuxcorrupted;
        public long decodedvideo;
    }

    // --------------------------------------------------
    // Audio filters
    // --------------------------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AudioFilters {
        @JsonProperty("filter_0")
        public String filter0;
    }

    // --------------------------------------------------
    // Information
    // --------------------------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Information {
        public int chapter;
        public List<JsonNode> chapters;
        public int title;

        // Enthält unterschiedliche Strukturen:
        // Stream 0, Stream 1, Stream 2, Stream 3, meta
        public JsonNode category;

        public List<JsonNode> titles;
    }

    // --------------------------------------------------
    // Video effects
    // --------------------------------------------------

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VideoEffects {
        public double hue;
        public double saturation;
        public double contrast;
        public double brightness;
        public double gamma;
    }
}
