package com.example.ayush.example.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor
@Builder
@Value
public class ExternalJson {
    
    int id;
    int userId;
    String body;
    String title;
}
