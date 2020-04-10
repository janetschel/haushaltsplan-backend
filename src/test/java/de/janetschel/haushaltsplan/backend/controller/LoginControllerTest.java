package de.janetschel.haushaltsplan.backend.controller;

import de.janetschel.haushaltsplan.backend.service.LoginService;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("/login -> 200 OK")
    public void givenValidHeader_whenLoginUser_thenTrue_andStatus200() throws Exception {
        String authorization = "samplestring";
        Mockito.when(loginService.loginUser(authorization)).thenReturn(ResponseEntity.ok(true));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/login").header("Auth-String", authorization))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("true"));

        Mockito.verify(loginService, Mockito.times(1)).loginUser(authorization);
    }

    @Test
    @DisplayName("/login invalid credentials -> 403 FORBIDDEN")
    public void givenInvalidHeader_whenLoginUser_thenFalse_andStatus403() throws Exception {
        String authorization = "wrongsamplestring";
        Mockito.when(loginService.loginUser(authorization)).thenReturn(ResponseEntity.status(HttpStatus.FORBIDDEN).body(false));

        this.mockMvc.perform(MockMvcRequestBuilders.get("/login").header("Auth-String", authorization))
                .andDo(MockMvcResultHandlers.print()).andExpect(MockMvcResultMatchers.status().isForbidden())
                .andExpect(MockMvcResultMatchers.content().string("false"));

        Mockito.verify(loginService, Mockito.times(1)).loginUser(authorization);
    }
}
