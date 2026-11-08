package com.eb.apps.ebvlc.app;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VideoCtrlParams
{
    String verzeichnis;
    String favoritenVerzeichnis;
    List<String> allFiles;
    List<String> deletedFiles;
    List<String> favoriteFiles;
}
