package com.eb.apps.ebchat_client.app;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class TorRequest {

    public static void main(String[] args) throws Exception {
        // Tor Browser Bundle: normalerweise Port 9150
        // Eine separate Tor-Installation verwendet häufig Port 9050.
        InetSocketAddress torAddress =
                new InetSocketAddress("127.0.0.1", 9150);

        Proxy torProxy = new Proxy(
                Proxy.Type.SOCKS,
                torAddress
        );



        URI uri = URI.create("https://www.heise.de");

        HttpURLConnection connection =
                (HttpURLConnection) uri.toURL().openConnection(torProxy);

        connection.setRequestMethod("GET");
        connection.setInstanceFollowRedirects(true);
        connection.setConnectTimeout(15_000);
        connection.setReadTimeout(30_000);
        connection.setRequestProperty("User-Agent", "Java-Tor-Client/1.0");

        int statusCode = connection.getResponseCode();


        System.out.println("HTTP-Status: " + statusCode);
        System.out.println("Antwort:");

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream(),
                        StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } finally {
            connection.disconnect();
        }
    }
}
