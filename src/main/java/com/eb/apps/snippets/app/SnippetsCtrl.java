package com.eb.apps.snippets.app;

import com.eb.apps.snippets.gui.SnippetEditCtrl;
import com.eb.apps.snippets.gui.SnippetTreeCtrl;
import com.eb.apps.snippets.gui.SnippetWindowFrame;
import com.eb.base.gui.decorator.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.gui.decorator.GuiDecoratorToolBars;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;

public class SnippetsCtrl {

    private final SnippetWindowFrame frame;private final IniFile myIniFile;
    private final GuiDecorator decorator;
    private final SnippetTreeCtrl treeCtrl;
    private final SnippetEditCtrl editCtrl;

    public static void main(String[] args){
        SnippetsCtrl ctrl = new SnippetsCtrl();
    }

    SnippetsCtrl(){

        frame = new SnippetWindowFrame();
        myIniFile = IniFileProvider.createIniFile("MySnippets.ini");

        decorator = initializeView();

        treeCtrl = new SnippetTreeCtrl(frame.getJTree(), frame.getSplitPanel().getToolBar1());
        editCtrl = new SnippetEditCtrl(frame.getEdSnippetText(), frame.getSplitPanel().getToolBar2());

        decorator.menuBars().setMenuBar(frame.getJMenuBar());
        //decorator.addMenuItem("Speichern",()->{});



        SwingUtilities.invokeLater(() -> {
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private GuiDecorator initializeView() {
        GuiDecorator guiDecorator = new GuiDecorator(frame, myIniFile, "Einstellungen");

        GuiDecoratorToolBars toolBarDecorator = guiDecorator.toolBars();

        toolBarDecorator.addContainer("mainToolBar", frame.getMainToolBar());
        toolBarDecorator.addToolbarButton("Speichern", ICF.SaveAll, (e)->{});

        toolBarDecorator.addContainer("treeToolBar", frame.getSplitPanel().getToolBar1());
        toolBarDecorator.addToolbarButton("Speichern", ICF.SaveAll, (e)->{});

        toolBarDecorator.addContainer("editToolBar", frame.getSplitPanel().getToolBar2());
        toolBarDecorator.addToolbarButton("Speichern", ICF.SaveAll, (e)->{});

        return guiDecorator;
    }
}
