package com.eb.apps.ebchatclient.components.fileprovider;

import javax.swing.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;

class FileNameDropTarget extends DropTarget {
    private final JList<String> fileNameList;
    private final FileNameListModel listModel;

    public FileNameDropTarget(JList<String> fileNameList, FileNameListModel listModel) {
        super();
        this.fileNameList = fileNameList;
        this.listModel = listModel;

        setComponent(fileNameList);
        setActive(true);

        try {
            addDropTargetListener(new FileNameDropTargetListener(listModel));
        } catch (TooManyListenersException e) {
            throw new RuntimeException(e);
        }
    }

    private static class FileNameDropTargetListener implements DropTargetListener {
        private final FileNameListModel listModel;

        public FileNameDropTargetListener(FileNameListModel listModel) {
            this.listModel = listModel;
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            if (isFileFlavor(dtde)) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            } else {
                dtde.rejectDrag();
            }
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            if (isFileFlavor(dtde)) {
                dtde.acceptDrag(DnDConstants.ACTION_COPY);
            } else {
                dtde.rejectDrag();
            }
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
        }

        @Override
        public void dragExit(DropTargetEvent dte) {
        }

        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                if (!isFileFlavor(dtde)) {
                    dtde.rejectDrop();
                    return;
                }

                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                Transferable transferable = dtde.getTransferable();

                @SuppressWarnings("unchecked")
                List<File> files = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

                for (File file : files) {
                    listModel.addFileName(file.getAbsolutePath());
                }

                dtde.dropComplete(true);
            } catch (Exception e) {
                e.printStackTrace();
                dtde.rejectDrop();
            }
        }

        private boolean isFileFlavor(DropTargetEvent dte) {
            return true;
            /*dte.getCurrentDataFlavors() != null &&
                    java.util.Arrays.stream(dte.getCurrentDataFlavors())
                            .anyMatch(DataFlavor::isFlavorJavaFileListType);*/
        }
    }
}
