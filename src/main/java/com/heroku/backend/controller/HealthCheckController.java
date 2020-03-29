package com.heroku.backend.controller;

import com.heroku.backend.service.HealtCheckService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    public final HealtCheckService healtCheckService;

    public HealthCheckController(HealtCheckService healtCheckService) {
        this.healtCheckService = healtCheckService;
    }

    @GetMapping("/healthcheck")
    public String healthCheck() {
        return healtCheckService.healthCheck();
    }
}
