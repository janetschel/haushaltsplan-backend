package de.janetschel.haushaltsplan.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValueConfig {

    public static String authenticationToken;
    public static String base64FirstUser;
    public static String base64SecondUser;

    @Value("${authentication.user}")
    public void setAuthenticationToken(String authenticationToken) {
        ValueConfig.authenticationToken = authenticationToken;
    }

    @Value("${users.jan.base64}")
    public void setBase64FirstUser(String base64FirstUser) {
        ValueConfig.base64FirstUser = base64FirstUser;
    }

    @Value("${users.lea.base64}")
    public void setBase64SecondUser(String base64SecondUser) {
        ValueConfig.base64SecondUser = base64SecondUser;
    }

}
