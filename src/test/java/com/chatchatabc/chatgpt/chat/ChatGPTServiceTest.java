package com.chatchatabc.chatgpt.chat;

import com.chatchatabc.chatgpt.ProjectBaseTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ChatGPTServiceTest extends ProjectBaseTest {

    @Autowired
    private ChatGPTService chatGPTService;

    @Test
    public void testChat() throws Exception {
        final Mono<ChatCompletionResponse> response = chatGPTService.chat(ChatCompletionRequest.of("What is Java?"));
        StepVerifier.create(response)
                .consumeNextWith(chatCompletionResponse -> {
                    System.out.println(chatCompletionResponse.getReply().content());
                })
                .verifyComplete();
    }
}
