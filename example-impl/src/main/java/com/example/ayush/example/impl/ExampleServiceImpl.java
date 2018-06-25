package com.example.ayush.example.impl;

import akka.Done;
import akka.NotUsed;
import com.example.ayush.example.api.DemoService;
import com.example.ayush.example.api.ExampleService;
import com.example.ayush.example.api.ExternalJson;
import com.example.ayush.example.api.UserDetails;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class ExampleServiceImpl implements ExampleService {
    
    private final CassandraSession session;
    DemoService demoService;
    
    @Inject
    public ExampleServiceImpl(CassandraSession session,DemoService demoService)
    {
        this.session = session;
        this.demoService = demoService;
    }
    
    
    @Override
    public ServiceCall<NotUsed, String> hello() {
        return request -> CompletableFuture.completedFuture("hello");
    }
    
    @Override
    public ServiceCall<UserDetails, String> newUser() {
        return request -> session.executeWrite("insert into user.user_details(user_id, user_age, user_name) values(?,?,?)",
                request.getId(), request.getAge(), request.getName())
                .thenApply(done -> "Inserted");
        
    }
    
    @Override
    public ServiceCall<NotUsed, Done> removeUser(int id) {
        return request -> session.executeWrite("delete from user.user_details where user_id = ?",id)
                .thenApply(done -> Done.getInstance());
    }
    
    @Override
    public ServiceCall<NotUsed, List<UserDetails>> getUser(int id) {
        return request -> session.selectOne("select * from user.user_details where user_id = ?", id)
                .thenApply(row -> {
                    return Arrays.asList(UserDetails.builder().name(row.get().getString("user_name"))
                    .age(row.get().getInt("user_age"))
                    .id(row.get().getInt("User_id")).build());
                });
    }
    
    @Override
    public ServiceCall<NotUsed, ExternalJson> external() {
        CompletionStage<ExternalJson> data = demoService.demo().invoke().thenApply(row -> row);
        return request -> data;
    }
}
