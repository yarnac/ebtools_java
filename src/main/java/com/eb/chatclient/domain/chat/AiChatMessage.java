package com.eb.chatclient.domain.chat;

public class AiChatMessage {
    private String systemQuestion;
    private String userQuestion;
    private String result;
    private String model;

    private java.time.OffsetDateTime start;
    private java.time.OffsetDateTime stop;

    private String storedAnswer;

    private int userErsteZeile;
    private int outputErsteZeile;

    private int id;

    // C# static flags
    public static boolean showUserQuestion = false;
    public static boolean showModel = false;

    private AiSession session;

    private int inputTokens;
    private int outputTokens;
    private int totalTokens;

    private String version;

    // Not serialized in C# via [JsonIgnore]
    private Object llmRequest;
    private Object llmResponse;
    private AiChat chat;

    private String title;

    public AiChatMessage() {
    }

    public AiChatMessage(String systemQuestion, String userQuestion, String model, String result) {
        this.session = new AiSession();
        this.session.setName("New Session");
        this.session.addSystemMessage(systemQuestion);
        this.session.addUserMessage(userQuestion);

        this.systemQuestion = systemQuestion;
        this.userQuestion = userQuestion;
        this.model = model;
        this.result = result;
    }

    public String getSystemQuestion() { return systemQuestion; }
    public void setSystemQuestion(String systemQuestion) { this.systemQuestion = systemQuestion; }

    public String getUserQuestion() { return userQuestion; }
    public void setUserQuestion(String userQuestion) { this.userQuestion = userQuestion; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public java.time.OffsetDateTime getStart() { return start; }
    public void setStart(java.time.OffsetDateTime start) { this.start = start; }

    public java.time.OffsetDateTime getStop() { return stop; }
    public void setStop(java.time.OffsetDateTime stop) { this.stop = stop; }

    public String getStoredAnswer() { return storedAnswer; }
    public void setStoredAnswer(String storedAnswer) { this.storedAnswer = storedAnswer; }

    public int getUserErsteZeile() { return userErsteZeile; }
    public void setUserErsteZeile(int userErsteZeile) { this.userErsteZeile = userErsteZeile; }

    public int getOutputErsteZeile() { return outputErsteZeile; }
    public void setOutputErsteZeile(int outputErsteZeile) { this.outputErsteZeile = outputErsteZeile; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public AiSession getSession() { return session; }
    public void setSession(AiSession session) { this.session = session; }

    public int getInputTokens() { return inputTokens; }
    public void setInputTokens(int inputTokens) { this.inputTokens = inputTokens; }

    public int getOutputTokens() { return outputTokens; }
    public void setOutputTokens(int outputTokens) { this.outputTokens = outputTokens; }

    public int getTotalTokens() { return totalTokens; }
    public void setTotalTokens(int totalTokens) { this.totalTokens = totalTokens; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public AiChat getChat() { return chat; }
    public void setChat(AiChat chat) { this.chat = chat; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAnswer() {
        if (storedAnswer != null) return storedAnswer;
        if (result == null) return "Kein Output erhalten";

        // Minimal Java port: full JSON parsing from C# omitted here.
        // Keep behavior safe; caller may still store parsed answer into storedAnswer.
        return result;
    }

    public String getModelNameFromResult() {
        // Minimal port: not implemented; return "unknown" like C# catch.
        return "unknown";
    }

    @Override
    public String toString() {
        if (title != null && !title.isBlank()) return title;

        StringBuilder sb = new StringBuilder();
        sb.append(id);

        if (showModel) {
            sb.append(" ").append(model).append(": ");
        }

        if (showUserQuestion) {
            if (userQuestion != null) sb.append(" ").append(userQuestion.trim());
        } else {
            String ans = getAnswer();
            if (ans != null && !ans.trim().isEmpty()) sb.append(" ").append(ans.trim());
            else sb.append(" unknown");
        }

        String res = sb.toString().trim();
        if (res.length() > 100) return res.substring(0, 100);
        return res;
    }
}
