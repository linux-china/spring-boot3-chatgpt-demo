package com.chatchatabc.chatgpt;


import com.chatchatabc.chatgpt.chat.ChatCompletionRequest;
import com.chatchatabc.chatgpt.chat.ChatGPTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ChatRobotController {
    @Autowired
    private ChatGPTService chatGPTService;

    @PostMapping("/chat")
    public Mono<String> chat(@RequestBody String content) {
        return chatGPTService.chat(ChatCompletionRequest.of(content))
                .map(chatCompletionResponse -> chatCompletionResponse.getReply().getContent());
    }
}
