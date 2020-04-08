package de.janetschel.haushaltsplan.backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class HealthCheckService {

    public ResponseEntity<String> healthCheck() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();

        String response = "Healthcheck completed at " + dateTimeFormatter.format(localDateTime);
        return ResponseEntity.ok(response);
    }

}
