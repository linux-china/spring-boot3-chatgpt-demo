package com.chatchatabc.chatgpt;

import com.chatchatabc.chatgpt.chat.ChatGPTService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ChatGPTConfiguration {

    @Bean
    public ChatGPTService chatGPTService(@Value("${openai.api.key}") String openaiApiKey) {
        WebClient webClient = WebClient.builder().defaultHeader("Authorization", "Bearer " + openaiApiKey).build();
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builder().clientAdapter(WebClientAdapter.forClient(webClient)).build();
        return httpServiceProxyFactory.createClient(ChatGPTService.class);

    }
}
