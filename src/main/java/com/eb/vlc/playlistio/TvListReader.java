package com.eb.vlc.playlistio;

import com.eb.vlc.domain.VlcItem;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TvListReader {

    public static void main(String[] args) {
        List<VlcItem> vlcItems = TvListReader.readTvListEntries("c:\\Users\\ekkart\\EbToolsDaten\\CSharp\\EbTool\\Medien\\Tv.xspf");
        // List<VlcItem> vlcItems = TvListReader.readTvListEntries("c:\\Users\\ekkart\\EbToolsDaten\\CSharp\\EbTool\\Medien\\AudioNew.xspf");
        System.out.println(vlcItems);
    }

    public static List<VlcItem> readTvListEntries(String fileName) {
        File file = new File(fileName);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try {
            XmlMapper xmlMapper = new XmlMapper();

            Playlist playlist = xmlMapper.readValue(file, Playlist.class);

            List<VlcItem> result = new ArrayList<>();

            if (playlist.trackList != null && playlist.trackList.tracks != null) {
                for (Track track : playlist.trackList.tracks) {

                    result.add(new VlcItem(
                            track.title,
                            track.title,              // kein echtes InnerXml mehr
                            track.location,
                            track.title,
                            "",                        // extension XML nicht nötig
                            track.extension != null ? track.extension.id : "",
                            track.creator,
                            track.album,
                            track.trackNr,
                            track.annotation,
                            track.duration,
                            VlcItem.Art.Musik
                    ));
                }
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}