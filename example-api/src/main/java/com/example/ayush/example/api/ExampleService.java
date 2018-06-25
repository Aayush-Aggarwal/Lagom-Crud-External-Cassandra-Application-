package com.example.ayush.example.api;

import static com.lightbend.lagom.javadsl.api.Service.restCall;
import static com.lightbend.lagom.javadsl.api.Service.named;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;

import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;

import java.util.List;

public interface ExampleService extends Service {
    
    ServiceCall<NotUsed, String> hello();
    
    ServiceCall<UserDetails, String> newUser();
    
    ServiceCall<NotUsed, Done> removeUser(int id);
    
    ServiceCall<NotUsed, List<UserDetails>> getUser(int id);
    
    ServiceCall<NotUsed, ExternalJson> external();
    
    @Override
    default Descriptor descriptor() {
        return named("example").withCalls(
                restCall(Method.GET, "/api/example", this::hello),
                restCall(Method.POST, "/api/example", this::newUser),
                restCall(Method.GET,"/api/example/get/:id", this::getUser),
                restCall(Method.DELETE, "/api/example/delete/:id", this::removeUser),
                restCall(Method.GET,"/api/example/external",this::external)
        ).withAutoAcl(true);
        // Access contol list use for re routing ports
    }
}
