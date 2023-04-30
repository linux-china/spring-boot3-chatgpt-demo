package com.chatchatabc.chatgpt.chat;

import org.jetbrains.annotations.NotNull;

public record ChatMessage(@NotNull String role, @NotNull String content) {
    public static ChatMessage systemMessage(@NotNull String content) {
        return new ChatMessage("system", content);
    }

    public static ChatMessage userMessage(@NotNull String content) {
        return new ChatMessage("user", content);
    }

    public static ChatMessage assistantMessage(@NotNull String content) {
        return new ChatMessage("assistant", content);
    }
}
