package com.heroku.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

@Configuration
public class MongoDBConfig {

    @Value("${mongodb.uri}")
    private String mongoDbUri;

    private static String mongoDbUriConstant;

    public @Bean MongoDbFactory mongoDbFactory() {
        if (mongoDbUri.startsWith("mongodb:")) {
            mongoDbUriConstant = mongoDbUri;
        }

        return new SimpleMongoClientDbFactory(mongoDbUriConstant);
    }

    public @Bean MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}