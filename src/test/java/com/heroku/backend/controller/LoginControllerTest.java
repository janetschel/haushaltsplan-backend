package com.heroku.backend.controller;

import com.heroku.backend.service.LoginService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(LoginController.class)
public class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginService loginService;

    @Test
    public void loginUserWithHeaderPresentShouldReturnTrueFromService() throws Exception {
        String authorization = "samplestring";
        Mockito.when(loginService.loginUser(authorization)).thenReturn(ResponseEntity.ok(true));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/login").header("Auth-String", authorization))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

        Mockito.verify(loginService, Mockito.times(1)).loginUser(authorization);
    }

    @Test
    public void loginUserWithInvalidHeaderPresentShouldReturnHttpStatusForbiddenFromService() throws Exception {
        String authorization = "wrongsamplestring";
        Mockito.when(loginService.loginUser(authorization)).thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).body(false));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/login").header("Auth-String", authorization))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden());

        Mockito.verify(loginService, Mockito.times(1)).loginUser(authorization);
    }

    @Test
    public void loginUserWithNoHeaderPresentShouldReturnHttpStatusBadRequestFromService() throws Exception {
        String authorization = "samplestring";
        Mockito.when(loginService.loginUser(authorization)).thenReturn(ResponseEntity.ok(true));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isBadRequest());

        Mockito.verify(loginService, Mockito.times(0)).loginUser(authorization);
    }
}
