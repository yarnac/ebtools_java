package com.eb.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class MavenArtefakt {

    private String artefaktId;
    private String fileName;
    private String version;
    private String groupId;

    @Setter
    private MavenArtefakt parent;
    private List<MavenArtefakt> children;
    private List<MavenArtefakt> dependencies;
    private List<MavenArtefakt> dependencyManagements;
}
/* Generiere eine Klasse MavenArtefaktTreeFactory, die ausgehend von einem Programmverzeichnis mit
   einer Datei pom.xml */