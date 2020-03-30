package com.heroku.backend.controller;

import com.heroku.backend.service.HealtCheckService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("https://haushaltsplan.herokuapp.com/")
public class HealthCheckController {
    public final HealtCheckService healtCheckService;

    public HealthCheckController(HealtCheckService healtCheckService) {
        this.healtCheckService = healtCheckService;
    }

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck() {
        return healtCheckService.healthCheck();
    }
}
