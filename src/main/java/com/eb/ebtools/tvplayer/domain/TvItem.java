package com.eb.ebtools.tvplayer.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TvItem {
    private String name;
    private String extInf;
    private String extVlcOpt;
    private String rstp;

    @Override public String toString() {
        return name;
    }
}
