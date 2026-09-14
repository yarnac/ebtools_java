package com.eb.apps.ebtools.tvplayer.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class TvKategorisierung {

    private List<String> kategorien = new ArrayList<String>();
    private Map<String, List<String>> itemToKategorien = new HashMap<>();
}
