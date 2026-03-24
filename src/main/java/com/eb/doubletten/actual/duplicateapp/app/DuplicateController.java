package com.eb.doubletten.actual.duplicateapp.app;

import com.eb.base.gui.GuiDecorator;
import com.eb.base.gui.IC;
import com.eb.base.inifile.api.IniFile;
import com.eb.base.inifile.api.IniFileProvider;
import com.eb.doubletten.actual.duplicates.api.IDuplicateScanService;
import com.eb.doubletten.actual.duplicates.api.IDuplikatContainer;
import com.eb.doubletten.actual.duplicateapp.gui.DuplicateView;
import com.eb.doubletten.actual.duplicates.repositories.DuplicateRepository;
import com.eb.ebookreader.tobj.StringUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class DuplicateController {

    private final DuplicateView view;
    private final IDuplicateScanService scanService;
    private final DuplicateRepository repository;
    private final IniFile inifile;
    private final DefaultComboBoxModel<String> cbPathModel = new DefaultComboBoxModel<>();

    public DuplicateController(
            DuplicateView view,
            IDuplicateScanService scanService,
            DuplicateRepository repository
    ) {
        this.view = view;
        this.scanService = scanService;
        this.repository = repository;
        inifile = IniFileProvider.createIniFile("NewDuplicates.ini");

        GuiDecorator decorator = new GuiDecorator(view, inifile, "Einstellungen");
        decorator.addContainer("main", view.getMainToolBar());
        decorator.addContainer("left", view.getLeftToolBar());
        decorator.addContainer("right", view.getRightToolBar());
        decorator.addEditIniFileButton("main");
        decorator.addToolbarButton("main", "Hilfe", IC.HELP_BLUE, (e)->{});
        decorator.addToolbarButton("left", "Hilfe", IC.HELP_BLUE, (e)->{});
        decorator.addToolbarButton("right", "Hilfe", IC.HELP_BLUE, (e)->{});


        transferInifileToView(view);
        wireEvents();
    }

    private void transferInifileToView(DuplicateView view) {
        List<String> pathList = inifile.getSectionValues("Paths");
        pathList.add(0,"");
        view.setPathList(pathList);
        int split = inifile.getSectionValueAsInteger("Einstellungen", "Split", 100);
        view.getSplitPane().setDividerLocation(split);
    }
    private void transferViewToIniFile() {
        List<String> list = view.getPathList();
        list.remove("");
        list.sort(String::compareTo);
        inifile.setSectionValues("Paths", list);
        inifile.setSectionValue("Einstellungen", "Split", view.getSplitPane().getDividerLocation());
        inifile.Write();
    }
    private void wireEvents() {

        view.getCbPaths().addActionListener(this::onPathChanged);

        view.getContainerList().addListSelectionListener(e -> {
            onContainerSelected();
        });

        view.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });
    }

    private void onWindowClosing() {
        transferViewToIniFile();
    }

    private void onPathChanged(ActionEvent actionEvent) {
        Object value = view.getCbPaths().getSelectedItem();

        if (StringUtil.isNullOrEmpty(value)) {
            clearLists();
            return;
        }

        String directory = (String) value;
        Path directoryPath = Path.of(directory);

        if (!Files.isDirectory(directoryPath)) {
            clearLists();
            return;
        }

        if (!view.getPathList().contains(directoryPath.toString())) {
            view.addPath(directory);
        }

        List<? extends IDuplikatContainer> result = scanService.scan(directoryPath);

        repository.setContainers(result);

        view.getContainerList().setListData(
                result.toArray(IDuplikatContainer[]::new)
        );
    }
    private void clearLists() {
        view.getContainerList().setListData(new IDuplikatContainer[0]);
        view.getDuplicateList().setListData(new String[0]);
    }

    private void onContainerSelected() {

        IDuplikatContainer selected =
                view.getContainerList().getSelectedValue();

        if (selected == null) {
            view.getDuplicateList().setListData(new String[0]);
            return;
        }

        view.getDuplicateList().setListData(
                selected.getDuplikate().toArray(String[]::new)
        );
    }
}