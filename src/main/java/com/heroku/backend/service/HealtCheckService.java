package com.heroku.backend.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class HealtCheckService {

    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Healthcheck completed");
    }

}
