package com.heroku.backend.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class HealthCheckServiceTest {
    @Mock
    private HealthCheckService healthCheckService;

    @Test
    public void whenHealthCheck_thenNoExceptions() {
        healthCheckService.healthCheck();
        Mockito.verify(healthCheckService, Mockito.times(1)).healthCheck();
    }
}
