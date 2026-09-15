package com.eb.base.ai_service.llm_client.api;

import com.eb.base.ai_service.llm_client.infrastructure.ollama.OllamaClient;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.io.IOException;

public class CheckNetworkService {

    public static void main(String[] args) {
        for (String host : OllamaClient.HOSTS)
        {
            CompletableFuture<Boolean> isAvailable2 = isOllamaAvailableAsync(host,11434);
            isAvailable2.thenAccept(isAvailable3 -> {
                System.out.println("%s isAvailable: ".formatted(host) + isAvailable3);
            });
        }

    }

    public static CompletableFuture<Boolean> isOllamaAvailableAsync(
        String host,
        int port) {
        
        HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_1_1)
            .connectTimeout(Duration.ofSeconds(3) )
            .build();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://%s:11434/api/version".formatted(host)))
            .timeout(Duration.ofSeconds(3))
            .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return CompletableFuture.completedFuture(response.statusCode() < 300);
        } catch (IOException | InterruptedException e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    public boolean isExistingHost(String url) {
        try {
            InetAddress address = InetAddress.getByName(url);
            NetworkInterface networkInterface = NetworkInterface.getByInetAddress(address);
            return networkInterface != null;
        } catch (UnknownHostException | SocketException e) {
            return false;
        }
    }
}