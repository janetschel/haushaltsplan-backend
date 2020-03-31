package com.heroku.backend;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValueConfiguration {

    public static String mongoDbUri;
    public static String authenticationToken;
    public static String base64FirstUser;
    public static String base64SecondUser;

    @Value("${mongodb.uri}")
    public void setMongoDbUri(String mongoDbUri) {
        ValueConfiguration.mongoDbUri = mongoDbUri;
    }

    @Value("${authentication.user}")
    public void setAuthenticationToken(String authenticationToken) {
        ValueConfiguration.authenticationToken = authenticationToken;
    }

    @Value("${users.jan.base64}")
    public void setBase64FirstUser(String base64FirstUser) {
        ValueConfiguration.base64FirstUser = base64FirstUser;
    }

    @Value("${users.lea.base64}")
    public void setBase64SecondUser(String base64SecondUser) {
        ValueConfiguration.base64SecondUser = base64SecondUser;
    }

}
