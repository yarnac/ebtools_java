package com.eb.apps.ebchatclient.clients;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class CheckNetworkService {

    public boolean checkIsAvailable(String url) {
        if (url == null || url.isEmpty()) {
            return false;
        }

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(url, 80), 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}