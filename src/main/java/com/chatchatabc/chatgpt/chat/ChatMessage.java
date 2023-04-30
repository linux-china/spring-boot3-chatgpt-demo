package com.chatchatabc.chatgpt.chat;

public record ChatMessage(String role, String content) {
    public static ChatMessage systemMessage(String content) {
        return new ChatMessage("system", content);
    }

    public static ChatMessage userMessage(String content) {
        return new ChatMessage("user", content);
    }

    public static ChatMessage assistantMessage(String content) {
        return new ChatMessage("assistant", content);
    }
}
