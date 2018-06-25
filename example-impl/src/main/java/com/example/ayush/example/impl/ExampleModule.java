package com.example.ayush.example.impl;

import com.example.ayush.example.api.DemoService;
import com.example.ayush.example.api.ExampleService;
import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;

public class ExampleModule extends AbstractModule implements ServiceGuiceSupport {
    
    @Override
    protected void configure() {
        bindService(ExampleService.class, ExampleServiceImpl.class);
        bindClient(DemoService.class);
    }
}
