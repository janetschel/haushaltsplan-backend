package de.janetschel.haushaltsplan.backend.controller;

import de.janetschel.haushaltsplan.backend.service.HealthCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    public final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck() {
        return healthCheckService.healthCheck();
    }
}
