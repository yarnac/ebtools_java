package com.eb.ebtools.tvplayer.infrastructure;

import com.eb.base.io.FileUtil;
import com.eb.ebookreader.tobj.StringUtil;
import com.eb.ebtools.tvplayer.domain.TvItem;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class TvItemReader {

    public static List<TvItem> readTvM3UItems(String fileName)
    {
        Set<String> names = new HashSet<String>();
        try {
            List<String> lines = FileUtil.readLines("UTF-8", fileName);
            List<TvItem> allItems = new ArrayList<TvItem>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (line.startsWith("#EXTINF")) {
                    TvItem item = new TvItem();
                    item.setExtInf(lines.get(i++));
                    item.setExtVlcOpt(lines.get(i++));
                    item.setRstp(lines.get(i));

                    setNameWithSdOrHd(item);
                    names.add(item.getName());
                    allItems.add(item);
                }
            }

            List<TvItem> result = allItems
                    .stream()
                    .filter(x -> !isSdItemithExistingHdItem(x, names))
                    .collect(Collectors.toList());

            result
                    .stream()
                    .forEach(x->x.setName(x.getName().replace(" SD","")));

            result.sort((o1, o2) -> String.CASE_INSENSITIVE_ORDER.compare(o1.getName(),o2.getName()));
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isSdItemithExistingHdItem(TvItem x, Set<String> names) {
        if (!x.getName().endsWith(" SD"))
            return false;
        String hdName = x.getName().replace(" SD", " HD");
        return names.contains(hdName);
    }

    private static void setNameWithSdOrHd(TvItem item) {
        String tempName = StringUtil.substringAfterFirst(item.getExtInf(), ",", false);
        if (tempName.endsWith(" HD") || tempName.endsWith(" SD"))
            item.setName(tempName);
        else
            item.setName(tempName + " SD");
    }

}
