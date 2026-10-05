package com.eb.apps.ebchatclient.codegen;

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

        decorator.addContainer("tbCode", frame.getToolBarCode());
        decorator.addToolbarButton("tbCode", "Generiere Code", ICF.ArrowFlowVertical,this::handleSwitchLinespaces);

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

    boolean showWithLines;
    private void handleSwitchLinespaces(ActionEvent actionEvent) {
        String code;
        if (showWithLines) {
            code = frame.getEdCode().getText().replace("\n","\\n");
        }
        else
            code = frame.getEdCode().getText().replace("\\n","\n");
        showWithLines = !showWithLines;
        frame.getEdCode().setText(code);
    }

    private void handleGenerateCode(ActionEvent actionEvent) {
        AiCodeGenerator generator = new AiCodeGenerator(frame.getEdNamespace().getText(), frame.getEdTargetDir().getText());
        generator.generateCodeDateien(frame.getEdCode().getText().replace("\"codeText\"","\"content\"").replace("\n", "\\n"));
    }

    public void setCodeText(String text) {
        frame.getEdCode().setText(text);
    }
}
