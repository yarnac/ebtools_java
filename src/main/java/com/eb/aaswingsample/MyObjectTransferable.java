package com.eb.aaswingsample;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class MyObjectTransferable implements Transferable {

    public static final DataFlavor MY_OBJECT_FLAVOR =
            new DataFlavor(
                    DataFlavor.javaJVMLocalObjectMimeType
                            + ";class=" + MyObject.class.getName(),
                    "MyObject"
            );

    private final MyObject object;

    public MyObjectTransferable(MyObject object) {
        this.object = object;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { MY_OBJECT_FLAVOR };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return MY_OBJECT_FLAVOR.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }

        return object;
    }
}