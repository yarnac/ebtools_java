package com.eb.apps.ebchat_client.app;

import com.eb.base.gui.decorator.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;

public class VlcVidCtrl {
    private final IniFile myIniFile;
    private final VlcVidFrame frame;

    public static void main(String[] args) {
        VlcVidCtrl ctrl = new VlcVidCtrl();

    }


    public VlcVidCtrl() {
        myIniFile = IniFileProvider.createIniFile("VlcVidCtrl.ini");
        frame = new VlcVidFrame();
        GuiDecorator decorator = new GuiDecorator(frame, myIniFile, "Einstellungen");
        decorator.addContainer("tbMain", frame.getToolBarMain());
        decorator.addEditIniFileButton();

        decorator.addContainer("tbLeft", frame.getSplitPanelMain().getToolBar1());
        decorator.addEditIniFileButton();

        decorator.addContainer("tbRight", frame.getSplitPanelMain().getToolBar2());
        decorator.addEditIniFileButton();


        SwingUtilities.invokeLater(()-> {
            frame.setVisible(true);
        });
    }
}
