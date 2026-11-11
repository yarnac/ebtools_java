package com.eb.apps.ebvlc.app;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class VLCStatus {

    private static String password = "009WowStatus";

    private static HttpClient client = HttpClient.newHttpClient();

    public static void sendCommand(String command)  {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8080/requests/status.json?command=" + command
                ))
                .header("Authorization", "Basic " + getAuth())
                .GET()
                .build();

        try {
            client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getVlcFileName()  {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(
                        "http://localhost:8080/requests/status.json"
                ))
                .header("Authorization", "Basic " + getAuth())
                .GET()
                .build();



        try {
            HttpResponse<String> response =
                    client.send(request,
                            HttpResponse.BodyHandlers.ofString());
            System.out.println(response.body());

            ObjectMapper mapper = new ObjectMapper();
            VlcResponse vlcResponse = mapper.readValue(response.body(), VlcResponse.class);

            String filename = vlcResponse.information.category
                    .path("meta")
                    .path("filename")
                    .asText(null);

            return filename;

        }
        catch (Exception e) {
            System.out.println("Message: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static String getAuth() {
        String auth = Base64.getEncoder().encodeToString(
                (":" + password).getBytes(StandardCharsets.UTF_8)
        );
        return auth;
    }
}