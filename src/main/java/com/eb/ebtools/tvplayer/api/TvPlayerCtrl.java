package com.eb.ebtools.tvplayer.api;

import com.eb.base.MainGlobals;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.io.FileUtil;
import com.eb.ebtools.tvplayer.domain.TvItem;
import com.eb.ebtools.tvplayer.domain.TvKategorisierung;
import com.eb.ebtools.tvplayer.infrastructure.TvItemReader;
import com.eb.ebtools.tvplayer.infrastructure.TvKategorizer;
import com.eb.ebtools.tvplayer.view.TvPlayerView;
import lombok.Getter;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class TvPlayerCtrl {

    private final IniFile myIniFile;
    private List<TvItem> tvM3UItems;
    private TvKategorisierung kategorisierung;
    private TvPlayerView view;
    private boolean shouldShowAll;
    private boolean ignoreChangeKategorien;

    public TvPlayerCtrl(IniFile iniFile, JPanel tvPanel)
    {
        myIniFile = iniFile;
        view = new TvPlayerView();
        view.initView(tvPanel);

        GuiDecorator decorator = new GuiDecorator();
        decorate(decorator);
        decorator.addMouseDoubleClickAction(view.getLstTvItems(), ()->onShowFile(view.getSelectedTvItem()));

        view.getCbKategorien().addListSelectionListener(e -> kategorieChanged());

        //view.getLstTvItems().addListSelectionListener(e -> onShowFile(e));

        reload();
    }

    private void decorate(GuiDecorator decorator) {

        decorator.addContainer("main", getView().getToolBar());

        decorator.addToolbarButton("main", "Hörzu", IC.VIDEO_LIBRARY_SEARCH, x->openHoerzu());
        decorator.addToolbarButton("main", "Open Tv File", IC.EDITDOC, x->tvFileBearbeiten());
        decorator.addToolbarButton("main", "Reload", IC.REFRESHPAGE, x->reload());
        decorator.addToolbarButton("main", "Export", IC.SAVE_ADD, x->copyScript());
        decorator.addToolbarButton("main", "Toggle Show All", IC.FILTER, x->toggleShowAll());


    }

    private void openHoerzu() {
        FileUtil.showWebseite("https://www.hoerzu.de/tv-programm/jetzt");
    }

    private void tvFileBearbeiten() {
        FileUtil.open(MainGlobals.getEbToolsFileName("Tv/TvListeAll.txt"));
    }

    private void copyScript() {

    }

    private void kategorieChanged() {
        if (ignoreChangeKategorien) {
            return;
        }
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
        view.setVisibleKategorien(kategorisierung.getKategorien().stream().filter(x->isAllowedKategorie(x)).toList());
        ignoreChangeKategorien = false;
        view.getCbKategorien().setSelectedValue(oldValue,true);
    }

    private void transfer() {
        String cat = myIniFile.getSectionValue("Einstellungen", "LastCategory", null);
        view.getCbKategorien().setSelectedValue(cat, true);
        view.setVisibleTvItems(getTvM3UItems().stream().filter(x->isAllowedItem(x)).collect(Collectors.toList()));
    }

    Set<String> schrottKategorien = Arrays.stream("Schrott,x,Musik,Shops,Religion".split(",")).collect(Collectors.toSet());
    private boolean isAllowedKategorie(String x) {
        if (shouldShowAll || x.endsWith("*"))
            return true;

        if (schrottKategorien.contains(x))
            return false;

        return !kategorisierung.getKategorien().contains(x + "*");
    }

    private boolean isAllowedItem(TvItem x) {
        String kategorie = (String) view.getCbKategorien().getSelectedValue();
        if (kategorie == null) {
            return true;
        }
        List <String> kategorien = getKategorisierung().getItemToKategorien().get(x.getName());
        if (kategorien==null)
            return true;
        for (String k : kategorien) {
            if (k.startsWith(kategorie)) {
                return true;
            }
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
        String tempFile =  MainGlobals.getTempFileName("Doit.m3u");
        FileUtil.WriteLines("UTF-8", tempFile, lines);
        FileUtil.open(tempFile);
    }

    public void reload() {
        loadTvList(MainGlobals.getEbToolsFileName("Tv/tvsd.m3u"));
        loadKategorisierung(MainGlobals.getEbToolsFileName("Tv/TvListeAll.txt"));
        transferKategorien();
        transfer();
    }

    public void toggleShowAll() {
        shouldShowAll = !shouldShowAll;
        transferKategorien();
    }
}
