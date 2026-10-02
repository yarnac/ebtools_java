package com.eb.apps.ebchatclient.app;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.ICF;
import com.eb.base.gui.persist.ComponentPersisterFactory;
import com.eb.base.gui.persist.IComponentPersister;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AiCodeGeneratorCtrl {

    private final IniFile myIniFile;
    private final AiCodeGeneratorFrame frame;

    public AiCodeGeneratorCtrl() {
        frame = new AiCodeGeneratorFrame();
        myIniFile = IniFileProvider.createIniFile("JavaAiPlaygroundCtrl.ini");

        GuiDecorator decorator = new GuiDecorator(frame, myIniFile, "Einstellungen");
        decorator.setFrame(frame);
        decorator.addContainer("tbGen", frame.getToolBar());
        decorator.addToolbarButton("tbGen", "Generiere Code", ICF.ArrowFlowVertical,this::handleGenerateCode);

        IComponentPersister persister = ComponentPersisterFactory.createIniFilePersister(myIniFile);
        persister.addComponentItem(frame.getEdNamespace(),"Namespace");

        persister.addComponentItem(frame.getEdTargetDir(),"Targetdirectory");
        persister.addComponentItem(frame.getEdCode(),"Code");

        persister.loadAndSetComponentItems();
        decorator.addCloseAction(persister::persistComponentItems);

        SwingUtilities.invokeLater(()-> {
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

    private void handleGenerateCode(ActionEvent actionEvent) {
        AiCodeGenerator generator = new AiCodeGenerator(frame.getEdNamespace().getText(), frame.getEdTargetDir().getText());
        generator.generateCodeDateien(frame.getEdCode().getText().replace("\"codeText\"","\"content\""));
    }

    public void setCodeText(String text) {
        frame.getEdCode().setText(text);
    }
}
