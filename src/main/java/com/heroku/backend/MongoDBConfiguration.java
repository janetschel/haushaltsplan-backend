package com.heroku.backend;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

@Configuration
public class MongoDBConfiguration {

    public @Bean MongoDbFactory mongoDbFactory() {
        return new SimpleMongoClientDbFactory(ValueConfiguration.mongoDbUri);
    }

    public @Bean MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongoDbFactory());
    }
}