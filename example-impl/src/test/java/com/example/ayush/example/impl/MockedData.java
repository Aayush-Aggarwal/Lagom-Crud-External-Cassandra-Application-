package com.example.ayush.example.impl;

import akka.NotUsed;
import com.example.ayush.example.api.DemoService;
import com.example.ayush.example.api.ExternalJson;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lightbend.lagom.javadsl.api.ServiceCall;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MockedData implements DemoService {
    
    private static final ObjectMapper MAPPER = new ObjectMapper();
    
    private String data = "{\n" +
            "    \"id\": 1,\n" +
            "    \"userId\": 1,\n" +
            "    \"body\": \"ABCD\",\n" +
            "    \"title\": \"ABCDEF\"\n" +
            "}";
    
    @Override
    public ServiceCall<NotUsed, ExternalJson> demo() {
        return req -> {
            ExternalJson externalJson = null;
            try {
                externalJson = MAPPER.readValue(data,ExternalJson.class);
            
            } catch (IOException ex) {
                System.out.println("Its exception :)\n" + ex.getMessage());
            
            }
            return CompletableFuture.completedFuture(externalJson);
        };
    }
}
