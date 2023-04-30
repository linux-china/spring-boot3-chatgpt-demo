package com.chatchatabc.chatgpt.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatCompletionRequest {
    private String model = "gpt-3.5-turbo";
    private double temperature = 1;
    private List<ChatMessage> messages;

    public ChatCompletionRequest() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public static ChatCompletionRequest of(String userMessage) {
        return of(null, userMessage, null);
    }

    public static ChatCompletionRequest of(String systemMessage, String userMessage, String assistantMessage) {
        ChatCompletionRequest request = new ChatCompletionRequest();
        if (systemMessage != null && !systemMessage.isEmpty()) {
            request.addMessage(ChatMessage.systemMessage(systemMessage));
        }
        request.addMessage(ChatMessage.userMessage(userMessage));
        if (assistantMessage != null && !assistantMessage.isEmpty()) {
            request.addMessage(ChatMessage.assistantMessage(assistantMessage));
        }
        return request;
    }

    public void addMessage(ChatMessage message) {
        if (this.messages == null) {
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }
}