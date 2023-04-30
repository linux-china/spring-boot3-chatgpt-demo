package com.chatchatabc.chatgpt.chat;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange("https://api.openai.com")
public interface ChatGPTService {
    @PostExchange("/v1/chat/completions")
    Mono<ChatCompletionResponse> chat(@RequestBody ChatCompletionRequest request);
}
