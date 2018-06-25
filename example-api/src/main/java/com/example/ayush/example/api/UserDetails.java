package com.example.ayush.example.api;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UserDetails {
    int id;
    int age;
    String name;
}
