package com.eb.apps.ebookreader.gobj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LandmarkConfig
{
    private String[] sprachen;
    List<LandmarkItem> items = new ArrayList<>();
    Map<Integer, List<LandmarkItem>> parapgraphItems = new HashMap<>();


    public List<LandmarkItem> getParagraphItems(int n)
    {
        return parapgraphItems.computeIfAbsent(n, intN -> items.stream().filter(x->x.getParagraphNr() == intN.intValue()).toList());
    }


    public List<LandmarkItem> getItems() {
        return items;
    }

    public void setItems(List<LandmarkItem> items) {
        this.items = items;
    }
    public String[] getSprachen() {
        return sprachen;
    }

    public void setSprachen(String[] sprachen) {
        this.sprachen = sprachen;
    }


}
