package com.eb.doubletten.actual.duplicateapp.app;

import com.eb.doubletten.actual.duplicateapp.gui.DuplicateView;
import com.eb.doubletten.actual.duplicates.api.DuplicateScanServiceFactory;
import com.eb.doubletten.actual.duplicates.api.IDuplicateScanService;
import com.eb.doubletten.actual.duplicates.repositories.DuplicateRepository;

import javax.swing.*;

public class DuplicateApp {

        public static void main(String[] args) {

            SwingUtilities.invokeLater(() -> {

                DuplicateView view = new DuplicateView();

                IDuplicateScanService scanService = DuplicateScanServiceFactory.getDuplicateScanService(DuplicateScanServiceFactory.DuplicateScanServiceType.HashCode);

                DuplicateRepository repository =
                        new DuplicateRepository();

                new DuplicateController(
                        view,
                        scanService,
                        repository
                );

                view.setVisible(true);
            });
        }
    }
