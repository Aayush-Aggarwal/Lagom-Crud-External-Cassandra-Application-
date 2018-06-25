package com.example.ayush.example.impl.ct;

import akka.Done;
import com.example.ayush.example.api.ExampleService;
import com.example.ayush.example.api.ExternalJson;
import com.example.ayush.example.api.UserDetails;
import com.example.ayush.example.impl.ExampleServiceImpl;
import com.example.ayush.example.impl.MockedData;
import com.lightbend.lagom.javadsl.persistence.cassandra.CassandraSession;
import com.lightbend.lagom.javadsl.testkit.ServiceTest;
import com.datastax.driver.core.Session;

import static com.lightbend.lagom.javadsl.testkit.ServiceTest.defaultSetup;
import static com.lightbend.lagom.javadsl.testkit.ServiceTest.startServer;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class ExampleServiceImplTest {
    
    private static ServiceTest.TestServer server;
    private static ExampleService exampleServiceMock;
    private static MockedData mockedData = new MockedData();
    
    @BeforeClass
    public static void setUp() throws Exception {
        final ServiceTest.Setup setup = defaultSetup();
        server = startServer(setup.withCassandra(true));
        
        CassandraSession cassandraSession = server.injector().instanceOf(CassandraSession.class);
        
        Session session = cassandraSession.underlying().toCompletableFuture().get();
        
        createSchema(session);
    
        exampleServiceMock = new ExampleServiceImpl(cassandraSession, mockedData);
    
    }
    
    
    public static void createSchema(Session session) {
        session.execute("CREATE KEYSPACE user WITH replication = {'class': 'SimpleStrategy', 'replication_factor':1};");
        session.execute("CREATE TABLE user.user_details(user_id int PRIMARY KEY, user_age int, user_name text);");
        session.execute("insert into user.user_details(user_id,user_age, user_name) values(1,23,'Ayush');");
        session.execute("insert into user.user_details(user_id,user_age, user_name) values(2,24,'Neel');");
    }
    
    @AfterClass
    
    public static void tearDown() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
    
    ExampleService exampleService = server.client(ExampleService.class);
    
    @Test
    public void callPostMethod() throws Exception {
        String message = exampleService.newUser().invoke(UserDetails.builder().id(1).age(23).name("ayush").build()).toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertEquals("Inserted", message);
    }
    
    @Test
    public void callGetMethod() throws Exception {
        List<UserDetails> details = exampleService.getUser(1).invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertEquals(23, details.get(0).getAge());
    }
    
    @Test
    public void callDeleteMethod() throws Exception {
        Done done = exampleService.removeUser(1).invoke().toCompletableFuture().get(5, TimeUnit.SECONDS);
        assertEquals(Done.getInstance(), done);
    }
    
    /*@Test
    public void callExternal() throws Exception {
        ExternalJson externalJson = exampleService.external().invoke().toCompletableFuture().get(5,TimeUnit.SECONDS);
        //assertEquals(externalJson ,ExternalJson.class);
        System.out.println("-------" + externalJson + "---------");
       // assertEquals(true,true);
    }*/
    
    @Test
    public void callExternal() throws Exception {
        ExternalJson externalJson = exampleServiceMock.external().invoke().toCompletableFuture().
                get(5,TimeUnit.SECONDS);
        
        assertEquals(ExternalJson.builder().id(1).userId(1).title("ABCDEF")
        .body("ABCD").build(),externalJson);
    }
    
}