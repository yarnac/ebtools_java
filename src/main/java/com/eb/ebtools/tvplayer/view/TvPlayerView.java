package com.eb.ebtools.tvplayer.view;

import com.eb.ebtools.tvplayer.domain.TvItem;
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
    private JToolBar toolBar;

    public void initView(JPanel tvPanel)
    {
        tvPanel.setLayout(new BorderLayout());
        {

            Component leftSplitPanel = new JPanel();
            Component rightSplitPanel = new JPanel();
            JSplitPane splitPane = new JSplitPane(
                    JSplitPane.HORIZONTAL_SPLIT,
                    leftSplitPanel,
                    rightSplitPanel
            );

            cbKategorien = new JList<>();
            cbKategorienModel = new DefaultListModel<>();
            cbKategorien.setModel(cbKategorienModel);

            lstTvItems = new JList<>();
            lstTvItemsModel = new DefaultListModel<TvItem>();
            lstTvItems.setModel(lstTvItemsModel);
            JScrollPane scrollPane = new JScrollPane(lstTvItems);
            splitPane.setLeftComponent(cbKategorien);
            splitPane.setRightComponent(scrollPane);
            splitPane.setDividerLocation(100);

            toolBar = new JToolBar();
            tvPanel.add(toolBar, BorderLayout.NORTH);
            tvPanel.add(splitPane, BorderLayout.CENTER);

            //tvPanel.add(cbKategorien, BorderLayout.WEST);
            //tvPanel.add(scrollPane, BorderLayout.CENTER);
        }
    }

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

    public JToolBar getToolBar() {
        return toolBar;
    }
}
