package com.eb.apps.snippets.app;

import com.eb.apps.snippets.domain.SnippetTree;
import com.eb.apps.snippets.gui.SnippetEditCtrl;
import com.eb.apps.snippets.gui.SnippetTreeCtrl;
import com.eb.apps.snippets.gui.SnippetWindowFrame;
import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
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

        decorator.setMenuBar(frame.getJMenuBar());
        //decorator.addMenuItem("Speichern",()->{});



        SwingUtilities.invokeLater(() -> {
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private GuiDecorator initializeView() {
        GuiDecorator decorator = new GuiDecorator(frame, myIniFile, "Einstellungen");
        decorator.addContainer("mainToolBar", frame.getMainToolBar());
        decorator.addContainer("treeToolBar", frame.getSplitPanel().getToolBar1());
        decorator.addContainer("editToolBar", frame.getSplitPanel().getToolBar2());
        decorator.addToolbarButton("mainToolBar", "Speichern", ICF.SaveAll, (e)->{});
        decorator.addToolbarButton("treeToolBar", "Speichern", ICF.SaveAll, (e)->{});
        decorator.addToolbarButton("editToolBar", "Speichern", ICF.SaveAll, (e)->{});
        return decorator;
    }
}
