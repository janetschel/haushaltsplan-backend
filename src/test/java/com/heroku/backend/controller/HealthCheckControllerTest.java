package com.heroku.backend.controller;

import com.heroku.backend.service.HealthCheckService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(HealthCheckController.class)
public class HealthCheckControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthCheckService healthCheckService;

    @Test
    public void whenHealthcheck_thenOkMessage_andStatus200() throws Exception {
        Mockito.when(healthCheckService.healthCheck()).thenReturn(ResponseEntity.ok("Healthcheck ok"));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/healthcheck"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("Healthcheck ok")));

        Mockito.verify(healthCheckService, Mockito.times(1)).healthCheck();
    }
}
