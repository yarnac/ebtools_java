package com.eb.ebtools.tvplayer.infrastructure;

import com.eb.base.io.FileUtil;
import com.eb.ebtools.tvplayer.domain.TvKategorisierung;

import java.io.IOException;
import java.util.*;

public class TvKategorizer {

    public static TvKategorisierung read(String fileName)
    {
        TvKategorisierung k = new TvKategorisierung();
        try {
            Set<String> kategorienSet = new HashSet<>();
            List<String> lines = FileUtil.readLines("UTF-8", fileName);
            for(String line : lines)
            {
                if (line.trim().length() == 0)
                    continue;
                String[] split = line.split("=");
                List<String> args = Arrays.stream(split[1].split(";")).toList();
                k.getItemToKategorien().put(split[0], args);
                kategorienSet.addAll(args);
            }

            k.getKategorien().clear();
            k.getKategorien().addAll(kategorienSet
                    .stream()
                    .sorted((o1,o2)->String.CASE_INSENSITIVE_ORDER.compare(o1,o2))
                    .toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return k;
    }
}
