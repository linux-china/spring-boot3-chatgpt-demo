Spring Boot 3 with ChatGPT demo
===============================

Please refer [Spring Boot 3应用如何整合ChatGPT](https://linux-china.davao.page/blog/2023-05-01-spring-boot3-chatgpt)
blog.

# Features

* Spring HTTP Interface to ChatGPT REST API
* Spring Reactive friendly: async and non-blocking
* ChatGPT functions support

# Get started

* Modify `application.properties` to set your OpenAI API key or set the environment variable `OPENAI_API_KEY`
* Run `mvn spring-boot:run`

# ChatGPT Functions support

Create a Spring Bean as following:

```java

@Component
public class GPTFunctions {

    public record CompileJavaRequest(
            @Parameter(required = true, value = "java file name or source code") String source) {
    }

    @GPTFunction(name = "compile_java", value = "Compile Java code")
    public void compileJava(CompileJavaRequest request) {
        System.out.println(request.source);
    }

    public record SendEmailRequest(@Parameter("Receiver's email") String receiverEmail,
                                   @Parameter("Subject of email") String subject,
                                   @Parameter("Content of email") String content) {
    }
}    
```

Call GPT function from ChatGPT response:

```
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
```

# References

* ChatGPT OpenAPI: https://platform.openai.com/docs/api-reference/chat
* Spring HTTP
  Interface: https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#rest-http-interface
* Creating Scalable OpenAI GPT Applications in
  Java: https://dzone.com/articles/creating-scalable-openai-gpt-applications-in-java