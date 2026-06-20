package com.eb.chatclient;
import java.net.URI;
import java.net.http.*;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.lang.System.out;

public class OpenAIChatClient {

    private static final String API_KEY = System.getenv("OPENAI_API_KEY");
    //private static final String URL = "https://api.openai.com/v1/chat/completions";
    private static final String URL = "http://air-von-ekkart:11434/v1/chat/completions";

    private static final List<Map<String, String>> messages = new ArrayList<>();
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {

        Scanner scanner = new Scanner(System.in);

        // System Prompt (optional)
        messages.add(msg("system", "Du bist ein hilfreicher Assistent."));

        out.println("Chat gestartet. Tippe 'exit' zum Beenden.");

        while (true) {
            out.print("Du: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit"))
                break;

            if (input.equals("context=Tuerkisch"))
            {
                input = "Du bist Türkischexperte. Du gibst kurze und prägnante Antworten. Du antwortest nur auf Anforderungen und Fragen." +
                        " Du übersetzt nach Deutsch und antwortest auf Deutsch";
                messages.add(msg("system", input));
                continue;
            }

            out.println("thinking");

            messages.add(msg("user", input));

            String response = callOpenAI(messages);
            System.out.println("AI: " + response);

            messages.add(msg("assistant", response));
        }
    }

    private static Map<String, String> msg(String role, String content) {
        Map<String, String> m = new HashMap<>();
        m.put("role", role);
        m.put("content", content);
        return m;
    }

    private static String callOpenAI(List<Map<String, String>> messages) throws Exception {

        HttpClient client = HttpClient.newHttpClient();

        Map<String, Object> body = new HashMap<>();
        body.put("model", "qwen3:8B");
        body.put("messages", messages);

        String json = mapper.writeValueAsString(body);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        out.println("Status: " + response.statusCode());
        out.println("Body: " + response.body());

        // sehr simple Extraktion (ohne vollständiges DTO Mapping)
        Map<?, ?> map = mapper.readValue(response.body(), Map.class);
        List<?> choices = (List<?>) map.get("choices");
        Map<?, ?> first = (Map<?, ?>) choices.get(0);
        Map<?, ?> message = (Map<?, ?>) first.get("message");

        return message.get("content").toString();
    }
}