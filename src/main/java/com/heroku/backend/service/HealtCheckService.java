package com.heroku.backend.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HealtCheckService {

    public String healthCheck() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();

        String response = "Healthcheck completed at " + dateTimeFormatter.format(localDateTime);
        return response;
    }

}
