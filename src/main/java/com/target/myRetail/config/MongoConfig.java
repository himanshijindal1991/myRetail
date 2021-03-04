package com.target.myRetail.config;




import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        MongoClient mongoClient = MongoClients.create
                ("mongodb+srv://Himanshi:Himanshi@cluster0.zeeom.mongodb.net/myFirstDatabase?retryWrites=true&w=majority");
     return mongoClient;
    }

}
