package com.heroku.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class ValueConfig {

    public static String mongoDbUri;
    public static String authenticationToken;

    @Value("${mongodb.uri}")
    public void setMongoDbUri(String mongoDbUri) {
        ValueConfig.mongoDbUri = mongoDbUri;
    }

    @Value("${authentication.user}")
    public void setAuthenticationToken(String authenticationToken) {
        ValueConfig.authenticationToken = authenticationToken;
    }
}
