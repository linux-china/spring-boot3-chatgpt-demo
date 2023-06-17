package com.chatchatabc.chatgpt.chat;

import com.chatchatabc.chatgpt.ProjectBaseTest;
import com.chatchatabc.chatgpt.app.service.GPTFunctions;
import org.junit.jupiter.api.Test;
import org.mvnsearch.chatgpt.model.ChatCompletionRequest;
import org.mvnsearch.chatgpt.model.ChatCompletionResponse;
import org.mvnsearch.chatgpt.model.ChatMessage;
import org.mvnsearch.chatgpt.spring.service.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ChatGPTServiceTest extends ProjectBaseTest {

    @Autowired
    private ChatGPTService chatGPTService;
    @Autowired
    private GPTFunctions functionsStub;

    @Test
    public void testChat() throws Exception {
        final Mono<ChatCompletionResponse> response = chatGPTService.chat(ChatCompletionRequest.of("What is Java?"));
        StepVerifier.create(response)
                .consumeNextWith(chatCompletionResponse -> {
                    for (ChatMessage chatMessage : chatCompletionResponse.getReply()) {
                        System.out.println(chatMessage.getContent());
                    }
                })
                .verifyComplete();
    }

}
