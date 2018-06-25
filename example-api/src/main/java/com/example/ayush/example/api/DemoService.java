package com.example.ayush.example.api;

import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import static com.lightbend.lagom.javadsl.api.Service.named;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.transport.Method;
import jdk.nashorn.internal.objects.annotations.Getter;

import static com.lightbend.lagom.javadsl.api.Service.restCall;


public interface DemoService extends Service {
    
    ServiceCall<NotUsed, ExternalJson> demo();
    
    @Override
    default Descriptor descriptor(){
        return named("demo").withCalls(
                restCall(Method.GET,"/posts/1",this::demo)
        ).withAutoAcl(true);
    }
    
}