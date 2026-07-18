package com.eb.android;

import java.util.List;

public class DialogBuilder {

    DialogConfig config;
    public DialogBuilder() {
        config = new DialogConfig();
    }

    public DialogBuilder setItems(List<DialogItem> items) {
        config.setItems(items);
        return this;
    }

    public DialogBuilder addItem(DialogItem item) {
        config.getItems().add(item);
        return this;
    }

    public DialogConfig build() {
        return config;
    }
}
