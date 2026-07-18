package com.eb.android;

public class DialogTest {
    public static void main(String[] args) {
        final Object[] value = new  String[1];

        Options options = new Options();
        DialogBuilder builder = new DialogBuilder();
        builder.addItem(new DialogItem("Titel", DialogItemType.String,  x -> options.setTitle("" + x), options::getTitle));
        //builder.addItem(new DialogItem("Message", DialogItemType.Enum,  Options.Type.values(),  x -> ((Options)x).setType(options), options::getType));
        DialogConfig config = builder.build();
        DialogItem item = config.getItems().get(1);
        item.writeFunction.accept(item.getEnumType()[1]);
        options.toString();
    }
}
