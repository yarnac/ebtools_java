package com.eb.apps.ebchatclient.app;

import com.eb.base.gui.EbGridBagUtil;
import com.eb.base.gui.EbSplitPanel;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class VlcVidFrame extends JFrame {
    @Getter
    private final JToolBar toolBarMain;
    @Getter
    private final EbSplitPanel splitPanelMain;

    public static void main(String[] args) {
        VlcVidFrame frame = new VlcVidFrame();

    }

    VlcVidFrame() {

        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window
        toolBarMain = new JToolBar();
        toolBarMain.setFloatable(false);
        add(toolBarMain, BorderLayout.NORTH);

        splitPanelMain = new EbSplitPanel("panelMain", new JPanel(), new JPanel(), JSplitPane.HORIZONTAL_SPLIT);
        add(splitPanelMain.getMainPanel(), BorderLayout.CENTER);


    }

}
