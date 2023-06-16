package com.chatchatabc.chatgpt.chat.function;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

public class GPTUtils {
    public static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    /**
     * extract GPT functions from class by scanning methods with @GPTFunction
     *
     * @param clazz Java Class
     * @return GPT functions
     */
    public static Map<String, JsonSchemaFunction> extractFunctions(Class<?> clazz) {
        Map<String, JsonSchemaFunction> functionDeclares = new HashMap<>();
        for (Method method : clazz.getMethods()) {
            //check GPT function or not
            final GPTFunction gptFunctionAnnotation = method.getAnnotation(GPTFunction.class);
            if (gptFunctionAnnotation != null) {
                String functionName = gptFunctionAnnotation.name();
                if (functionName.isEmpty()) {
                    functionName = method.getName();
                }
                JsonSchemaFunction gptFunction = new JsonSchemaFunction();
                gptFunction.setJavaMethod(method);
                gptFunction.setName(functionName);
                gptFunction.setDescription(gptFunctionAnnotation.value());
                final Class<?> requestClazz = method.getParameterTypes()[0];
                for (Field field : requestClazz.getDeclaredFields()) {
                    // parse properties
                    final Parameter functionParamAnnotation = field.getAnnotation(Parameter.class);
                    if (functionParamAnnotation != null) {
                        String fieldName = functionParamAnnotation.name();
                        String fieldType = getJsonSchemaType(field.getType());
                        if (fieldName.isEmpty()) {
                            fieldName = field.getName();
                        }
                        gptFunction.addProperty(fieldName, fieldType, functionParamAnnotation.value());
                        if (functionParamAnnotation.required()) {
                            gptFunction.addRequired(fieldName);
                        }
                    }
                }
                functionDeclares.put(functionName, gptFunction);
            }
        }
        return functionDeclares;
    }

    private static String getJsonSchemaType(Class<?> clazz) {
        if (clazz.equals(Integer.class) || clazz.equals(int.class)
                || clazz.equals(Long.class) || clazz.equals(long.class)) {
            return "integer";
        } else if (clazz.equals(Boolean.class) || clazz.equals(boolean.class)) {
            return "boolean";
        } else if (clazz.equals(Double.class) || clazz.equals(double.class)
                || clazz.equals(Float.class) || clazz.equals(float.class)) {
            return "number";
        } else {
            return "string";
        }
    }

    /**
     * convert functions to json array
     *
     * @param jsonSchemaFunctions JSON Schema functions
     * @return JSON Array text
     * @throws Exception exception
     */
    public static String toFunctionsJsonArray(Collection<JsonSchemaFunction> jsonSchemaFunctions) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchemaFunctions);
    }

    /**
     * call GPT function
     *
     * @param target        target object
     * @param function      json schema function
     * @param argumentsJson arguments json string
     * @return result
     * @throws Exception exception
     */
    public static Object callGPTFunction(Object target, JsonSchemaFunction function, String argumentsJson) throws Exception {
        final Method javaMethod = function.getJavaMethod();
        final Class<?> parameterType = javaMethod.getParameterTypes()[0];
        final Object param = objectMapper.readValue(argumentsJson, parameterType);
        return javaMethod.invoke(target, param);
    }

}
