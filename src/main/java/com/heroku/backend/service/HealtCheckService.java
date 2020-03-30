package com.heroku.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HealtCheckService {

    @Value("${spring.mongodb.uri}")
    private String mongoDbUri;

    public ResponseEntity<String> healthCheck() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println(mongoDbUri);

        String response = "Healthcheck completed at " + dateTimeFormatter.format(localDateTime);
        return ResponseEntity.ok(mongoDbUri);
    }

}
