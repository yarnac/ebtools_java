package com.eb.inspector.domain.attributes;


import com.eb.base.gui.decorator.GuiDecorator;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;

import javax.swing.*;

public class InspectMappingConfigCtrl {

    private final InspectMappingConfigWindow window;
    private final IniFile myIniFile;

    public InspectMappingConfigCtrl() {
        window = new InspectMappingConfigWindow();
        myIniFile = IniFileProvider.createIniFile("Inspect.ini");
        GuiDecorator decorator = new GuiDecorator(window, myIniFile, "Einstellungen");
        GenericsInspectAttributeProvider.reset();
    }


    public static void main(String ...args)
    {
        InspectMappingConfigCtrl inspectCtrl = new InspectMappingConfigCtrl();
        SwingUtilities.invokeLater(()->{
            inspectCtrl.window.setVisible(true);
            //inspectCtrl.setObject(Example.createExample());
        });

    }
}
