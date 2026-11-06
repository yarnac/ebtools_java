package com.eb.apps.ebtools.tvplayer.api;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;
import com.eb.apps.ebchat_client.domain.chat.GlobaleEinstellungen;
import com.eb.apps.ebtools.tvplayer.domain.TvItem;
import com.eb.apps.ebtools.tvplayer.domain.TvKategorisierung;
import com.eb.apps.ebtools.tvplayer.infrastructure.TvItemReader;
import com.eb.apps.ebtools.tvplayer.infrastructure.TvKategorizer;
import com.eb.apps.ebtools.tvplayer.view.TvPlayerView;
import lombok.Getter;

import javax.swing.*;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class TvPlayerCtrl {

    public static final Set<String> SCHROTT_KATEGORIEN_SET = Set.of("Schrott", "x", "Musik", "Shops", "Religion");
    public static final String HTTPS_WWW_HOERZU_DE_TV_PROGRAMM_JETZT = "https://www.hoerzu.de/tv-programm/jetzt";
    private static String getTvKategorisierungFilePfad() {return GlobaleEinstellungen.getDataPfadJUser("Tv/TvListeAll.txt");}
    private static String getTvM3uFilePfad() {return GlobaleEinstellungen.getDataPfadJUser("Tv/tv.m3u");}

    private final IniFile myIniFile;
    private List<TvItem> tvM3UItems;
    private TvKategorisierung kategorisierung;
    private final TvPlayerView view;
    private boolean shouldShowAll;
    private boolean ignoreChangeKategorien;

    public TvPlayerCtrl(IniFile iniFile, JPanel tvPanel)
    {
        myIniFile = iniFile;
        view = new TvPlayerView();
        view.initView(tvPanel);

        GuiDecorator decorator = new GuiDecorator();
        decorate(decorator);

        view.getCbKategorien().addListSelectionListener(e -> kategorieChanged());

        reload();
    }

    private void decorate(GuiDecorator decorator) {

        decorator.addContainer("main", getView().getToolBar());

        decorator.addToolbarButton("main", "Hörzu", IC.VIDEO_LIBRARY_SEARCH, x->openHoerzu());
        decorator.addToolbarButton("main", "Open Tv File", IC.EDITDOC, x->tvFileBearbeiten());
        decorator.addToolbarButton("main", "Reload", IC.REFRESHPAGE, x->reload());
        decorator.addToolbarButton("main", "Export", IC.SAVE_ADD, x->copyScript());
        decorator.addToolbarButton("main", "Toggle Show All", IC.FILTER, x->toggleShowAll());
        decorator.addMouseDoubleClickAction(view.getLstTvItems(), ()->onShowFile(view.getSelectedTvItem()));
    }

    private void openHoerzu() {
        FileUtil.showWebseite(HTTPS_WWW_HOERZU_DE_TV_PROGRAMM_JETZT);
    }

    private void tvFileBearbeiten() {FileUtil.open(getTvKategorisierungFilePfad());}

    private void copyScript() {

    }

    private void kategorieChanged() {
        if (ignoreChangeKategorien) {return;}

        String cat = view.getCbKategorien().getSelectedValue();
        if (cat!=null)
        {
            myIniFile.setSectionValue("Einstellungen", "LastCategory", cat);
            myIniFile.Write();
        }
        transfer();
    }

    private void onShowFile(TvItem item) {
        if (item==null)
            return;

        showFile(item);
    }

    private void transferKategorien()
    {
        String oldValue = view.getCbKategorien().getSelectedValue();
        ignoreChangeKategorien = true;
        view.setVisibleKategorien(kategorisierung.getKategorien().stream().filter(this::isAllowedKategorie).toList());
        ignoreChangeKategorien = false;
        view.getCbKategorien().setSelectedValue(oldValue,true);
    }

    private void transfer() {
        String cat = myIniFile.getSectionValue("Einstellungen", "LastCategory", null);
        view.getCbKategorien().setSelectedValue(cat, true);
        view.setVisibleTvItems(getTvM3UItems().stream().filter(this::isAllowedItem).collect(Collectors.toList()));
    }

    private boolean isAllowedKategorie(String x) {
        if (shouldShowAll || x.endsWith("*")) return true;
        if (SCHROTT_KATEGORIEN_SET.contains(x)) return false;
        return !kategorisierung.getKategorien().contains(x + "*");
    }

    private boolean isAllowedItem(TvItem x) {
        String kategorie = view.getCbKategorien().getSelectedValue();
        if (kategorie == null) return true;

        List<String> kategorien = Optional.ofNullable(getKategorisierung())
                .map(TvKategorisierung::getItemToKategorien)
                .map(map -> map.get(x.getName()))
                .orElse(Collections.emptyList());

        for (String k : kategorien) {
            if (k.startsWith(kategorie)) return true;
        }
        return false;
    }

    private void loadKategorisierung(String ebToolsFileName) {
        kategorisierung = TvKategorizer.read(ebToolsFileName);
    }

    private void loadTvList(String fileName) {
        tvM3UItems = TvItemReader.readTvM3UItems(fileName);
        view.getLstTvItemsModel().removeAllElements();
        view.getLstTvItemsModel().addAll(tvM3UItems);
    }

    public void showFile(TvItem v) {
        List<String> lines = new ArrayList<>();
        lines.add("#EXTM3U");
        lines.add(v.getExtInf());
        lines.add(v.getExtVlcOpt());
        lines.add(v.getRstp());
        String tempFile =  GlobaleEinstellungen.getTempFileName("Doit.m3u");
        FileUtil.WriteLines("UTF-8", tempFile, lines);
        FileUtil.open(tempFile);
    }

    public void reload() {

        loadTvList(getTvM3uFilePfad());
        loadKategorisierung(getTvKategorisierungFilePfad());
        transferKategorien();
        transfer();
    }

    public void toggleShowAll() {
        shouldShowAll = !shouldShowAll;
        transferKategorien();
    }
}
