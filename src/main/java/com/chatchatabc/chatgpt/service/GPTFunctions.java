package com.chatchatabc.chatgpt.service;

import com.chatchatabc.chatgpt.chat.function.GPTFunction;
import com.chatchatabc.chatgpt.chat.function.Parameter;
import org.springframework.stereotype.Component;

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

    @GPTFunction(name = "send_email", value = "Send email to receiver")
    public void sendEmail(SendEmailRequest request) {
        System.out.println("send email to :" + request.receiverEmail);
    }
}
