package com.chatchatabc.chatgpt.chat.function;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonSchemaFunction {
    private String name;
    private String description;
    private Parameters parameters;
    @JsonIgnore
    private Method javaMethod;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public record Parameters(String type, Map<String, Object> properties, List<String> required) {

    }

    public record JsonSchemaProperty(@JsonIgnore String name, String type, String description) {

    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record JsonArrayItems(String type, String description) {

    }

    public record JsonSchemaArrayProperty(@JsonIgnore String name, String type, String description,
                                          JsonArrayItems items) {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public Method getJavaMethod() {
        return javaMethod;
    }

    public void setJavaMethod(Method javaMethod) {
        this.javaMethod = javaMethod;
    }

    public void addProperty(String name, String type, String description) {
        if (this.parameters == null) {
            this.parameters = new Parameters("object", new HashMap<>(), new ArrayList<>());
        }
        this.parameters.properties.put(name, new JsonSchemaProperty(name, type, description));
    }

    public void addArrayProperty(String name, String type, String description) {
        if (this.parameters == null) {
            this.parameters = new Parameters("object", new HashMap<>(), new ArrayList<>());
        }
        this.parameters.properties.put(name, new JsonSchemaArrayProperty(name, "array", description, new JsonArrayItems(type, null)));
    }

    public void addRequired(String name) {
        this.getParameters().required.add(name);
    }
}
