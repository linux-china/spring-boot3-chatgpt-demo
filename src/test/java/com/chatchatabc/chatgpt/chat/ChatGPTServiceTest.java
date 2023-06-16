package com.chatchatabc.chatgpt.chat;

import com.chatchatabc.chatgpt.ProjectBaseTest;
import com.chatchatabc.chatgpt.chat.function.GPTUtils;
import com.chatchatabc.chatgpt.chat.function.JsonSchemaFunction;
import com.chatchatabc.chatgpt.service.GPTFunctions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

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
                    System.out.println(chatCompletionResponse.getReply().getContent());
                })
                .verifyComplete();
    }

    @Test
    public void testFunctions() throws Exception {
        final Map<String, JsonSchemaFunction> jsonSchemaFunctionMap = GPTUtils.extractFunctions(GPTFunctions.class);
        String functionsJson = GPTUtils.toFunctionsJsonArray(List.of(jsonSchemaFunctionMap.get("compile_java")));
        final ChatCompletionRequest chatRequest = ChatCompletionRequest.functions("Give me a simple Java example, and compile the generated source code.", functionsJson);
        final ChatCompletionResponse response = chatGPTService.chat(chatRequest).block();
        final FunctionCall functionCall = response.getReply().getFunctionCall();
        if (functionCall != null) {
            GPTUtils.callGPTFunction(functionsStub, jsonSchemaFunctionMap.get(functionCall.getName()), functionCall.getArguments());
        }
    }
}
