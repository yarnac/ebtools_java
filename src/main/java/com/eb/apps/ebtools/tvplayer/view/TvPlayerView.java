package com.eb.apps.ebtools.tvplayer.view;

import com.eb.apps.ebtools.tvplayer.domain.TvItem;
import com.eb.base.gui.EbSplitPanel;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

@Getter
public class TvPlayerView {

    private JList<String> cbKategorien;
    private DefaultListModel<String> cbKategorienModel;
    private JList<TvItem> lstTvItems;
    private DefaultListModel<TvItem> lstTvItemsModel;
    private EbSplitPanel ebSplitPanel;


    public void initView(JPanel tvPanel)
    {

        tvPanel.setLayout(new BorderLayout());

        cbKategorien = new JList<>();
        cbKategorienModel = new DefaultListModel<>();
        cbKategorien.setModel(cbKategorienModel);
        JScrollPane scrollPaneLeft = new JScrollPane(cbKategorien);

        lstTvItems = new JList<>();
        lstTvItemsModel = new DefaultListModel<TvItem>();
        lstTvItems.setModel(lstTvItemsModel);
        JScrollPane scrollPaneRight = new JScrollPane(lstTvItems);

        EbSplitPanel.BORDER_SIZE = 2;
        ebSplitPanel = new EbSplitPanel("mainPanel", scrollPaneLeft, scrollPaneRight, 1);
        tvPanel.add(ebSplitPanel, BorderLayout.CENTER);
        ebSplitPanel.setDividerLocation(250);
    }

    public JToolBar getToolBarLeft(){return ebSplitPanel.getToolBar1();}
    public JToolBar getToolBarRight(){return ebSplitPanel.getToolBar2();}

    public TvItem getSelectedTvItem() {
        return getLstTvItems().getSelectedValue();
    }

    public void setVisibleTvItems(List<TvItem> collect) {getLstTvItemsModel().clear();
        getLstTvItemsModel().clear();
        getLstTvItemsModel().addAll(collect);
    }

    public void setVisibleKategorien(List<String> collect) {
        cbKategorienModel.clear();
        cbKategorienModel.addAll(collect);
    }
}
