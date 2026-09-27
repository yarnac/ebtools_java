package com.eb.jabai;

import java.io.IOException;
import java.util.List;

public class JabaiWindowApp {

    public static void main(String[] args) throws IOException {
        JabaiWindowInfoService jabaiWindowInfoService = new JabaiWindowInfoService();

        List<JabaiWindowInfo> jabaiWindowInfos = jabaiWindowInfoService.determineJabaiWindowInfos();

        for (JabaiWindowInfo jabaiWindowInfo : jabaiWindowInfos) {

            String message = "id = %d, title = '%s', app = '%s'".formatted(
                            jabaiWindowInfo.id(),
                            jabaiWindowInfo.title(),
                    jabaiWindowInfo.app());

            if (!message.trim().isEmpty()) {
                System.out.println(message);
            }
        }
    }
}
